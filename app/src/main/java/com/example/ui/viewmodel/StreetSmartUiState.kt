package com.example.ui.viewmodel

import com.example.data.model.CashRegisterEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.StockItemEntity
import com.example.data.model.TransactionEntity
import com.example.localization.AppLanguage

enum class CashStatus {
    BALANCED,
    SHORTAGE,
    SURPLUS
}

data class DailyFinancialSummary(
    val selectedDate: String,
    val totalSales: Double = 0.0,
    val cashSales: Double = 0.0,
    val upiSales: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val cashExpenses: Double = 0.0,
    val otherExpenses: Double = 0.0,
    val openingCash: Double = 0.0,
    val cashAdded: Double = 0.0,
    val cashWithdrawn: Double = 0.0,
    val expectedClosingCash: Double = 0.0,
    val actualClosingCash: Double = 0.0,
    val cashDifference: Double = 0.0,
    val cashStatus: CashStatus = CashStatus.BALANCED,
    val estimatedOperatingProfit: Double = 0.0,
    val totalUnsoldStockQty: Double = 0.0,
    val totalWastageQty: Double = 0.0,
    val totalWastageCost: Double = 0.0,
    val weatherTag: String = "tag_sunny",
    val isDemoDay: Boolean = false
)

data class WeatherSalesMetric(
    val weatherTag: String,
    val dayCount: Int,
    val totalSales: Double,
    val averageSales: Double
)

data class StreetSmartUiState(
    val selectedDate: String = "",
    val language: AppLanguage = AppLanguage.ENGLISH,
    val summary: DailyFinancialSummary = DailyFinancialSummary(selectedDate = ""),
    val currentDayTransactions: List<TransactionEntity> = emptyList(),
    val allTransactions: List<TransactionEntity> = emptyList(),
    val currentDayCashRegister: CashRegisterEntity? = null,
    val currentDayStockItems: List<StockItemEntity> = emptyList(),
    val allStockItems: List<StockItemEntity> = emptyList(),
    val savingsGoal: SavingsGoalEntity = SavingsGoalEntity(),
    val weatherMetrics: List<WeatherSalesMetric> = emptyList(),
    val isLoading: Boolean = false,
    val userNotification: String? = null
)
