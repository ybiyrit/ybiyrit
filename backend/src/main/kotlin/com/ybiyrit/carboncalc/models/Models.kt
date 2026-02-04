package com.ybiyrit.carboncalc.models

import kotlinx.serialization.Serializable

@Serializable
data class CalculationRequest(
    val category: String,
    val activityType: String,
    val value: Double,
    val unit: String
)

@Serializable
data class CalculationResponse(
    val id: Long,
    val category: String,
    val activityType: String,
    val value: Double,
    val unit: String,
    val carbonKg: Double,
    val timestamp: String
)

@Serializable
data class SummaryResponse(
    val totalCarbonKg: Double,
    val recentCalculations: List<CalculationResponse>
)

@Serializable
data class ErrorResponse(
    val error: String
)
