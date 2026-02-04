package com.ybiyrit.carboncalc

import com.ybiyrit.carboncalc.database.Database
import com.ybiyrit.carboncalc.routes.carbonRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Initialize database
    val database = Database()
    
    // Install CORS
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost() // For development - restrict in production
    }
    
    // Install Content Negotiation for JSON
    install(ContentNegotiation) {
        json()
    }
    
    // Configure routing
    routing {
        get("/") {
            call.respondText("Carbon Footprint Calculator API - Use /api/* endpoints", ContentType.Text.Plain)
        }
        
        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "healthy"))
        }
        
        carbonRoutes(database)
    }
    
    // Cleanup on shutdown
    environment.monitor.subscribe(ApplicationStopped) {
        database.close()
    }
}
