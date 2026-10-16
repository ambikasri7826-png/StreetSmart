package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.GradientBrightAmberRed
import com.example.ui.theme.GradientDarkBurgundy
import com.example.ui.theme.GradientMidCrimson
import com.example.ui.theme.GradientRichRed
import com.example.ui.theme.GradientVibrantOrangeRed
import com.example.ui.theme.MetricNegativeContainer
import com.example.ui.theme.MetricNegativeRed
import com.example.ui.theme.MetricPositiveContainer
import com.example.ui.theme.MetricPositiveGreen
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryMuted
import com.example.ui.viewmodel.CashStatus
import com.example.ui.viewmodel.DailyFinancialSummary
import java.text.NumberFormat
import java.util.Locale

fun formatRupee(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = 0
    formatter.minimumFractionDigits = 0
    val formatted = formatter.format(kotlin.math.abs(amount))
    return if (amount < 0) "-₹$formatted" else "₹$formatted"
}

@Composable
fun SummaryMetricCard(
    title: String,
    amount: Double,
    subtitle: String? = null,
    icon: ImageVector,
    containerColor: Color = CardSurfaceWhite,
    contentColor: Color = GradientRichRed,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextSecondaryMuted,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatRupee(amount),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark,
                    fontSize = 22.sp
                )
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondaryMuted,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Composable
fun CashRealityAlertBanner(
    summary: DailyFinancialSummary,
    language: AppLanguage,
    onNavigateToRealityCheck: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isShortage = summary.cashStatus == CashStatus.SHORTAGE
    val isSurplus = summary.cashStatus == CashStatus.SURPLUS
    val isBalanced = summary.cashStatus == CashStatus.BALANCED

    val (bgColor, textColor, icon) = when {
        isShortage -> Triple(Color(0xFF2B0B08), Color(0xFFFFDAD6), Icons.Default.Warning)
        isSurplus -> Triple(Color(0xFF0F2620), Color(0xFFA7F3D0), Icons.Default.Info)
        else -> Triple(Color(0xFF16221C), Color(0xFFD1FAE5), Icons.Default.CheckCircle)
    }

    val statusText = when {
        isShortage -> Strings.get("shortage_warning", language)
        isSurplus -> Strings.get("surplus_note", language)
        else -> Strings.get("balanced_note", language)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onNavigateToRealityCheck() }
            .testTag("cash_reality_banner"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(textColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = textColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = Strings.get("reality_check_status", language),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isShortage) GradientRichRed else Color.White.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (isBalanced) "Balanced ₹0" else formatRupee(summary.cashDifference),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Breakdown snapshot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = Strings.get("expected_closing_cash", language),
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.65f), fontSize = 11.sp)
                    )
                    Text(
                        text = formatRupee(summary.expectedClosingCash),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = Strings.get("actual_closing_cash", language),
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.65f), fontSize = 11.sp)
                    )
                    Text(
                        text = formatRupee(summary.actualClosingCash),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Review Reconciliation",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = GradientBrightAmberRed
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = GradientBrightAmberRed,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun EstimatedProfitCard(
    summary: DailyFinancialSummary,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            GradientDarkBurgundy,
            GradientMidCrimson,
            GradientRichRed
        )
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = Strings.get("operating_profit", language),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatRupee(summary.estimatedOperatingProfit),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 32.sp
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.18f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Strings.get("profit_disclaimer", language),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    )
                }
            }
        }
    }
}
