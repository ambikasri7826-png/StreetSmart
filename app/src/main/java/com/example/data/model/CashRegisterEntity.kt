package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cash_registers")
data class CashRegisterEntity(
    @PrimaryKey
    val date: String, // "YYYY-MM-DD"
    val openingCash: Double = 0.0,
    val cashAdded: Double = 0.0,
    val cashWithdrawn: Double = 0.0,
    val actualClosingCash: Double = 0.0,
    val weatherTag: String = "tag_sunny",
    val notes: String = "",
    val isDemo: Boolean = false
)
