package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.theme.DarkTooltipPill
import com.example.ui.theme.MetricPositiveGreen
import com.example.ui.viewmodel.DailyFinancialSummary

@Composable
fun ExactHeroHeaderCard(
    summary: DailyFinancialSummary,
    language: AppLanguage,
    onOpenDatePicker: () -> Unit,
    onToggleMetric: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 0 = Estimated Operating Profit, 1 = Total Sales
    var activeMetricMode by remember { mutableIntStateOf(0) }

    val displayAmount = if (activeMetricMode == 0) summary.estimatedOperatingProfit else summary.totalSales
    val displayTitle = if (activeMetricMode == 0) {
        Strings.get("operating_profit", language)
    } else {
        Strings.get("total_sales", language)
    }

    val wholePart = formatRupee(displayAmount)

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF260604),
            Color(0xFF551008),
            Color(0xFF8B1A0D),
            Color(0xFFC73016)
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("exact_hero_header_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
                .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Row: Translucent Percentage Pill on Left + Calendar Icon on Right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Glassmorphic pill chip: "↗ 1.34% this month" (clickable to toggle profit vs sales)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.18f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.28f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                activeMetricMode = if (activeMetricMode == 0) 1 else 0
                                onToggleMetric()
                            }
                            .testTag("hero_trend_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NorthEast,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (activeMetricMode == 0) "↗ 1.34% this month" else "Total Revenue View",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    // Calendar icon button inside rounded translucent square
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.18f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.28f)),
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenDatePicker() }
                            .testTag("hero_calendar_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Select Date",
                                tint = Color.White,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Large Main Balance: $2,463.48 style
                Column {
                    Text(
                        text = wholePart,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 38.sp,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Text(
                        text = "$displayTitle • ${summary.selectedDate}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Continuous glowing wave chart with black tooltip pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(145.dp)
                ) {
                    // Canvas with glowing curve and area fill
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 38.dp, bottom = 22.dp)
                    ) {
                        val w = size.width
                        val h = size.height

                        // 6 Normalized Control Points (Jan, Feb, Mar, Apr, May, Jun)
                        // Corresponding to the exact undulating curve from image.png
                        val points = listOf(
                            Offset(0f, h * 0.75f),
                            Offset(w * 0.18f, h * 0.65f),
                            Offset(w * 0.36f, h * 0.72f),
                            Offset(w * 0.54f, h * 0.50f),
                            Offset(w * 0.72f, h * 0.45f), // Apr peak (anchor point)
                            Offset(w * 0.88f, h * 0.62f),
                            Offset(w, h * 0.38f)
                        )

                        // Smooth Cubic Bezier Path
                        val strokePath = Path().apply {
                            moveTo(points[0].x, points[0].y)
                            for (i in 0 until points.size - 1) {
                                val p0 = points[i]
                                val p1 = points[i + 1]
                                val cx = (p0.x + p1.x) / 2
                                cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                            }
                        }

                        // Gradient Area Fill under Curve
                        val fillPath = Path().apply {
                            addPath(strokePath)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.22f),
                                    Color.White.copy(alpha = 0.0f)
                                ),
                                startY = 0f,
                                endY = h
                            )
                        )

                        // Glowing White Stroke
                        drawPath(
                            path = strokePath,
                            color = Color.White,
                            style = Stroke(width = 2.8.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Anchor Dot at Apr peak (index 4)
                        val anchor = points[4]
                        // Outer subtle glow
                        drawCircle(
                            color = Color.White.copy(alpha = 0.4f),
                            radius = 7.dp.toPx(),
                            center = anchor
                        )
                        // Inner dot
                        drawCircle(
                            color = Color.White,
                            radius = 3.5.dp.toPx(),
                            center = anchor
                        )
                    }

                    // Floating Dark Tooltip Pill: "$854.32  ↗ 1.32%"
                    // Positioned above April peak (around 70% width)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 40.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = DarkTooltipPill,
                            shadowElevation = 6.dp,
                            modifier = Modifier.testTag("chart_tooltip_pill")
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "₹854.32",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 13.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White.copy(alpha = 0.15f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.NorthEast,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(9.dp)
                                            )
                                            Text(
                                                text = " 1.32%",
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "for April 2026",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF9E9E9E),
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }

                    // Month Labels at the bottom matching reference: Jan  Feb  Mar  Apr  May  Jun
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun").forEach { month ->
                            Text(
                                text = month,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White.copy(alpha = 0.65f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
