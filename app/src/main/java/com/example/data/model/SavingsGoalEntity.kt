package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goal")
data class SavingsGoalEntity(
    @PrimaryKey
    val id: Int = 1,
    val dailyTarget: Double = 300.0,
    val weeklyTarget: Double = 1800.0,
    val goalReason: String = "Reserve for wholesale market & stall fees",
    val manualSavedAmount: Double = 150.0,
    val planCategory: String = "plan_wholesale",
    val planTargetAmount: Double = 10000.0,
    val planAccumulatedAmount: Double = 3200.0,
    val planDaysDuration: Int = 30,
    val dailyContributionPercent: Double = 15.0
)
