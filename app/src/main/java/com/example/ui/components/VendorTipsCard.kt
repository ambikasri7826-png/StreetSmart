package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.theme.GradientRichRed
import com.example.ui.theme.MetricNegativeRed
import com.example.ui.viewmodel.CashStatus
import com.example.ui.viewmodel.DailyFinancialSummary

data class VendorTip(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color,
    val actionHint: String? = null
)

@Composable
fun VendorTipsCard(
    summary: DailyFinancialSummary,
    language: AppLanguage,
    onNavigateToStock: () -> Unit,
    onNavigateToRealityCheck: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToSavings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTamil = language == AppLanguage.TAMIL

    val tips by remember(summary, isTamil) {
        derivedStateOf {
            val list = mutableListOf<VendorTip>()

            // 1. Stock & Wastage Contextual Tip
            if (summary.totalWastageQty > 0 || summary.totalUnsoldStockQty > 0) {
                list.add(
                    VendorTip(
                        title = if (isTamil) "சரக்கு வாங்கும் முன் சரிபார்க்கவும்" else "Check stock before buying more",
                        description = if (isTamil) {
                            "இன்று ${summary.totalUnsoldStockQty.toInt()} அலகுகள் விற்பனையாகாமல் உள்ளன (${summary.totalWastageQty.toInt()} அலகுகள் சேதம்). நாளை காலை மொத்த சந்தையில் வாங்கும் அளவை 15% குறைக்கவும்."
                        } else {
                            "You have ${summary.totalUnsoldStockQty.toInt()} unsold units recorded today (${summary.totalWastageQty.toInt()} units spoiled). Inspect cart inventory before morning mandi purchases to prevent overstocking."
                        },
                        icon = Icons.Default.Inventory2,
                        accentColor = if (summary.totalWastageQty > 0) MetricNegativeRed else GradientRichRed,
                        actionHint = if (isTamil) "சரக்கு சரிபார்க்க →" else "Review Stock →"
                    )
                )
            } else {
                list.add(
                    VendorTip(
                        title = if (isTamil) "சரக்கு இருப்பு மேலாண்மை" else "Check your stock before buying more",
                        description = if (isTamil) {
                            "தினமும் மீதமுள்ள காய்கறி/சரக்குகளை கணக்கிடுங்கள். மாலை 6 மணிக்கு மேல் அழுகும் பொருட்களை தள்ளுபடி விலையில் விற்பது நஷ்டத்தை தடுக்கும்."
                        } else {
                            "Always check leftover perishable stock before visiting the morning mandi. Discounting remaining stock after 6 PM prevents total spoilage loss."
                        },
                        icon = Icons.Default.Inventory2,
                        accentColor = GradientRichRed,
                        actionHint = if (isTamil) "சரக்கு விவரம் →" else "View Stock →"
                    )
                )
            }

            // 2. Weather & Weekend/Festival Sales Patterns
            list.add(
                VendorTip(
                    title = if (isTamil) "விற்பனை முறைகளை மதிப்பாய்வு செய்யவும்" else "Review your weekend & sales patterns",
                    description = if (isTamil) {
                        "வானிலை மற்றும் சந்தை நாட்களின் விற்பனை அறிக்கையை பார்க்கவும். மழை நாட்களில் நடைபாதை வியாபாரம் 25% குறையலாம்; அதற்கேற்ப சரக்கு வாங்கவும்."
                    } else {
                        "Review your weekend and weather sales patterns in Reports. Rainy weather reduces walk-in footfall by ~25%, while festival market days generate 2x revenue."
                    },
                    icon = Icons.Default.WbSunny,
                    accentColor = Color(0xFFE65100),
                    actionHint = if (isTamil) "அறிக்கைகள் பார்க்க →" else "Compare Patterns →"
                )
            )

            // 3. Cash Reality Check Discrepancy Tip
            if (summary.cashStatus == CashStatus.SHORTAGE) {
                list.add(
                    VendorTip(
                        title = if (isTamil) "ரொக்க பற்றாக்குறை எச்சரிக்கை" else "Unrecorded cash leak detected",
                        description = if (isTamil) {
                            "பெட்டியில் ${formatRupee(kotlin.math.abs(summary.cashDifference))} குறைவாக உள்ளது! பதிவு செய்யாத டீ, சிற்றுண்டி, பிளாஸ்டிக் கவர் அல்லது சில்லறை தவறை உடனே சரிபார்க்கவும்."
                        } else {
                            "${formatRupee(kotlin.math.abs(summary.cashDifference))} is missing from your cash drawer. Record small cash payouts (chai, snacks, bags, transport) immediately before leaving."
                        },
                        icon = Icons.Default.Warning,
                        accentColor = MetricNegativeRed,
                        actionHint = if (isTamil) "சரிபார்க்க →" else "Reconcile Cash →"
                    )
                )
            }

            // 4. UPI vs Cash Tip
            if (summary.upiSales > 0 && summary.upiSales >= summary.cashSales) {
                list.add(
                    VendorTip(
                        title = if (isTamil) "UPI டிஜிட்டல் வசூல் அதிகரிப்பு" else "Digital UPI adoption is high",
                        description = if (isTamil) {
                            "விற்பனையில் ₹${summary.upiSales.toInt()} நேரடியாக வங்கிக்கு சென்றுள்ளது. ரொக்க வாடிக்கையாளர்களுக்கு போதிய சில்லறையை பெட்டியில் வைத்திருக்கவும்."
                        } else {
                            "Over 50% of your sales (₹${summary.upiSales.toInt()}) came via UPI QR directly into your bank. Keep sufficient cash coins in the drawer for cash buyers."
                        },
                        icon = Icons.Default.Payments,
                        accentColor = Color(0xFF006874),
                        actionHint = null
                    )
                )
            }

            // 5. Savings Strategy & Capital Protection
            if (summary.estimatedOperatingProfit > 0) {
                val safeSavings = (summary.estimatedOperatingProfit * 0.15).toInt().coerceAtLeast(50)
                list.add(
                    VendorTip(
                        title = if (isTamil) "முதன்மை முதலீட்டை பாதுகாக்கவும்" else "Protect tomorrow's working capital",
                        description = if (isTamil) {
                            "இன்றைய மதிப்பிடப்பட்ட லாபம் ₹${summary.estimatedOperatingProfit.toInt()}. இதில் ₹$safeSavings தொகையை சேமிப்பில் ஒதுக்கி, மீதியை நாளை காலை சரக்கு வாங்க பாதுகாக்கவும்."
                        } else {
                            "Today's estimated operating profit is ₹${summary.estimatedOperatingProfit.toInt()}. Deposit a safe ₹$safeSavings (15%) into your Savings Plan and reserve the rest for wholesale stock."
                        },
                        icon = Icons.Default.TrendingUp,
                        accentColor = GradientRichRed,
                        actionHint = if (isTamil) "சேமிப்புத் திட்டம் →" else "Open Savings Plan →"
                    )
                )
            }

            list
        }
    }

    var currentTipIndex by remember { mutableIntStateOf(0) }
    val currentTip = tips.getOrElse(currentTipIndex % tips.size) { tips.first() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("vendor_tips_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row with Next Tip cycle button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(GradientRichRed.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GradientRichRed,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Strings.get("vendor_tips_header", language),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = GradientRichRed
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F5F0),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { currentTipIndex = (currentTipIndex + 1) % tips.size }
                        .testTag("next_vendor_tip_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = GradientRichRed,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = Strings.get("tip_next", language),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GradientRichRed,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Animated Tip Content
            AnimatedContent(
                targetState = currentTip,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tip_transition"
            ) { tip ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(tip.accentColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tip.icon,
                            contentDescription = null,
                            tint = tip.accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tip.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1C1E)
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tip.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF43474E),
                                lineHeight = 16.sp,
                                fontSize = 12.sp
                            )
                        )

                        if (tip.actionHint != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = tip.actionHint,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = tip.accentColor
                                ),
                                modifier = Modifier
                                    .clickable {
                                        when {
                                            tip.title.contains("stock", ignoreCase = true) || tip.title.contains("சரக்கு", ignoreCase = true) -> onNavigateToStock()
                                            tip.title.contains("cash", ignoreCase = true) || tip.title.contains("ரொக்க", ignoreCase = true) -> onNavigateToRealityCheck()
                                            tip.title.contains("patterns", ignoreCase = true) || tip.title.contains("விற்பனை", ignoreCase = true) -> onNavigateToReports()
                                            else -> onNavigateToSavings()
                                        }
                                    }
                                    .padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
