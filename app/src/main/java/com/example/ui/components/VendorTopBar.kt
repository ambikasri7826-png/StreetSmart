package com.example.ui.components

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StreetSmartRepository
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.theme.GradientDarkBurgundy
import com.example.ui.theme.GradientRichRed
import com.example.ui.theme.MetricNegativeRed

@Composable
fun VendorTopBar(
    currentLanguage: AppLanguage,
    selectedDate: String,
    isDemoDay: Boolean,
    onLanguageToggle: () -> Unit,
    onSelectDate: (String) -> Unit,
    onResetDemo: () -> Unit,
    onClearAll: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    var showDateMenu by remember { mutableStateOf(false) }

    Surface(
        color = GradientDarkBurgundy,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Title with Cart Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "StreetSmart Logo",
                            tint = GradientRichRed,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = Strings.get("app_title", currentLanguage),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 19.sp
                            )
                        )
                        Text(
                            text = if (isDemoDay) Strings.get("demo_badge", currentLanguage) else Strings.get("real_data_badge", currentLanguage),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isDemoDay) Color(0xFFFFD54F) else Color(0xFFA5D6A7),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Controls: Language Toggle & Menu Actions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // One-tap Language Switcher Button
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onLanguageToggle() }
                            .testTag("language_toggle_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Switch Language",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.ENGLISH) "தமிழ்" else "English",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Reset Demo Button
                    IconButton(
                        onClick = { showResetDialog = true },
                        modifier = Modifier.size(36.dp).testTag("reset_demo_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reset Demo Data",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Clear All Button
                    IconButton(
                        onClick = { showClearDialog = true },
                        modifier = Modifier.size(36.dp).testTag("clear_all_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear All Records",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Date Selector Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val today = StreetSmartRepository.getTodayDate()
                val yesterday = StreetSmartRepository.getYesterdayDate()

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Today Chip
                    DateChip(
                        label = Strings.get("filter_today", currentLanguage),
                        isSelected = selectedDate == today,
                        onClick = { onSelectDate(today) },
                        tag = "date_chip_today"
                    )
                    // Yesterday Chip
                    DateChip(
                        label = Strings.get("filter_yesterday", currentLanguage),
                        isSelected = selectedDate == yesterday,
                        onClick = { onSelectDate(yesterday) },
                        tag = "date_chip_yesterday"
                    )
                }

                // Date Picker Dropdown Anchor
                Box {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clickable { showDateMenu = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Select Date",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedDate,
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showDateMenu,
                        onDismissRequest = { showDateMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Today ($today)") },
                            onClick = {
                                onSelectDate(today)
                                showDateMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Yesterday ($yesterday)") },
                            onClick = {
                                onSelectDate(yesterday)
                                showDateMenu = false
                            }
                        )
                    }
                }
            }
        }
    }

    // Reset Demo Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(Strings.get("reset_demo", currentLanguage)) },
            text = {
                Text("Restore the standard sample business day (Opening cash ₹500, Cash sales ₹2,000, UPI ₹1,000, Cash expenses ₹700, Closing cash ₹1,750 with -₹50 shortage warning)?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetDemo()
                        showResetDialog = false
                    }
                ) {
                    Text("Reset", color = GradientRichRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Clear All Confirmation Dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(Strings.get("clear_all", currentLanguage)) },
            text = {
                Text("Are you sure you want to delete all transactions, cash registers, and stock items? This cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearAll()
                        showClearDialog = false
                    }
                ) {
                    Text("Delete All", color = MetricNegativeRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun DateChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.2f),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(tag)
    ) {
        Text(
            text = label,
            color = if (isSelected) GradientRichRed else Color.White,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
