package com.ybiyrit.carboncalc

object CarbonCalculator {
    // Carbon emission factors (kg CO2 per unit)
    private val emissionFactors = mapOf(
        // Transportation (kg CO2 per km)
        "transport_car_petrol" to 0.192,
        "transport_car_diesel" to 0.171,
        "transport_car_electric" to 0.053,
        "transport_bus" to 0.089,
        "transport_train" to 0.041,
        "transport_flight_short" to 0.255,
        "transport_flight_long" to 0.195,
        
        // Energy (kg CO2 per kWh)
        "energy_electricity" to 0.475,
        "energy_gas" to 0.185,
        "energy_heating_oil" to 0.265,
        
        // Food (kg CO2 per kg)
        "food_beef" to 27.0,
        "food_lamb" to 39.2,
        "food_pork" to 12.1,
        "food_chicken" to 6.9,
        "food_fish" to 5.7,
        "food_cheese" to 13.5,
        "food_milk" to 1.9,
        "food_eggs" to 4.8,
        "food_rice" to 2.7,
        "food_vegetables" to 2.0,
        
        // Waste (kg CO2 per kg)
        "waste_general" to 0.5,
        "waste_plastic" to 6.0,
        "waste_paper" to 0.3
    )

    fun calculate(category: String, activityType: String, value: Double): Double {
        val key = "${category}_${activityType}"
        val factor = emissionFactors[key] ?: throw IllegalArgumentException("Unknown activity type: $key")
        return value * factor
    }

    fun getActivityTypes(): Map<String, List<String>> {
        return mapOf(
            "transport" to listOf("car_petrol", "car_diesel", "car_electric", "bus", "train", "flight_short", "flight_long"),
            "energy" to listOf("electricity", "gas", "heating_oil"),
            "food" to listOf("beef", "lamb", "pork", "chicken", "fish", "cheese", "milk", "eggs", "rice", "vegetables"),
            "waste" to listOf("general", "plastic", "paper")
        )
    }
}
