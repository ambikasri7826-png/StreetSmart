package com.example.data

import com.example.data.dao.StreetSmartDao
import com.example.data.model.CashRegisterEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.StockItemEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StreetSmartRepository(private val dao: StreetSmartDao) {

    val allTransactions: Flow<List<TransactionEntity>> = dao.getAllTransactions()
    val allCashRegisters: Flow<List<CashRegisterEntity>> = dao.getAllCashRegisters()
    val allStockItems: Flow<List<StockItemEntity>> = dao.getAllStockItems()
    val savingsGoal: Flow<SavingsGoalEntity?> = dao.getSavingsGoal()

    fun getTransactionsForDate(date: String): Flow<List<TransactionEntity>> =
        dao.getTransactionsByDate(date)

    fun getCashRegisterForDate(date: String): Flow<CashRegisterEntity?> =
        dao.getCashRegister(date)

    fun getStockForDate(date: String): Flow<List<StockItemEntity>> =
        dao.getStockItemsByDate(date)

    suspend fun saveTransaction(tx: TransactionEntity): Long = dao.insertTransaction(tx)

    suspend fun updateTransaction(tx: TransactionEntity) = dao.updateTransaction(tx)

    suspend fun deleteTransaction(tx: TransactionEntity) = dao.deleteTransaction(tx)

    suspend fun deleteTransactionById(id: Long) = dao.deleteTransactionById(id)

    suspend fun saveCashRegister(register: CashRegisterEntity) =
        dao.insertOrUpdateCashRegister(register)

    suspend fun saveStockItem(item: StockItemEntity): Long = dao.insertStockItem(item)

    suspend fun updateStockItem(item: StockItemEntity) = dao.updateStockItem(item)

    suspend fun deleteStockItem(item: StockItemEntity) = dao.deleteStockItem(item)

    suspend fun deleteStockItemById(id: Long) = dao.deleteStockItemById(id)

    suspend fun saveSavingsGoal(goal: SavingsGoalEntity) = dao.insertOrUpdateSavingsGoal(goal)

    suspend fun clearAllData() {
        dao.clearAllTransactions()
        dao.clearAllCashRegisters()
        dao.clearAllStockItems()
    }

    suspend fun resetDemoData(targetDate: String = getTodayDate()) {
        // Clear previous demo entries
        dao.deleteDemoTransactions()
        dao.deleteDemoCashRegisters()
        dao.deleteDemoStockItems()

        // Seed requested exact Demo Scenario:
        // Opening cash: ₹500
        // Cash sales: ₹2,000
        // UPI sales: ₹1,000
        // Cash expenses: ₹700
        // Cash added: ₹0
        // Cash withdrawn: ₹0
        // Actual closing cash: ₹1,750
        // Expected closing cash = ₹500 + ₹2,000 - ₹700 = ₹1,800
        // Cash difference = ₹1,750 - ₹1,800 = -₹50

        // 1. Transactions
        dao.insertTransaction(
            TransactionEntity(
                type = "INCOME",
                amount = 1200.0,
                paymentMethod = "CASH",
                category = "cat_meals",
                notes = "Lunch rush cash collections (demo)",
                date = targetDate,
                isDemo = true
            )
        )
        dao.insertTransaction(
            TransactionEntity(
                type = "INCOME",
                amount = 800.0,
                paymentMethod = "CASH",
                category = "cat_tea_coffee",
                notes = "Evening tea & snacks cash (demo)",
                date = targetDate,
                isDemo = true
            )
        )
        dao.insertTransaction(
            TransactionEntity(
                type = "INCOME",
                amount = 600.0,
                paymentMethod = "UPI",
                category = "cat_meals",
                notes = "GPay lunch QR (demo)",
                date = targetDate,
                isDemo = true
            )
        )
        dao.insertTransaction(
            TransactionEntity(
                type = "INCOME",
                amount = 400.0,
                paymentMethod = "UPI",
                category = "cat_snacks",
                notes = "PhonePe chai orders (demo)",
                date = targetDate,
                isDemo = true
            )
        )

        // Expenses totaling ₹700 in cash
        dao.insertTransaction(
            TransactionEntity(
                type = "EXPENSE",
                amount = 450.0,
                paymentMethod = "CASH",
                category = "cat_stock_purchase",
                notes = "Morning market vegetables & milk (demo)",
                date = targetDate,
                isDemo = true
            )
        )
        dao.insertTransaction(
            TransactionEntity(
                type = "EXPENSE",
                amount = 150.0,
                paymentMethod = "CASH",
                category = "cat_transport",
                notes = "Auto carriage from wholesale mandi (demo)",
                date = targetDate,
                isDemo = true
            )
        )
        dao.insertTransaction(
            TransactionEntity(
                type = "EXPENSE",
                amount = 100.0,
                paymentMethod = "CASH",
                category = "cat_packaging",
                notes = "Paper cups & meal containers (demo)",
                date = targetDate,
                isDemo = true
            )
        )

        // 2. Cash Register for the day
        dao.insertOrUpdateCashRegister(
            CashRegisterEntity(
                date = targetDate,
                openingCash = 500.0,
                cashAdded = 0.0,
                cashWithdrawn = 0.0,
                actualClosingCash = 1750.0,
                weatherTag = "tag_rainy",
                notes = "Rain in afternoon slowed 3-4 PM walk-in footfall",
                isDemo = true
            )
        )

        // 3. Stock items (demonstrating unsold stock & wastage calculations)
        dao.insertStockItem(
            StockItemEntity(
                itemName = "Tomatoes / தக்காளி",
                unit = "kg",
                quantityPurchased = 30.0,
                quantitySold = 25.0,
                quantityRemaining = 3.0,
                purchaseCostPerUnit = 25.0,
                date = targetDate,
                isDemo = true
            )
        )
        dao.insertStockItem(
            StockItemEntity(
                itemName = "Milk Packets / பால்",
                unit = "pieces",
                quantityPurchased = 20.0,
                quantitySold = 18.0,
                quantityRemaining = 2.0,
                purchaseCostPerUnit = 30.0,
                date = targetDate,
                isDemo = true
            )
        )

        // 4. Default savings goal
        dao.insertOrUpdateSavingsGoal(
            SavingsGoalEntity(
                id = 1,
                dailyTarget = 300.0,
                weeklyTarget = 1800.0,
                goalReason = "Chit fund installment & cart maintenance",
                manualSavedAmount = 150.0
            )
        )
    }

    companion object {
        fun getTodayDate(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }

        fun getYesterdayDate(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val cal = java.util.Calendar.getInstance()
            cal.add(java.util.Calendar.DATE, -1)
            return sdf.format(cal.time)
        }
    }
}
