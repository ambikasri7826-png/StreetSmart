package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ExactDesignLightScheme = lightColorScheme(
  primary = GradientRichRed,
  onPrimary = Color.White,
  primaryContainer = GradientBrightAmberRed,
  onPrimaryContainer = Color.White,
  secondary = GradientVibrantOrangeRed,
  onSecondary = Color.White,
  secondaryContainer = MetricAmberContainer,
  onSecondaryContainer = Color(0xFF451A03),
  tertiary = MetricInfoTeal,
  onTertiary = Color.White,
  background = CanvasBackground,
  onBackground = TextPrimaryDark,
  surface = CardSurfaceWhite,
  onSurface = TextPrimaryDark,
  surfaceVariant = CardSurfaceVariant,
  onSurfaceVariant = TextSecondaryMuted,
  outline = Color(0xFFE2E5E9),
  error = MetricNegativeRed,
  errorContainer = MetricNegativeContainer,
  onError = Color.White,
  onErrorContainer = Color(0xFF7F1D1D)
)

private val ExactDesignDarkScheme = darkColorScheme(
  primary = GradientVibrantOrangeRed,
  onPrimary = Color.White,
  primaryContainer = GradientDarkBurgundy,
  onPrimaryContainer = Color(0xFFFFD7D0),
  secondary = GradientRichRed,
  onSecondary = Color.White,
  background = Color(0xFF0F1113),
  onBackground = Color(0xFFF3F4F6),
  surface = Color(0xFF181A1D),
  onSurface = Color(0xFFF3F4F6),
  surfaceVariant = Color(0xFF22252A),
  onSurfaceVariant = Color(0xFF9CA3AF),
  outline = Color(0xFF33383F),
  error = Color(0xFFF87171),
  errorContainer = Color(0xFF450A0A),
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) ExactDesignDarkScheme else ExactDesignLightScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
