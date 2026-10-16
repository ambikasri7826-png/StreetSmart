package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.Strings
import com.example.ui.components.VendorTopBar
import com.example.ui.screens.AddExpenseScreen
import com.example.ui.screens.AddIncomeScreen
import com.example.ui.screens.CashRealityCheckScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SavingsGoalScreen
import com.example.ui.screens.StockTrackerScreen
import com.example.ui.screens.TransactionHistoryScreen
import com.example.ui.theme.GradientRichRed
import com.example.ui.theme.MetricNegativeRed
import com.example.ui.viewmodel.StreetSmartUiState
import com.example.ui.viewmodel.StreetSmartViewModel

enum class ScreenDestination {
    DASHBOARD,
    ADD_INCOME,
    ADD_EXPENSE,
    CASH_REALITY_CHECK,
    STOCK_TRACKER,
    REPORTS,
    SAVINGS_GOALS,
    TRANSACTION_HISTORY
}

@Composable
fun StreetSmartApp(
    viewModel: StreetSmartViewModel,
    uiState: StreetSmartUiState
) {
    var currentScreen by remember { mutableStateOf(ScreenDestination.DASHBOARD) }
    val lang = uiState.language

    // Back handling to return to Dashboard from any secondary screen
    if (currentScreen != ScreenDestination.DASHBOARD) {
        BackHandler {
            currentScreen = ScreenDestination.DASHBOARD
        }
    }

    Scaffold(
        topBar = {
            VendorTopBar(
                currentLanguage = uiState.language,
                selectedDate = uiState.selectedDate,
                isDemoDay = uiState.summary.isDemoDay,
                onLanguageToggle = { viewModel.toggleLanguage() },
                onSelectDate = { viewModel.setSelectedDate(it) },
                onResetDemo = { viewModel.resetDemoData() },
                onClearAll = { viewModel.clearAllData() }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                // 1. Dashboard
                NavigationBarItem(
                    selected = currentScreen == ScreenDestination.DASHBOARD,
                    onClick = { currentScreen = ScreenDestination.DASHBOARD },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = {
                        Text(
                            Strings.get("nav_dashboard", lang),
                            fontSize = 10.sp,
                            fontWeight = if (currentScreen == ScreenDestination.DASHBOARD) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GradientRichRed,
                        selectedTextColor = GradientRichRed,
                        indicatorColor = Color(0xFFFFEEEE)
                    ),
                    modifier = Modifier.testTag("nav_item_dashboard")
                )

                // 2. Sales
                NavigationBarItem(
                    selected = currentScreen == ScreenDestination.ADD_INCOME,
                    onClick = { currentScreen = ScreenDestination.ADD_INCOME },
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Add Sales") },
                    label = {
                        Text(
                            Strings.get("nav_income", lang),
                            fontSize = 10.sp,
                            fontWeight = if (currentScreen == ScreenDestination.ADD_INCOME) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GradientRichRed,
                        selectedTextColor = GradientRichRed,
                        indicatorColor = Color(0xFFFFEEEE)
                    ),
                    modifier = Modifier.testTag("nav_item_income")
                )

                // 3. Expenses
                NavigationBarItem(
                    selected = currentScreen == ScreenDestination.ADD_EXPENSE,
                    onClick = { currentScreen = ScreenDestination.ADD_EXPENSE },
                    icon = { Icon(Icons.Default.RemoveCircle, contentDescription = "Add Expense") },
                    label = {
                        Text(
                            Strings.get("nav_expenses", lang),
                            fontSize = 10.sp,
                            fontWeight = if (currentScreen == ScreenDestination.ADD_EXPENSE) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MetricNegativeRed,
                        selectedTextColor = MetricNegativeRed,
                        indicatorColor = Color(0xFFFFDAD6)
                    ),
                    modifier = Modifier.testTag("nav_item_expense")
                )

                // 4. Cash Reality Check
                NavigationBarItem(
                    selected = currentScreen == ScreenDestination.CASH_REALITY_CHECK,
                    onClick = { currentScreen = ScreenDestination.CASH_REALITY_CHECK },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Cash Check") },
                    label = {
                        Text(
                            Strings.get("nav_cash_check", lang),
                            fontSize = 10.sp,
                            fontWeight = if (currentScreen == ScreenDestination.CASH_REALITY_CHECK) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GradientRichRed,
                        selectedTextColor = GradientRichRed,
                        indicatorColor = Color(0xFFFFEEEE)
                    ),
                    modifier = Modifier.testTag("nav_item_cash_check")
                )

                // 5. Stock / More Tools
                val isMoreSelected = currentScreen in listOf(
                    ScreenDestination.STOCK_TRACKER,
                    ScreenDestination.REPORTS,
                    ScreenDestination.SAVINGS_GOALS,
                    ScreenDestination.TRANSACTION_HISTORY
                )
                NavigationBarItem(
                    selected = isMoreSelected,
                    onClick = {
                        if (!isMoreSelected) {
                            currentScreen = ScreenDestination.STOCK_TRACKER
                        }
                    },
                    icon = { Icon(Icons.Default.Widgets, contentDescription = "Stock & More Tools") },
                    label = {
                        Text(
                            "Tools",
                            fontSize = 10.sp,
                            fontWeight = if (isMoreSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GradientRichRed,
                        selectedTextColor = GradientRichRed,
                        indicatorColor = Color(0xFFFFEEEE)
                    ),
                    modifier = Modifier.testTag("nav_item_tools")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Secondary Top Tabs for Tools (Stock, Reports, Savings, History)
            if (currentScreen in listOf(
                    ScreenDestination.STOCK_TRACKER,
                    ScreenDestination.REPORTS,
                    ScreenDestination.SAVINGS_GOALS,
                    ScreenDestination.TRANSACTION_HISTORY
                )
            ) {
                TabRow(
                    selectedTabIndex = when (currentScreen) {
                        ScreenDestination.STOCK_TRACKER -> 0
                        ScreenDestination.REPORTS -> 1
                        ScreenDestination.SAVINGS_GOALS -> 2
                        ScreenDestination.TRANSACTION_HISTORY -> 3
                        else -> 0
                    },
                    containerColor = Color.White,
                    contentColor = GradientRichRed,
                    indicator = { tabPositions ->
                        val index = when (currentScreen) {
                            ScreenDestination.STOCK_TRACKER -> 0
                            ScreenDestination.REPORTS -> 1
                            ScreenDestination.SAVINGS_GOALS -> 2
                            ScreenDestination.TRANSACTION_HISTORY -> 3
                            else -> 0
                        }
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[index]),
                            color = GradientRichRed
                        )
                    }
                ) {
                    Tab(
                        selected = currentScreen == ScreenDestination.STOCK_TRACKER,
                        onClick = { currentScreen = ScreenDestination.STOCK_TRACKER },
                        text = { Text(Strings.get("nav_stock", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = currentScreen == ScreenDestination.REPORTS,
                        onClick = { currentScreen = ScreenDestination.REPORTS },
                        text = { Text(Strings.get("nav_reports", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = currentScreen == ScreenDestination.SAVINGS_GOALS,
                        onClick = { currentScreen = ScreenDestination.SAVINGS_GOALS },
                        text = { Text(Strings.get("nav_savings", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = currentScreen == ScreenDestination.TRANSACTION_HISTORY,
                        onClick = { currentScreen = ScreenDestination.TRANSACTION_HISTORY },
                        text = { Text(Strings.get("nav_history", lang), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            // Screen Router
            when (currentScreen) {
                ScreenDestination.DASHBOARD -> {
                    DashboardScreen(
                        uiState = uiState,
                        onNavigateToAddIncome = { currentScreen = ScreenDestination.ADD_INCOME },
                        onNavigateToAddExpense = { currentScreen = ScreenDestination.ADD_EXPENSE },
                        onNavigateToRealityCheck = { currentScreen = ScreenDestination.CASH_REALITY_CHECK },
                        onNavigateToStock = { currentScreen = ScreenDestination.STOCK_TRACKER },
                        onNavigateToSavings = { currentScreen = ScreenDestination.SAVINGS_GOALS },
                        onNavigateToReports = { currentScreen = ScreenDestination.REPORTS },
                        onDeleteTransaction = { viewModel.deleteTransaction(it) }
                    )
                }

                ScreenDestination.ADD_INCOME -> {
                    AddIncomeScreen(
                        currentLanguage = lang,
                        selectedDate = uiState.selectedDate,
                        onSaveIncome = { amount, method, category, notes ->
                            viewModel.addTransaction("INCOME", amount, method, category, notes)
                        },
                        onBack = { currentScreen = ScreenDestination.DASHBOARD }
                    )
                }

                ScreenDestination.ADD_EXPENSE -> {
                    AddExpenseScreen(
                        currentLanguage = lang,
                        selectedDate = uiState.selectedDate,
                        onSaveExpense = { amount, method, category, notes ->
                            viewModel.addTransaction("EXPENSE", amount, method, category, notes)
                        },
                        onBack = { currentScreen = ScreenDestination.DASHBOARD }
                    )
                }

                ScreenDestination.CASH_REALITY_CHECK -> {
                    CashRealityCheckScreen(
                        uiState = uiState,
                        onSaveCashRegister = { opening, added, withdrawn, actualClosing, weatherTag, notes ->
                            viewModel.updateCashRegister(opening, added, withdrawn, actualClosing, weatherTag, notes)
                        },
                        onBack = { currentScreen = ScreenDestination.DASHBOARD }
                    )
                }

                ScreenDestination.STOCK_TRACKER -> {
                    StockTrackerScreen(
                        uiState = uiState,
                        onAddStockItem = { name, unit, purchased, sold, remaining, cost ->
                            viewModel.addStockItem(name, unit, purchased, sold, remaining, cost)
                        },
                        onDeleteStockItem = { viewModel.deleteStockItem(it) },
                        onBack = { currentScreen = ScreenDestination.DASHBOARD }
                    )
                }

                ScreenDestination.REPORTS -> {
                    ReportsScreen(
                        uiState = uiState,
                        onSetWeatherTag = { viewModel.setWeatherTag(it) },
                        onBack = { currentScreen = ScreenDestination.DASHBOARD }
                    )
                }

                ScreenDestination.SAVINGS_GOALS -> {
                    SavingsGoalScreen(
                        uiState = uiState,
                        onSaveGoal = { daily, weekly, savedToday, reason ->
                            viewModel.updateSavingsGoal(daily, weekly, savedToday, reason)
                        },
                        onSavePlan = { target, category, duration, daily, weekly, reason ->
                            viewModel.updateSavingsPlan(target, category, duration, daily, weekly, reason)
                        },
                        onLogDeposit = { amount ->
                            viewModel.logSavingsDeposit(amount)
                        },
                        onBack = { currentScreen = ScreenDestination.DASHBOARD }
                    )
                }

                ScreenDestination.TRANSACTION_HISTORY -> {
                    TransactionHistoryScreen(
                        uiState = uiState,
                        onDeleteTransaction = { viewModel.deleteTransaction(it) },
                        onBack = { currentScreen = ScreenDestination.DASHBOARD }
                    )
                }
            }
        }
    }
}
