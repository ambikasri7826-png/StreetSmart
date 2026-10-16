package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "INCOME" or "EXPENSE"
    val amount: Double,
    val paymentMethod: String, // "CASH" or "UPI"
    val category: String, // e.g. "cat_tea_coffee", "cat_stock_purchase"
    val notes: String = "",
    val date: String, // ISO date "YYYY-MM-DD"
    val timestamp: Long = System.currentTimeMillis(),
    val isDemo: Boolean = false
)
