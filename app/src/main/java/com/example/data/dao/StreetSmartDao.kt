package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CashRegisterEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.StockItemEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StreetSmartDao {
    // Transactions
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date = :date ORDER BY timestamp DESC")
    fun getTransactionsByDate(date: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun clearAllTransactions()

    @Query("DELETE FROM transactions WHERE isDemo = 1")
    suspend fun deleteDemoTransactions()

    // Cash Registers
    @Query("SELECT * FROM cash_registers ORDER BY date DESC")
    fun getAllCashRegisters(): Flow<List<CashRegisterEntity>>

    @Query("SELECT * FROM cash_registers WHERE date = :date LIMIT 1")
    fun getCashRegister(date: String): Flow<CashRegisterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCashRegister(register: CashRegisterEntity)

    @Query("DELETE FROM cash_registers")
    suspend fun clearAllCashRegisters()

    @Query("DELETE FROM cash_registers WHERE isDemo = 1")
    suspend fun deleteDemoCashRegisters()

    // Stock Items
    @Query("SELECT * FROM stock_items ORDER BY id DESC")
    fun getAllStockItems(): Flow<List<StockItemEntity>>

    @Query("SELECT * FROM stock_items WHERE date = :date ORDER BY id DESC")
    fun getStockItemsByDate(date: String): Flow<List<StockItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockItem(item: StockItemEntity): Long

    @Update
    suspend fun updateStockItem(item: StockItemEntity)

    @Delete
    suspend fun deleteStockItem(item: StockItemEntity)

    @Query("DELETE FROM stock_items WHERE id = :id")
    suspend fun deleteStockItemById(id: Long)

    @Query("DELETE FROM stock_items")
    suspend fun clearAllStockItems()

    @Query("DELETE FROM stock_items WHERE isDemo = 1")
    suspend fun deleteDemoStockItems()

    // Savings Goal
    @Query("SELECT * FROM savings_goal WHERE id = 1 LIMIT 1")
    fun getSavingsGoal(): Flow<SavingsGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSavingsGoal(goal: SavingsGoalEntity)
}
