package com.ybiyrit.carboncalc.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class Database(private val dbPath: String = "carbon_footprint.db") {
    private var connection: Connection? = null

    init {
        connect()
        createTables()
    }

    private fun connect() {
        connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        println("Connected to SQLite database at $dbPath")
    }

    private fun createTables() {
        val createCalculationsTable = """
            CREATE TABLE IF NOT EXISTS calculations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                category TEXT NOT NULL,
                activity_type TEXT NOT NULL,
                value REAL NOT NULL,
                unit TEXT NOT NULL,
                carbon_kg REAL NOT NULL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()

        connection?.createStatement()?.execute(createCalculationsTable)
    }

    fun saveCalculation(category: String, activityType: String, value: Double, unit: String, carbonKg: Double): Long {
        val sql = """
            INSERT INTO calculations (category, activity_type, value, unit, carbon_kg)
            VALUES (?, ?, ?, ?, ?)
        """.trimIndent()

        val statement = connection?.prepareStatement(sql)
        statement?.setString(1, category)
        statement?.setString(2, activityType)
        statement?.setDouble(3, value)
        statement?.setString(4, unit)
        statement?.setDouble(5, carbonKg)
        statement?.executeUpdate()

        // Get the last inserted row id
        val lastIdQuery = "SELECT last_insert_rowid()"
        val resultSet = connection?.createStatement()?.executeQuery(lastIdQuery)
        return if (resultSet?.next() == true) {
            resultSet.getLong(1)
        } else {
            -1L
        }
    }

    fun getCalculations(limit: Int = 50): List<Calculation> {
        val sql = "SELECT * FROM calculations ORDER BY timestamp DESC LIMIT ?"
        val statement = connection?.prepareStatement(sql)
        statement?.setInt(1, limit)
        val resultSet = statement?.executeQuery()

        val calculations = mutableListOf<Calculation>()
        while (resultSet?.next() == true) {
            calculations.add(resultSet.toCalculation())
        }
        return calculations
    }

    fun getTotalCarbon(): Double {
        val sql = "SELECT SUM(carbon_kg) as total FROM calculations"
        val resultSet = connection?.createStatement()?.executeQuery(sql)
        return if (resultSet?.next() == true) {
            resultSet.getDouble("total")
        } else {
            0.0
        }
    }

    fun close() {
        connection?.close()
    }

    private fun ResultSet.toCalculation(): Calculation {
        return Calculation(
            id = getLong("id"),
            category = getString("category"),
            activityType = getString("activity_type"),
            value = getDouble("value"),
            unit = getString("unit"),
            carbonKg = getDouble("carbon_kg"),
            timestamp = getString("timestamp")
        )
    }
}

data class Calculation(
    val id: Long,
    val category: String,
    val activityType: String,
    val value: Double,
    val unit: String,
    val carbonKg: Double,
    val timestamp: String
)
