package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_items")
data class StockItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemName: String,
    val unit: String = "kg",
    val quantityPurchased: Double = 0.0,
    val quantitySold: Double = 0.0,
    val quantityRemaining: Double = 0.0,
    val purchaseCostPerUnit: Double = 0.0,
    val date: String, // "YYYY-MM-DD"
    val isDemo: Boolean = false
) {
    // Calculated unsold or wasted quantity
    val unsoldQuantity: Double
        get() = (quantityPurchased - quantitySold).coerceAtLeast(0.0)

    // Wastage occurs when unsold quantity exceeds physically remaining salvageable stock
    val wastageQuantity: Double
        get() = (unsoldQuantity - quantityRemaining).coerceAtLeast(0.0)

    val wastageCost: Double
        get() = wastageQuantity * purchaseCostPerUnit
}
