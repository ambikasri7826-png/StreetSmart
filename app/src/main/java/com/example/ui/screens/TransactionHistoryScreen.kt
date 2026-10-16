package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionEntity
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.components.formatRupee
import com.example.ui.theme.EmeraldDark
import com.example.ui.viewmodel.StreetSmartUiState

@Composable
fun TransactionHistoryScreen(
    uiState: StreetSmartUiState,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language
    val allTransactions = uiState.allTransactions

    var selectedFilter by remember { mutableStateOf("ALL") } // ALL, INCOME, EXPENSE, CASH, UPI

    val filteredList = remember(allTransactions, selectedFilter) {
        when (selectedFilter) {
            "INCOME" -> allTransactions.filter { it.type == "INCOME" }
            "EXPENSE" -> allTransactions.filter { it.type == "EXPENSE" }
            "CASH" -> allTransactions.filter { it.paymentMethod == "CASH" }
            "UPI" -> allTransactions.filter { it.paymentMethod == "UPI" }
            else -> allTransactions
        }
    }

    val totalAmount = remember(filteredList) {
        filteredList.sumOf { if (it.type == "INCOME") it.amount else -it.amount }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("transaction_history_screen")
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("history_back_btn")) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = Strings.get("nav_history", lang),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${filteredList.size} records found across all dates",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                HistoryFilterChip(label = "All", isSelected = selectedFilter == "ALL", onClick = { selectedFilter = "ALL" })
            }
            item {
                HistoryFilterChip(label = "Sales (Income)", isSelected = selectedFilter == "INCOME", onClick = { selectedFilter = "INCOME" })
            }
            item {
                HistoryFilterChip(label = "Expenses", isSelected = selectedFilter == "EXPENSE", onClick = { selectedFilter = "EXPENSE" })
            }
            item {
                HistoryFilterChip(label = "Cash Box", isSelected = selectedFilter == "CASH", onClick = { selectedFilter = "CASH" })
            }
            item {
                HistoryFilterChip(label = "UPI / Bank", isSelected = selectedFilter == "UPI", onClick = { selectedFilter = "UPI" })
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Net Amount Summary Bar
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Net Filtered Flow:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = formatRupee(totalAmount),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (totalAmount >= 0) EmeraldDark else Color(0xFFBA1A1A)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // List
        if (filteredList.isEmpty()) {
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
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No transactions match this filter.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredList, key = { it.id }) { tx ->
                    ExactDesignTransactionRow(
                        transaction = tx,
                        language = lang,
                        onDelete = { onDeleteTransaction(tx) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) EmeraldDark else Color.White,
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD6DDD5)) else null,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color(0xFF2E3133),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
