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
import com.example.ui.theme.CashAmber
import com.example.ui.theme.CashAmberContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.OnCashAmberContainer
import com.example.ui.theme.OnUpiTealContainer
import com.example.ui.theme.ShortageRed
import com.example.ui.theme.UpiTeal
import com.example.ui.theme.UpiTealContainer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddIncomeScreen(
    currentLanguage: AppLanguage,
    selectedDate: String,
    onSaveIncome: (amount: Double, paymentMethod: String, category: String, notes: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var amountText by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("CASH") } // CASH or UPI
    var selectedCategory by remember { mutableStateOf("cat_tea_coffee") }
    var notesText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val incomePresets = listOf(
        "cat_tea_coffee",
        "cat_snacks",
        "cat_meals",
        "cat_vegetables",
        "cat_fruits",
        "cat_flowers",
        "cat_general_sale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("add_income_screen")
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("income_back_btn")) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = Strings.get("add_sale", currentLanguage),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Record money received from customers ($selectedDate)",
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
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
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
                                color = EmeraldDark
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
                        color = EmeraldDark
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldDark,
                        unfocusedBorderColor = Color(0xFFD6DDD5)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("income_amount_input")
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

        // Large Payment Mode Switcher (Cash vs UPI)
        Text(
            text = "Payment Mode (ரொக்கம் அல்லது UPI)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cash Button
            PaymentMethodCard(
                title = Strings.get("pay_cash", currentLanguage),
                subtitle = "Goes directly to cash box",
                icon = Icons.Default.Paid,
                isSelected = selectedPaymentMethod == "CASH",
                selectedBg = CashAmberContainer,
                selectedText = OnCashAmberContainer,
                onClick = { selectedPaymentMethod = "CASH" },
                modifier = Modifier
                    .weight(1f)
                    .testTag("income_method_cash")
            )

            // UPI Button
            PaymentMethodCard(
                title = Strings.get("pay_upi", currentLanguage),
                subtitle = "QR / GPay / PhonePe bank",
                icon = Icons.Default.QrCode,
                isSelected = selectedPaymentMethod == "UPI",
                selectedBg = UpiTealContainer,
                selectedText = OnUpiTealContainer,
                onClick = { selectedPaymentMethod = "UPI" },
                modifier = Modifier
                    .weight(1f)
                    .testTag("income_method_upi")
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Vendor Category Presets
        Text(
            text = "Category Presets (வகை)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            incomePresets.forEach { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) EmeraldDark else Color.White,
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
                .testTag("income_notes_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Save Button
        Button(
            onClick = {
                val amt = amountText.toDoubleOrNull()
                if (amt == null || amt <= 0.0) {
                    errorMessage = "Please enter a valid positive amount in ₹."
                } else {
                    onSaveIncome(amt, selectedPaymentMethod, selectedCategory, notesText.trim())
                    onBack()
                }
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_income_submit_btn")
        ) {
            Text(
                text = Strings.get("save_changes", currentLanguage) + " (+₹${amountText.ifBlank { "0" }})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun PaymentMethodCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    selectedBg: Color,
    selectedText: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) selectedText else Color(0xFFD6DDD5),
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) selectedBg else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) selectedText else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = selectedText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) selectedText else Color(0xFF1A1C1E)
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp,
                    color = if (isSelected) selectedText.copy(alpha = 0.8f) else Color.Gray
                )
            )
        }
    }
}
