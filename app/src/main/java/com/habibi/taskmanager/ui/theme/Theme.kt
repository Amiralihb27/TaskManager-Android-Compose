// Generated using MaterialKolor Builder version 1.2.1 (103)
// https://materialkolor.com/?color_seed=FFD0BCFF&color_primary=FFD0BCFF&color_secondary=FFFFC107&color_tertiary=FF81D4FA&dark_mode=true&package_name=com.example.app

package com.habibi.taskmanager.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.habibi.taskmanager.TaskManagerApp

private val lightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    inversePrimary = InversePrimaryLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    surfaceTint = SurfaceTintLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    scrim = ScrimLight,
    surfaceBright = SurfaceBrightLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceDim = SurfaceDimLight,
//    primaryFixed = PrimaryFixed,
//    primaryFixedDim = PrimaryFixedDim,
//    onPrimaryFixed = OnPrimaryFixed,
//    onPrimaryFixedVariant = OnPrimaryFixedVariant,
//    secondaryFixed = SecondaryFixed,
//    secondaryFixedDim = SecondaryFixedDim,
//    onSecondaryFixed = OnSecondaryFixed,
//    onSecondaryFixedVariant = OnSecondaryFixedVariant,
//    tertiaryFixed = TertiaryFixed,
//    tertiaryFixedDim = TertiaryFixedDim,
//    onTertiaryFixed = OnTertiaryFixed,
//    onTertiaryFixedVariant = OnTertiaryFixedVariant,
)

private val darkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    inversePrimary = InversePrimaryDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    surfaceTint = SurfaceTintDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    scrim = ScrimDark,
    surfaceBright = SurfaceBrightDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceDim = SurfaceDimDark,
//    primaryFixed = PrimaryFixed,
//    primaryFixedDim = PrimaryFixedDim,
//    onPrimaryFixed = OnPrimaryFixed,
//    onPrimaryFixedVariant = OnPrimaryFixedVariant,
//    secondaryFixed = SecondaryFixed,
//    secondaryFixedDim = SecondaryFixedDim,
//    onSecondaryFixed = OnSecondaryFixed,
//    onSecondaryFixedVariant = OnSecondaryFixedVariant,
//    tertiaryFixed = TertiaryFixed,
//    tertiaryFixedDim = TertiaryFixedDim,
//    onTertiaryFixed = OnTertiaryFixed,
//    onTertiaryFixedVariant = OnTertiaryFixedVariant,
)

@Composable
fun TaskManagerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
    val context = LocalContext.current
    // Pulls colors from the phone's wallpaper / theme (Android 12+)
    val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Use dynamic colors on Android 12+
        if (darkTheme) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    } else {
        // Fallback colors for Android < 12
        if (darkTheme) darkColorScheme()
        else lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,

    )
}