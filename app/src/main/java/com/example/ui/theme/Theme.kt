package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val KalisDarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = KalisBlack,
    primaryContainer = ElectricBlue,
    onPrimaryContainer = TextPrimary,
    secondary = ElectricBlue,
    onSecondary = TextPrimary,
    secondaryContainer = KalisCardSurfaceElevated,
    onSecondaryContainer = ElectricCyan,
    tertiary = NeonPurple,
    onTertiary = TextPrimary,
    background = KalisBlack,
    onBackground = TextPrimary,
    surface = KalisDarkCharcoal,
    onSurface = TextPrimary,
    surfaceVariant = KalisCardSurface,
    onSurfaceVariant = TextSecondary,
    outline = KalisBorder,
    error = KalisError,
    onError = TextPrimary
)

@Composable
fun KalisEventsTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = KalisDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                if (Build.VERSION.SDK_INT < 35) {
                    @Suppress("DEPRECATION")
                    window.statusBarColor = KalisBlack.toArgb()
                    @Suppress("DEPRECATION")
                    window.navigationBarColor = KalisBlack.toArgb()
                }
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
