package com.ybiyrit.carboncalc.routes

import com.ybiyrit.carboncalc.CarbonCalculator
import com.ybiyrit.carboncalc.database.Database
import com.ybiyrit.carboncalc.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.carbonRoutes(database: Database) {
    route("/api") {
        // Calculate carbon footprint
        post("/calculate") {
            try {
                val request = call.receive<CalculationRequest>()
                
                // Calculate carbon emissions
                val carbonKg = CarbonCalculator.calculate(
                    request.category,
                    request.activityType,
                    request.value
                )
                
                // Save to database
                val id = database.saveCalculation(
                    request.category,
                    request.activityType,
                    request.value,
                    request.unit,
                    carbonKg
                )
                
                call.respond(
                    HttpStatusCode.Created,
                    CalculationResponse(
                        id = id,
                        category = request.category,
                        activityType = request.activityType,
                        value = request.value,
                        unit = request.unit,
                        carbonKg = carbonKg,
                        timestamp = java.time.Instant.now().toString()
                    )
                )
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Invalid input"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Server error: ${e.message}"))
            }
        }
        
        // Get calculation history
        get("/calculations") {
            try {
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 50
                val calculations = database.getCalculations(limit)
                
                val response = calculations.map {
                    CalculationResponse(
                        id = it.id,
                        category = it.category,
                        activityType = it.activityType,
                        value = it.value,
                        unit = it.unit,
                        carbonKg = it.carbonKg,
                        timestamp = it.timestamp
                    )
                }
                
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Server error: ${e.message}"))
            }
        }
        
        // Get summary (total carbon + recent calculations)
        get("/summary") {
            try {
                val totalCarbon = database.getTotalCarbon()
                val recentCalculations = database.getCalculations(10).map {
                    CalculationResponse(
                        id = it.id,
                        category = it.category,
                        activityType = it.activityType,
                        value = it.value,
                        unit = it.unit,
                        carbonKg = it.carbonKg,
                        timestamp = it.timestamp
                    )
                }
                
                call.respond(
                    HttpStatusCode.OK,
                    SummaryResponse(
                        totalCarbonKg = totalCarbon,
                        recentCalculations = recentCalculations
                    )
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Server error: ${e.message}"))
            }
        }
        
        // Get available activity types
        get("/activity-types") {
            call.respond(HttpStatusCode.OK, CarbonCalculator.getActivityTypes())
        }
    }
}
