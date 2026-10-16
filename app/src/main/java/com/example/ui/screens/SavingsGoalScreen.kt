package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Festival
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.components.formatRupee
import com.example.ui.theme.CashAmber
import com.example.ui.theme.CashAmberContainer
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.OnCashAmberContainer
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.theme.OnShortageRedContainer
import com.example.ui.theme.ShortageRed
import com.example.ui.theme.ShortageRedContainer
import com.example.ui.theme.UpiTealContainer
import com.example.ui.viewmodel.StreetSmartUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SavingsGoalScreen(
    uiState: StreetSmartUiState,
    onSaveGoal: (daily: Double, weekly: Double, savedToday: Double, reason: String) -> Unit,
    onSavePlan: (targetAmount: Double, category: String, durationDays: Int, daily: Double, weekly: Double, reason: String) -> Unit,
    onLogDeposit: (amount: Double) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language
    val goal = uiState.savingsGoal
    val summary = uiState.summary

    var selectedPlanCategory by remember(goal) { mutableStateOf(goal.planCategory) }
    var targetAmountText by remember(goal) { mutableStateOf(goal.planTargetAmount.toInt().toString()) }
    var durationDaysText by remember(goal) { mutableStateOf(goal.planDaysDuration.toString()) }
    var dailyTargetText by remember(goal) { mutableStateOf(goal.dailyTarget.toInt().toString()) }
    var weeklyTargetText by remember(goal) { mutableStateOf(goal.weeklyTarget.toInt().toString()) }
    var reasonText by remember(goal) { mutableStateOf(goal.goalReason) }
    var customDepositText by remember { mutableStateOf("") }
    var showSavedToast by remember { mutableStateOf(false) }

    val planTarget = targetAmountText.toDoubleOrNull() ?: 10000.0
    val accumulated = goal.planAccumulatedAmount
    val planProgress = (accumulated / planTarget).toFloat().coerceIn(0f, 1f)
    val remainingToSave = (planTarget - accumulated).coerceAtLeast(0.0)

    // Recommended daily deposit: 15% of today's operating profit, or minimum ₹50
    val recommendedDeposit = (summary.estimatedOperatingProfit * 0.15).toInt().coerceAtLeast(50)

    val planPresets = listOf(
        Pair("plan_wholesale", Icons.Default.Inventory),
        Pair("plan_repair", Icons.Default.Build),
        Pair("plan_emergency", Icons.Default.MedicalServices),
        Pair("plan_loan", Icons.Default.Handshake),
        Pair("plan_festival", Icons.Default.Festival)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("savings_goal_screen")
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("savings_back_btn")) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = Strings.get("savings_plan_header", lang),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Actionable micro-reserves & capital protection",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Critical Safeguard Banner
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ShortageRedContainer),
            modifier = Modifier.fillMaxWidth().testTag("savings_caution_card")
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = ShortageRed,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Golden Rule for Street Vendors",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnShortageRedContainer
                        )
                    )
                    Text(
                        text = Strings.get("savings_caution", lang),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnShortageRedContainer,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Plan Category Selector Chips
        Text(
            text = "Choose Your Active Savings Plan",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            planPresets.forEach { (cat, icon) ->
                val isSelected = selectedPlanCategory == cat
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) EmeraldDark else Color.White,
                    border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD6DDD5)) else null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { selectedPlanCategory = cat }
                        .testTag("plan_chip_${cat}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else Color(0xFF2E3133),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = Strings.get(cat, lang),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF2E3133)
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Savings Plan Progress Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("savings_plan_progress_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = Strings.get(selectedPlanCategory, lang),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${(planProgress * 100).toInt()}% completed (${formatRupee(remainingToSave)} remaining)",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(EmeraldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = EmeraldDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { planProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = EmeraldDark,
                    trackColor = Color(0xFFE8EFEA)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF7F9F6), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = Strings.get("accumulated_so_far", lang),
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontSize = 10.sp)
                        )
                        Text(
                            text = formatRupee(accumulated),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = EmeraldDark)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = Strings.get("target_goal", lang),
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontSize = 10.sp)
                        )
                        Text(
                            text = formatRupee(planTarget),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1C1E))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Recommended Safe Deposit Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${Strings.get("deposit_recommended", lang)}: ₹$recommendedDeposit",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnEmeraldContainer
                                )
                            )
                            Text(
                                text = "Based on 15% of today's operating profit (₹${summary.estimatedOperatingProfit.toInt()})",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = OnEmeraldContainer,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        Button(
                            onClick = {
                                onLogDeposit(recommendedDeposit.toDouble())
                                showSavedToast = true
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                            modifier = Modifier.testTag("deposit_recommended_btn")
                        ) {
                            Text("Deposit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Deposit Buttons
                Text(
                    text = "Quick Log to Savings Drawer / Envelope",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(50, 100, 200, 500).forEach { amt ->
                        OutlinedButton(
                            onClick = {
                                onLogDeposit(amt.toDouble())
                                showSavedToast = true
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("quick_deposit_$amt")
                        ) {
                            Text("+₹$amt", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Custom Deposit Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = customDepositText,
                        onValueChange = { if (it.all { c -> c.isDigit() }) customDepositText = it },
                        placeholder = { Text("Custom ₹") },
                        prefix = { Text("₹ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("custom_deposit_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Button(
                        onClick = {
                            val amt = customDepositText.toDoubleOrNull()
                            if (amt != null && amt > 0) {
                                onLogDeposit(amt)
                                customDepositText = ""
                                showSavedToast = true
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                        modifier = Modifier.testTag("custom_deposit_submit_btn")
                    ) {
                        Text(Strings.get("add_deposit_btn", lang), fontWeight = FontWeight.Bold)
                    }
                }

                if (showSavedToast) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✅ Savings deposit logged successfully!",
                        color = EmeraldDark,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Plan Settings & Targets Configuration Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Configure Savings Plan Parameters",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                OutlinedTextField(
                    value = targetAmountText,
                    onValueChange = { if (it.all { c -> c.isDigit() }) targetAmountText = it },
                    label = { Text("Total Target Goal Amount (₹)") },
                    prefix = { Text("₹ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("input_plan_target")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = durationDaysText,
                        onValueChange = { if (it.all { c -> c.isDigit() }) durationDaysText = it },
                        label = { Text("Duration (Days)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("input_plan_days")
                    )
                    OutlinedTextField(
                        value = dailyTargetText,
                        onValueChange = { if (it.all { c -> c.isDigit() }) dailyTargetText = it },
                        label = { Text("Daily Target") },
                        prefix = { Text("₹ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("input_daily_target")
                    )
                }

                OutlinedTextField(
                    value = reasonText,
                    onValueChange = { reasonText = it },
                    label = { Text("Plan Description (e.g. Mandi advance, new stove, chit)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_savings_reason")
                )

                Button(
                    onClick = {
                        val target = targetAmountText.toDoubleOrNull() ?: 10000.0
                        val days = durationDaysText.toIntOrNull() ?: 30
                        val daily = dailyTargetText.toDoubleOrNull() ?: 300.0
                        val weekly = weeklyTargetText.toDoubleOrNull() ?: 1800.0
                        onSavePlan(target, selectedPlanCategory, days, daily, weekly, reasonText.trim())
                        showSavedToast = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_savings_plan_btn")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Plan Settings", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
