package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Exact Rich Crimson & Burnt Blood-Orange Gradient Palette (from reference design)
val GradientDarkBurgundy = Color(0xFF280705)
val GradientMidCrimson = Color(0xFF6C130A)
val GradientRichRed = Color(0xFF981C0E)
val GradientVibrantOrangeRed = Color(0xFFD83818)
val GradientBrightAmberRed = Color(0xFFE84D1C)

// Accent & Glow Colors
val ChartLineGlow = Color(0xFFFFFFFF)
val ChartAreaGradientTop = Color(0x66FFFFFF)
val ChartAreaGradientBottom = Color(0x00FFFFFF)

// Canvas & Surfaces (Soft cool-stone grey backdrop matching screenshot)
val CanvasBackground = Color(0xFFECEFF2)
val CardSurfaceWhite = Color(0xFFFFFFFF)
val CardSurfaceVariant = Color(0xFFF7F8FA)
val DarkTooltipPill = Color(0xFF101214)

// Text tokens
val TextPrimaryDark = Color(0xFF111315)
val TextSecondaryMuted = Color(0xFF6B7280)
val TextTertiaryLight = Color(0xFF9CA3AF)

// Status & Indicators
val MetricPositiveGreen = Color(0xFF10B981)
val MetricPositiveContainer = Color(0xFFD1FAE5)
val MetricNegativeRed = Color(0xFFEF4444)
val MetricNegativeContainer = Color(0xFFFEE2E2)
val MetricInfoTeal = Color(0xFF0284C7)
val MetricInfoContainer = Color(0xFFE0F2FE)
val MetricAmber = Color(0xFFF59E0B)
val MetricAmberContainer = Color(0xFFFEF3C7)

// Translucent Glass Tokens
val GlassPillBackground = Color(0x38FFFFFF)
val GlassPillBorder = Color(0x4DFFFFFF)

// Theme Binding Aliases to guarantee all components inherit the exact design
val EmeraldDark = GradientRichRed
val EmeraldMedium = GradientVibrantOrangeRed
val EmeraldLight = GradientBrightAmberRed
val EmeraldContainer = Color(0xFFFFEEEE)
val OnEmeraldContainer = Color(0xFF4A0A06)

val CharcoalDark = TextPrimaryDark
val CharcoalMedium = Color(0xFF1E2124)
val CharcoalLight = TextSecondaryMuted
val SurfaceLight = CanvasBackground
val SurfaceCard = CardSurfaceWhite
val SurfaceCardElevated = CardSurfaceVariant
val OutlineMuted = Color(0xFFE2E5E9)

val ShortageRed = MetricNegativeRed
val ShortageRedContainer = MetricNegativeContainer
val OnShortageRedContainer = Color(0xFF550A0A)

val SurplusGreen = MetricPositiveGreen
val SurplusGreenContainer = MetricPositiveContainer
val OnSurplusGreenContainer = Color(0xFF064E3B)

val UpiTeal = MetricInfoTeal
val UpiTealContainer = MetricInfoContainer
val OnUpiTealContainer = Color(0xFF0C4A6E)

val CashAmber = MetricAmber
val CashAmberContainer = MetricAmberContainer
val OnCashAmberContainer = Color(0xFF78350F)
