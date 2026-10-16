package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.TransactionEntity
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.components.CashRealityAlertBanner
import com.example.ui.components.ExactHeroHeaderCard
import com.example.ui.components.SummaryMetricCard
import com.example.ui.components.VendorTipsCard
import com.example.ui.components.formatRupee
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.GradientBrightAmberRed
import com.example.ui.theme.GradientDarkBurgundy
import com.example.ui.theme.GradientMidCrimson
import com.example.ui.theme.GradientRichRed
import com.example.ui.theme.GradientVibrantOrangeRed
import com.example.ui.theme.MetricNegativeContainer
import com.example.ui.theme.MetricNegativeRed
import com.example.ui.theme.MetricPositiveGreen
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryMuted
import com.example.ui.viewmodel.StreetSmartUiState

@Composable
fun DashboardScreen(
    uiState: StreetSmartUiState,
    onNavigateToAddIncome: () -> Unit,
    onNavigateToAddExpense: () -> Unit,
    onNavigateToRealityCheck: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToSavings: () -> Unit,
    onNavigateToReports: () -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = uiState.language
    val summary = uiState.summary
    var showModernToolsHero by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBackground)
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Exact Reference Design Hero Gradient Card with Glowing Wave Chart & Tooltip
        item {
            ExactHeroHeaderCard(
                summary = summary,
                language = lang,
                onOpenDatePicker = { onNavigateToReports() },
                onToggleMetric = { }
            )
        }

        // 2. Feature 3: Cash Reality Check Alert Banner
        item {
            CashRealityAlertBanner(
                summary = summary,
                language = lang,
                onNavigateToRealityCheck = onNavigateToRealityCheck
            )
        }

        // 3. Dynamic Business Advice (Vendor Tips Card)
        item {
            VendorTipsCard(
                summary = summary,
                language = lang,
                onNavigateToStock = onNavigateToStock,
                onNavigateToRealityCheck = onNavigateToRealityCheck,
                onNavigateToReports = onNavigateToReports,
                onNavigateToSavings = onNavigateToSavings
            )
        }

        // 4. "Modern Tools for Modern Finance" Featured 3D Visual Card (Exact style from left of image.png)
        if (showModernToolsHero) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("modern_tools_hero_card"),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = GradientDarkBurgundy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_vendor_hero),
                            contentDescription = "Modern Tools for Modern Finance 3D Asset",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Gradient Scrim for readable contrast
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0xBB180403),
                                            Color(0xEE140302)
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.22f)
                                ) {
                                    Text(
                                        text = "STREETSMART 3.0",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 9.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { showModernToolsHero = false },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Dismiss Banner",
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Modern Tools for\nModern Street Finance",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = Color.White,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .clickable { onNavigateToSavings() }
                                    ) {
                                        Text(
                                            text = "Savings Plan",
                                            color = Color(0xFF1E0402),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                            .clickable { onNavigateToSavings() },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = null,
                                            tint = Color(0xFF1E0402),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Quick Action Action Strip (Large Tappable Buttons)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onNavigateToAddIncome,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GradientRichRed),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("quick_add_income_btn")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Strings.get("add_sale", lang),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = onNavigateToAddExpense,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2F36)),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("quick_add_expense_btn")
                ) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Strings.get("add_expense", lang),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // 6. 2x2 Metric Grid: Cash Sales, UPI Sales, Total Expenses, Stock Wastage
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryMetricCard(
                        title = Strings.get("cash_sales", lang),
                        amount = summary.cashSales,
                        subtitle = "Paper cash in drawer",
                        icon = Icons.Default.Paid,
                        containerColor = CardSurfaceWhite,
                        contentColor = GradientRichRed,
                        modifier = Modifier.weight(1f).testTag("card_cash_sales")
                    )

                    SummaryMetricCard(
                        title = Strings.get("upi_sales", lang),
                        amount = summary.upiSales,
                        subtitle = "QR / Direct bank",
                        icon = Icons.Default.QrCode,
                        containerColor = CardSurfaceWhite,
                        contentColor = Color(0xFF0284C7),
                        modifier = Modifier.weight(1f).testTag("card_upi_sales")
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryMetricCard(
                        title = Strings.get("total_expenses", lang),
                        amount = summary.totalExpenses,
                        subtitle = "Cash paid: ₹${summary.cashExpenses.toInt()}",
                        icon = Icons.Default.Receipt,
                        containerColor = CardSurfaceWhite,
                        contentColor = Color(0xFF374151),
                        modifier = Modifier.weight(1f).testTag("card_total_expenses")
                    )

                    SummaryMetricCard(
                        title = Strings.get("unsold_stock_count", lang),
                        amount = summary.totalUnsoldStockQty,
                        subtitle = if (summary.totalWastageQty > 0) "⚠️ ${summary.totalWastageQty} wasted" else "All items tracked",
                        icon = Icons.Default.Inventory2,
                        containerColor = CardSurfaceWhite,
                        contentColor = if (summary.totalWastageQty > 0) MetricNegativeRed else Color(0xFF059669),
                        modifier = Modifier.weight(1f).testTag("card_unsold_stock")
                    )
                }
            }
        }

        // 7. Recent Transactions Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions (${uiState.currentDayTransactions.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = summary.selectedDate,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryMuted)
                )
            }
        }

        // 8. Transactions List — Exact Design of Bottom-Right of Reference Screenshot
        if (uiState.currentDayTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite)
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
                            text = Strings.get("no_data", lang),
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryMuted),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(uiState.currentDayTransactions, key = { it.id }) { tx ->
                ExactDesignTransactionRow(
                    transaction = tx,
                    language = lang,
                    onDelete = { onDeleteTransaction(tx) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

/**
 * Exact Transaction Row styled to replicate the bottom-right list cards of image.png:
 * - Rounded pill card (shape = RoundedCornerShape(22.dp))
 * - Circular tinted icon badge on the left
 * - Clean two-line title & subtitle
 * - Crisp right-aligned amounts: -$59.99 / +$187.50
 */
@Composable
fun ExactDesignTransactionRow(
    transaction: TransactionEntity,
    language: AppLanguage,
    onDelete: () -> Unit
) {
    val isIncome = transaction.type == "INCOME"
    val isCash = transaction.paymentMethod == "CASH"

    // Icon & Color Tinting matching the screenshot's soft circular badges
    val (badgeBg, iconTint, iconVector) = when {
        transaction.category.contains("meals") || transaction.category.contains("tea") ->
            Triple(Color(0xFFFFEEEE), GradientRichRed, Icons.Default.LocalCafe)
        transaction.paymentMethod == "UPI" ->
            Triple(Color(0xFFE0F2FE), Color(0xFF0284C7), Icons.Default.QrCode)
        transaction.category.contains("stock") ->
            Triple(Color(0xFFFEF3C7), Color(0xFFD97706), Icons.Default.ShoppingBag)
        transaction.category.contains("transport") ->
            Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), Icons.Default.Paid)
        isIncome ->
            Triple(Color(0xFFECFDF5), MetricPositiveGreen, Icons.Default.Paid)
        else ->
            Triple(Color(0xFFFEE2E2), MetricNegativeRed, Icons.Default.Receipt)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tx_item_${transaction.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Soft tinted circle badge
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(badgeBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = Strings.get(transaction.category, language),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (transaction.notes.isNotBlank()) {
                            "${transaction.notes} • ${if (isCash) "Cash" else "UPI"}"
                        } else {
                            "Today, ${if (isCash) "Cash Box" else "UPI Bank"}"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondaryMuted,
                            fontSize = 12.sp
                        ),
                        maxLines = 1
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${if (isIncome) "+" else "-"}₹${transaction.amount.toInt()}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp).testTag("delete_tx_${transaction.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Transaction",
                        tint = Color.LightGray,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}
