package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.StreetSmartDao
import com.example.data.model.CashRegisterEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.StockItemEntity
import com.example.data.model.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CashRegisterEntity::class,
        StockItemEntity::class,
        SavingsGoalEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class StreetSmartDatabase : RoomDatabase() {
    abstract fun dao(): StreetSmartDao

    companion object {
        @Volatile
        private var INSTANCE: StreetSmartDatabase? = null

        fun getDatabase(context: Context): StreetSmartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StreetSmartDatabase::class.java,
                    "streetsmart.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
