package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.StockItemEntity
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.components.formatRupee
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.theme.OnShortageRedContainer
import com.example.ui.theme.ShortageRed
import com.example.ui.theme.ShortageRedContainer
import com.example.ui.viewmodel.StreetSmartUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockTrackerScreen(
    uiState: StreetSmartUiState,
    onAddStockItem: (name: String, unit: String, purchased: Double, sold: Double, remaining: Double, cost: Double) -> Unit,
    onDeleteStockItem: (StockItemEntity) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language
    val stockItems = uiState.currentDayStockItems
    val summary = uiState.summary

    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("stock_tracker_screen")
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("stock_back_btn")) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = Strings.get("nav_stock", lang),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Track purchased vs sold inventory & wastage (${summary.selectedDate})",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                modifier = Modifier.testTag("add_stock_item_open_btn")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Item", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Daily Wastage Alert Banner
        if (summary.totalWastageQty > 0) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ShortageRedContainer),
                modifier = Modifier.fillMaxWidth().testTag("stock_wastage_banner")
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = ShortageRed,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = Strings.get("wastage_warning", lang),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnShortageRedContainer
                            )
                        )
                        Text(
                            text = "Total spoilage: ${summary.totalWastageQty} units lost (Estimated cost: ${formatRupee(summary.totalWastageCost)}). Reduce purchase quantity by 10-15% tomorrow.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = OnShortageRedContainer,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Restocking Advice Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = EmeraldContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = EmeraldDark,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Vendor Purchase Tip",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnEmeraldContainer
                        )
                    )
                    Text(
                        text = "Unsold stock remaining can be carried forward or used for morning sales. Always discount perishable items by 6 PM to prevent zero-value wastage.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnEmeraldContainer,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Stock Items List
        Text(
            text = "Tracked Items (${stockItems.size})",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (stockItems.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No stock items recorded today. Tap 'Add Item' to track tomatoes, milk, chai ingredients, or vegetables.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(stockItems, key = { it.id }) { item ->
                    StockItemCard(
                        item = item,
                        language = lang,
                        onDelete = { onDeleteStockItem(item) }
                    )
                }
            }
        }
    }

    // Add Stock Item Modal Dialog
    if (showAddDialog) {
        AddStockItemDialog(
            language = lang,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, unit, purchased, sold, remaining, cost ->
                onAddStockItem(name, unit, purchased, sold, remaining, cost)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun StockItemCard(
    item: StockItemEntity,
    language: AppLanguage,
    onDelete: () -> Unit
) {
    val hasWastage = item.wastageQuantity > 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("stock_item_${item.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.itemName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Cost: ₹${item.purchaseCostPerUnit.toInt()}/${item.unit}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Item",
                        tint = Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4-Column Metric Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F9F6), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StockColumnItem(label = "Purchased", value = "${item.quantityPurchased} ${item.unit}", color = Color.Black)
                StockColumnItem(label = "Sold", value = "${item.quantitySold} ${item.unit}", color = EmeraldDark)
                StockColumnItem(label = "Remaining", value = "${item.quantityRemaining} ${item.unit}", color = Color(0xFF1B4D3E))
                StockColumnItem(
                    label = "Unsold Loss",
                    value = if (hasWastage) "⚠️ ${item.wastageQuantity} ${item.unit}" else "0",
                    color = if (hasWastage) ShortageRed else Color.Gray
                )
            }

            if (hasWastage) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⚠️ ${item.wastageQuantity} ${item.unit} damaged/spoiled (Loss: ${formatRupee(item.wastageCost)})",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = ShortageRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun StockColumnItem(label: String, value: String, color: Color) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontSize = 10.sp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = color)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStockItemDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onConfirm: (name: String, unit: String, purchased: Double, sold: Double, remaining: Double, cost: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }
    var purchasedText by remember { mutableStateOf("") }
    var soldText by remember { mutableStateOf("") }
    var remainingText by remember { mutableStateOf("") }
    var costText by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val units = listOf("kg", "pieces", "bunches", "cups", "plates")
    var unitMenuExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Daily Stock Item", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name (e.g. Tomatoes, Milk)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("dialog_stock_name")
                )

                // Unit Selector
                ExposedDropdownMenuBox(
                    expanded = unitMenuExpanded,
                    onExpandedChange = { unitMenuExpanded = !unitMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Unit of Measure") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitMenuExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = unitMenuExpanded,
                        onDismissRequest = { unitMenuExpanded = false }
                    ) {
                        units.forEach { u ->
                            DropdownMenuItem(
                                text = { Text(u) },
                                onClick = {
                                    unit = u
                                    unitMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = purchasedText,
                        onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) purchasedText = it },
                        label = { Text("Purchased") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("dialog_stock_purchased")
                    )
                    OutlinedTextField(
                        value = soldText,
                        onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) soldText = it },
                        label = { Text("Sold") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("dialog_stock_sold")
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = remainingText,
                        onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) remainingText = it },
                        label = { Text("Remaining") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("dialog_stock_remaining")
                    )
                    OutlinedTextField(
                        value = costText,
                        onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) costText = it },
                        label = { Text("Cost ₹/unit") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("dialog_stock_cost")
                    )
                }

                if (errorMsg != null) {
                    Text(text = errorMsg!!, color = ShortageRed, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = purchasedText.toDoubleOrNull() ?: 0.0
                    val s = soldText.toDoubleOrNull() ?: 0.0
                    val r = remainingText.toDoubleOrNull() ?: 0.0
                    val c = costText.toDoubleOrNull() ?: 0.0
                    if (name.isBlank()) {
                        errorMsg = "Please enter an item name."
                    } else if (p <= 0.0) {
                        errorMsg = "Please enter purchased quantity."
                    } else {
                        onConfirm(name.trim(), unit, p, s, r, c)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                modifier = Modifier.testTag("dialog_stock_save_btn")
            ) {
                Text("Save Item")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
