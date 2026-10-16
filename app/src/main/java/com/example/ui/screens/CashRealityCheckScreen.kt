package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.OnSurplusGreenContainer
import com.example.ui.theme.OnUpiTealContainer
import com.example.ui.theme.ShortageRed
import com.example.ui.theme.ShortageRedContainer
import com.example.ui.theme.SurplusGreen
import com.example.ui.theme.SurplusGreenContainer
import com.example.ui.theme.UpiTeal
import com.example.ui.theme.UpiTealContainer
import com.example.ui.viewmodel.CashStatus
import com.example.ui.viewmodel.StreetSmartUiState

@Composable
fun CashRealityCheckScreen(
    uiState: StreetSmartUiState,
    onSaveCashRegister: (opening: Double, added: Double, withdrawn: Double, actualClosing: Double, weatherTag: String, notes: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language
    val summary = uiState.summary
    val register = uiState.currentDayCashRegister

    var openingText by remember { mutableStateOf(register?.openingCash?.toInt()?.toString() ?: "500") }
    var cashAddedText by remember { mutableStateOf(register?.cashAdded?.toInt()?.toString() ?: "0") }
    var cashWithdrawnText by remember { mutableStateOf(register?.cashWithdrawn?.toInt()?.toString() ?: "0") }
    var actualClosingText by remember { mutableStateOf(register?.actualClosingCash?.toInt()?.toString() ?: "1750") }
    var notesText by remember { mutableStateOf(register?.notes ?: "") }
    var savedSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(register) {
        if (register != null) {
            openingText = register.openingCash.toInt().toString()
            cashAddedText = register.cashAdded.toInt().toString()
            cashWithdrawnText = register.cashWithdrawn.toInt().toString()
            actualClosingText = register.actualClosingCash.toInt().toString()
            notesText = register.notes
        }
    }

    val opening = openingText.toDoubleOrNull() ?: 0.0
    val cashSales = summary.cashSales
    val cashAdded = cashAddedText.toDoubleOrNull() ?: 0.0
    val cashExpenses = summary.cashExpenses
    val cashWithdrawn = cashWithdrawnText.toDoubleOrNull() ?: 0.0

    // Exact required formula:
    // Expected closing cash = Opening cash + Cash sales + Cash added − Cash expenses − Cash withdrawn
    val expectedClosing = opening + cashSales + cashAdded - cashExpenses - cashWithdrawn
    val actualClosing = actualClosingText.toDoubleOrNull() ?: 0.0
    val cashDifference = actualClosing - expectedClosing

    val isShortage = cashDifference < -0.01
    val isSurplus = cashDifference > 0.01
    val isBalanced = !isShortage && !isSurplus

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("cash_reality_check_screen")
    ) {
        // Screen Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("reality_check_back_btn")) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = Strings.get("reality_check_status", lang),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Cash Drawer Reconciliation (${summary.selectedDate})",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Formula Banner Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = EmeraldContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = EmeraldDark,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = Strings.get("formula_title", lang),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnEmeraldContainer
                        )
                    )
                    Text(
                        text = Strings.get("formula_text", lang),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnEmeraldContainer,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Big Discrepancy Alert Box
        val (alertBg, alertText, alertBorder) = when {
            isShortage -> Triple(ShortageRedContainer, OnShortageRedContainer, ShortageRed)
            isSurplus -> Triple(UpiTealContainer, OnUpiTealContainer, UpiTeal)
            else -> Triple(SurplusGreenContainer, OnSurplusGreenContainer, SurplusGreen)
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = alertBg),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, alertBorder, RoundedCornerShape(16.dp))
                .testTag("discrepancy_result_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when {
                                isShortage -> Icons.Default.Warning
                                isSurplus -> Icons.Default.Info
                                else -> Icons.Default.CheckCircle
                            },
                            contentDescription = null,
                            tint = alertText,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = Strings.get("cash_difference", lang),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = alertText.copy(alpha = 0.85f)
                                )
                            )
                            Text(
                                text = when {
                                    isShortage -> "Shortage: ${formatRupee(cashDifference)}"
                                    isSurplus -> "Surplus: +${formatRupee(cashDifference)}"
                                    else -> "Balanced: ₹0"
                                },
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = alertText,
                                    fontSize = 26.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Practical explanation
                Text(
                    text = when {
                        isShortage -> {
                            "⚠️ ${formatRupee(kotlin.math.abs(cashDifference))} is unaccounted for! Please review the day's cash transactions:\n• Check if any cash expense was paid without recording (tea, snacks, packaging, bribes, local transport).\n• Check for customer change calculation mistakes or lost currency notes in the box."
                        }
                        isSurplus -> {
                            "ℹ️ Extra cash of ${formatRupee(cashDifference)} in drawer! Check for cash sales from early morning rush that were not recorded."
                        }
                        else -> {
                            "✅ Perfectly balanced! The ₹${actualClosing.toInt()} in your cash box matches your opening cash, sales, and expenses exactly."
                        }
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = alertText,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Detailed Step-by-Step Counting Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Daily Cash Equation Breakdown",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 1. Opening Cash (Editable)
                CashEquationInputField(
                    label = Strings.get("opening_cash", lang),
                    value = openingText,
                    onValueChange = { openingText = it },
                    prefixSymbol = "Start",
                    prefixColor = CashAmber,
                    testTag = "input_opening_cash"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 2. (+) Cash Sales (From Sales Records)
                CashEquationDisplayRow(
                    label = "(+) " + Strings.get("cash_sales", lang),
                    amount = cashSales,
                    description = "Recorded cash income from customers today",
                    color = EmeraldDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 3. (+) Cash Added (Editable)
                CashEquationInputField(
                    label = "(+) " + Strings.get("cash_added", lang),
                    value = cashAddedText,
                    onValueChange = { cashAddedText = it },
                    prefixSymbol = "+ Add",
                    prefixColor = EmeraldDark,
                    testTag = "input_cash_added"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 4. (-) Cash Expenses (From Expense Records)
                CashEquationDisplayRow(
                    label = "(-) " + Strings.get("cash_expenses_paid", lang),
                    amount = cashExpenses,
                    description = "Paid from cash drawer (stock, transport, bags)",
                    color = ShortageRed
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 5. (-) Cash Withdrawn (Editable)
                CashEquationInputField(
                    label = "(-) " + Strings.get("cash_withdrawn", lang),
                    value = cashWithdrawnText,
                    onValueChange = { cashWithdrawnText = it },
                    prefixSymbol = "- Draw",
                    prefixColor = ShortageRed,
                    testTag = "input_cash_withdrawn"
                )

                Spacer(modifier = Modifier.height(14.dp))
                androidx.compose.material3.HorizontalDivider(color = Color(0xFFE0E0E0))
                Spacer(modifier = Modifier.height(14.dp))

                // 6. (=) Expected Closing Cash (Auto-computed)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EmeraldContainer.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "(=) " + Strings.get("expected_closing_cash", lang),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldDark
                            )
                        )
                        Text(
                            text = "Formula result: ₹$opening + ₹$cashSales - ₹$cashExpenses",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 10.sp)
                        )
                    }
                    Text(
                        text = formatRupee(expectedClosing),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldDark
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 7. Actual Closing Cash Counted (Editable)
                CashEquationInputField(
                    label = Strings.get("actual_closing_cash", lang),
                    value = actualClosingText,
                    onValueChange = { actualClosingText = it },
                    prefixSymbol = "Count",
                    prefixColor = Color(0xFF1B4D3E),
                    testTag = "input_actual_closing"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                onSaveCashRegister(
                    opening,
                    cashAdded,
                    cashWithdrawn,
                    actualClosing,
                    summary.weatherTag,
                    notesText
                )
                savedSuccess = true
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_cash_register_btn")
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (savedSuccess) "Reconciliation Saved!" else "Save Cash Register",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun CashEquationInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    prefixSymbol: String,
    prefixColor: Color,
    testTag: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.all { ch -> ch.isDigit() || ch == '.' }) {
                    onValueChange(it)
                }
            },
            prefix = {
                Text(
                    text = "₹ ",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = prefixColor)
                )
            },
            trailingIcon = {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = prefixColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = prefixSymbol,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = prefixColor),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
private fun CashEquationDisplayRow(
    label: String,
    amount: Double,
    description: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.08f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = color)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                )
            }
            Text(
                text = formatRupee(amount),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = color)
            )
        }
    }
}
