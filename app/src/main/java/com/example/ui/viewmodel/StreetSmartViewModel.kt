package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.StreetSmartDatabase
import com.example.data.StreetSmartRepository
import com.example.data.model.CashRegisterEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.StockItemEntity
import com.example.data.model.TransactionEntity
import com.example.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StreetSmartViewModel(
    application: Application,
    private val repository: StreetSmartRepository
) : AndroidViewModel(application) {

    private val _selectedDate = MutableStateFlow(StreetSmartRepository.getTodayDate())
    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    private val _notification = MutableStateFlow<String?>(null)

    val uiState: StateFlow<StreetSmartUiState> = combine(
        combine(_selectedDate, _language) { date, lang -> Pair(date, lang) },
        repository.allTransactions,
        repository.allCashRegisters,
        repository.allStockItems,
        repository.savingsGoal
    ) { (date, lang), allTxs, allRegisters, allStock, goal ->
        val dayTxs = allTxs.filter { it.date == date }
        val dayRegister = allRegisters.find { it.date == date }
        val dayStock = allStock.filter { it.date == date }

        // Sales calculations
        val incomeTxs = dayTxs.filter { it.type == "INCOME" }
        val totalSales = incomeTxs.sumOf { it.amount }
        val cashSales = incomeTxs.filter { it.paymentMethod == "CASH" }.sumOf { it.amount }
        val upiSales = incomeTxs.filter { it.paymentMethod == "UPI" }.sumOf { it.amount }

        // Expenses calculations
        val expenseTxs = dayTxs.filter { it.type == "EXPENSE" }
        val totalExpenses = expenseTxs.sumOf { it.amount }
        val cashExpenses = expenseTxs.filter { it.paymentMethod == "CASH" }.sumOf { it.amount }
        val otherExpenses = expenseTxs.filter { it.paymentMethod != "CASH" }.sumOf { it.amount }

        // Cash register parameters
        val openingCash = dayRegister?.openingCash ?: 0.0
        val cashAdded = dayRegister?.cashAdded ?: 0.0
        val cashWithdrawn = dayRegister?.cashWithdrawn ?: 0.0
        val actualClosing = dayRegister?.actualClosingCash ?: 0.0

        // Exact formula:
        // Expected closing cash = Opening cash + Cash sales + Cash added − Cash expenses − Cash withdrawn
        val expectedClosing = openingCash + cashSales + cashAdded - cashExpenses - cashWithdrawn
        val cashDiff = actualClosing - expectedClosing

        val cashStatus = when {
            kotlin.math.abs(cashDiff) < 0.01 -> CashStatus.BALANCED
            cashDiff < 0 -> CashStatus.SHORTAGE
            else -> CashStatus.SURPLUS
        }

        val estimatedOperatingProfit = totalSales - totalExpenses

        val totalUnsoldQty = dayStock.sumOf { it.unsoldQuantity }
        val totalWastageQty = dayStock.sumOf { it.wastageQuantity }
        val totalWastageCost = dayStock.sumOf { it.wastageCost }

        val isDemo = (dayRegister?.isDemo == true) || dayTxs.any { it.isDemo } || dayStock.any { it.isDemo }

        val summary = DailyFinancialSummary(
            selectedDate = date,
            totalSales = totalSales,
            cashSales = cashSales,
            upiSales = upiSales,
            totalExpenses = totalExpenses,
            cashExpenses = cashExpenses,
            otherExpenses = otherExpenses,
            openingCash = openingCash,
            cashAdded = cashAdded,
            cashWithdrawn = cashWithdrawn,
            expectedClosingCash = expectedClosing,
            actualClosingCash = actualClosing,
            cashDifference = cashDiff,
            cashStatus = cashStatus,
            estimatedOperatingProfit = estimatedOperatingProfit,
            totalUnsoldStockQty = totalUnsoldQty,
            totalWastageQty = totalWastageQty,
            totalWastageCost = totalWastageCost,
            weatherTag = dayRegister?.weatherTag ?: "tag_sunny",
            isDemoDay = isDemo
        )

        // Weather comparisons across recorded days
        val allTags = listOf("tag_sunny", "tag_rainy", "tag_cloudy", "tag_festival", "tag_normal")
        val weatherMetrics = allTags.map { tag ->
            val datesWithTag = allRegisters.filter { it.weatherTag == tag }.map { it.date }.toSet()
            val totalTagSales = allTxs.filter { it.type == "INCOME" && it.date in datesWithTag }.sumOf { it.amount }
            val count = datesWithTag.size.coerceAtLeast(if (totalTagSales > 0) 1 else 0)
            WeatherSalesMetric(
                weatherTag = tag,
                dayCount = count,
                totalSales = totalTagSales,
                averageSales = if (count > 0) totalTagSales / count else 0.0
            )
        }

        StreetSmartUiState(
            selectedDate = date,
            language = lang,
            summary = summary,
            currentDayTransactions = dayTxs,
            allTransactions = allTxs,
            currentDayCashRegister = dayRegister,
            currentDayStockItems = dayStock,
            allStockItems = allStock,
            savingsGoal = goal ?: SavingsGoalEntity(),
            weatherMetrics = weatherMetrics,
            userNotification = _notification.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StreetSmartUiState()
    )

    init {
        // Preload demo scenario on first launch if database is clean
        viewModelScope.launch {
            repository.resetDemoData(StreetSmartRepository.getTodayDate())
        }
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.ENGLISH) {
            AppLanguage.TAMIL
        } else {
            AppLanguage.ENGLISH
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun addTransaction(
        type: String,
        amount: Double,
        paymentMethod: String,
        category: String,
        notes: String,
        date: String = _selectedDate.value
    ) {
        if (amount <= 0.0) return
        viewModelScope.launch {
            val tx = TransactionEntity(
                type = type,
                amount = amount,
                paymentMethod = paymentMethod,
                category = category,
                notes = notes,
                date = date,
                isDemo = false
            )
            repository.saveTransaction(tx)
        }
    }

    fun updateTransaction(tx: TransactionEntity) {
        if (tx.amount <= 0.0) return
        viewModelScope.launch {
            repository.updateTransaction(tx.copy(isDemo = false))
        }
    }

    fun deleteTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(tx)
        }
    }

    fun updateCashRegister(
        openingCash: Double,
        cashAdded: Double,
        cashWithdrawn: Double,
        actualClosingCash: Double,
        weatherTag: String,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val current = uiState.value.currentDayCashRegister
            val register = CashRegisterEntity(
                date = _selectedDate.value,
                openingCash = openingCash.coerceAtLeast(0.0),
                cashAdded = cashAdded.coerceAtLeast(0.0),
                cashWithdrawn = cashWithdrawn.coerceAtLeast(0.0),
                actualClosingCash = actualClosingCash.coerceAtLeast(0.0),
                weatherTag = weatherTag,
                notes = notes,
                isDemo = false
            )
            repository.saveCashRegister(register)
        }
    }

    fun setWeatherTag(tag: String) {
        viewModelScope.launch {
            val current = uiState.value.currentDayCashRegister
            val updated = current?.copy(weatherTag = tag, isDemo = false)
                ?: CashRegisterEntity(date = _selectedDate.value, weatherTag = tag, isDemo = false)
            repository.saveCashRegister(updated)
        }
    }

    fun addStockItem(
        itemName: String,
        unit: String,
        purchased: Double,
        sold: Double,
        remaining: Double,
        costPerUnit: Double
    ) {
        if (itemName.isBlank()) return
        viewModelScope.launch {
            val item = StockItemEntity(
                itemName = itemName.trim(),
                unit = unit,
                quantityPurchased = purchased.coerceAtLeast(0.0),
                quantitySold = sold.coerceAtLeast(0.0),
                quantityRemaining = remaining.coerceAtLeast(0.0),
                purchaseCostPerUnit = costPerUnit.coerceAtLeast(0.0),
                date = _selectedDate.value,
                isDemo = false
            )
            repository.saveStockItem(item)
        }
    }

    fun updateStockItem(item: StockItemEntity) {
        viewModelScope.launch {
            repository.updateStockItem(item.copy(isDemo = false))
        }
    }

    fun deleteStockItem(item: StockItemEntity) {
        viewModelScope.launch {
            repository.deleteStockItem(item)
        }
    }

    fun updateSavingsGoal(daily: Double, weekly: Double, savedToday: Double, reason: String) {
        viewModelScope.launch {
            val current = uiState.value.savingsGoal
            val goal = current.copy(
                dailyTarget = daily.coerceAtLeast(0.0),
                weeklyTarget = weekly.coerceAtLeast(0.0),
                manualSavedAmount = savedToday.coerceAtLeast(0.0),
                goalReason = reason
            )
            repository.saveSavingsGoal(goal)
        }
    }

    fun updateSavingsPlan(
        targetAmount: Double,
        category: String,
        durationDays: Int,
        dailyTarget: Double,
        weeklyTarget: Double,
        reason: String
    ) {
        viewModelScope.launch {
            val current = uiState.value.savingsGoal
            val updated = current.copy(
                planTargetAmount = targetAmount.coerceAtLeast(100.0),
                planCategory = category,
                planDaysDuration = durationDays.coerceAtLeast(1),
                dailyTarget = dailyTarget.coerceAtLeast(0.0),
                weeklyTarget = weeklyTarget.coerceAtLeast(0.0),
                goalReason = reason
            )
            repository.saveSavingsGoal(updated)
        }
    }

    fun logSavingsDeposit(amount: Double) {
        if (amount <= 0.0) return
        viewModelScope.launch {
            val current = uiState.value.savingsGoal
            val updated = current.copy(
                planAccumulatedAmount = current.planAccumulatedAmount + amount,
                manualSavedAmount = current.manualSavedAmount + amount
            )
            repository.saveSavingsGoal(updated)
        }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.resetDemoData(_selectedDate.value)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val db = StreetSmartDatabase.getDatabase(application)
                    val repo = StreetSmartRepository(db.dao())
                    return StreetSmartViewModel(application, repo) as T
                }
            }
    }
}
