package com.example

import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testCashRealityCheckReconciliation() {
    val openingCash = 500.0
    val cashSales = 2000.0
    val cashAdded = 0.0
    val cashExpenses = 700.0
    val cashWithdrawn = 0.0
    val actualClosingCash = 1750.0

    // Formula: Expected = Opening + Cash sales + Cash added - Cash expenses - Cash withdrawn
    val expectedClosingCash = openingCash + cashSales + cashAdded - cashExpenses - cashWithdrawn
    assertEquals(1800.0, expectedClosingCash, 0.001)

    // Cash difference = Actual - Expected
    val cashDifference = actualClosingCash - expectedClosingCash
    assertEquals(-50.0, cashDifference, 0.001)
  }

  @Test
  fun testStockUnsoldAndWastageCalculation() {
    val purchased = 30.0
    val sold = 25.0
    val remaining = 3.0
    val costPerUnit = 25.0

    val unsold = (purchased - sold).coerceAtLeast(0.0)
    assertEquals(5.0, unsold, 0.001)

    val wastage = (unsold - remaining).coerceAtLeast(0.0)
    assertEquals(2.0, wastage, 0.001)

    val wastageCost = wastage * costPerUnit
    assertEquals(50.0, wastageCost, 0.001)
  }

  @Test
  fun testSavingsPlanProgress() {
    val targetAmount = 10000.0
    val accumulated = 3200.0
    val deposit = 300.0

    val updatedAccumulated = accumulated + deposit
    assertEquals(3500.0, updatedAccumulated, 0.001)

    val progress = (updatedAccumulated / targetAmount).toFloat()
    assertEquals(0.35f, progress, 0.001f)

    val remaining = targetAmount - updatedAccumulated
    assertEquals(6500.0, remaining, 0.001)
  }
}
