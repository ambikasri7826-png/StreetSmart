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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.QrCode
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
import com.example.ui.theme.CashAmberContainer
import com.example.ui.theme.OnCashAmberContainer
import com.example.ui.theme.OnShortageRedContainer
import com.example.ui.theme.OnUpiTealContainer
import com.example.ui.theme.ShortageRed
import com.example.ui.theme.ShortageRedContainer
import com.example.ui.theme.UpiTealContainer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddExpenseScreen(
    currentLanguage: AppLanguage,
    selectedDate: String,
    onSaveExpense: (amount: Double, paymentMethod: String, category: String, notes: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var amountText by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("CASH") } // CASH (draws from box) or UPI (online)
    var selectedCategory by remember { mutableStateOf("cat_stock_purchase") }
    var notesText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val expensePresets = listOf(
        "cat_stock_purchase",
        "cat_transport",
        "cat_packaging",
        "cat_rent_bribe",
        "cat_electricity_ice",
        "cat_gas_fuel",
        "cat_oil_groceries",
        "cat_other_expense"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("add_expense_screen")
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("expense_back_btn")) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = Strings.get("add_expense", currentLanguage),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Record costs paid for business operations ($selectedDate)",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big Amount Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = Strings.get("amount_hint", currentLanguage),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = ShortageRed)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        if (it.all { ch -> ch.isDigit() || ch == '.' }) {
                            amountText = it
                            errorMessage = null
                        }
                    },
                    prefix = {
                        Text(
                            text = "₹ ",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = ShortageRed
                            )
                        )
                    },
                    placeholder = {
                        Text(
                            text = "0",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = ShortageRed
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ShortageRed,
                        unfocusedBorderColor = Color(0xFFD6DDD5)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("expense_amount_input")
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage!!,
                        style = MaterialTheme.typography.bodySmall.copy(color = ShortageRed)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Source Card (Cash from Drawer vs Bank UPI)
        Text(
            text = "Payment Source (எங்கிருந்து பணம் கொடுக்கப்பட்டது?)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentMethodCard(
                title = Strings.get("pay_cash", currentLanguage),
                subtitle = "Paid from cash drawer box",
                icon = Icons.Default.Paid,
                isSelected = selectedPaymentMethod == "CASH",
                selectedBg = ShortageRedContainer,
                selectedText = OnShortageRedContainer,
                onClick = { selectedPaymentMethod = "CASH" },
                modifier = Modifier
                    .weight(1f)
                    .testTag("expense_method_cash")
            )

            PaymentMethodCard(
                title = Strings.get("pay_upi", currentLanguage),
                subtitle = "Paid online via UPI bank app",
                icon = Icons.Default.QrCode,
                isSelected = selectedPaymentMethod == "UPI",
                selectedBg = UpiTealContainer,
                selectedText = OnUpiTealContainer,
                onClick = { selectedPaymentMethod = "UPI" },
                modifier = Modifier
                    .weight(1f)
                    .testTag("expense_method_upi")
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Vendor Expense Categories
        Text(
            text = "Expense Category (செலவு வகை)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            expensePresets.forEach { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) ShortageRed else Color.White,
                    border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD6DDD5)) else null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { selectedCategory = cat }
                        .testTag("preset_${cat}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = Strings.get(cat, currentLanguage),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF2E3133)
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Optional Notes
        OutlinedTextField(
            value = notesText,
            onValueChange = { notesText = it },
            label = { Text(Strings.get("notes_hint", currentLanguage)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("expense_notes_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Save Button
        Button(
            onClick = {
                val amt = amountText.toDoubleOrNull()
                if (amt == null || amt <= 0.0) {
                    errorMessage = "Please enter a valid positive expense amount in ₹."
                } else {
                    onSaveExpense(amt, selectedPaymentMethod, selectedCategory, notesText.trim())
                    onBack()
                }
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ShortageRed),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_expense_submit_btn")
        ) {
            Text(
                text = Strings.get("save_changes", currentLanguage) + " (-₹${amountText.ifBlank { "0" }})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
