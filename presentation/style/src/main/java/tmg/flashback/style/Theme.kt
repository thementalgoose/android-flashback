package tmg.flashback.style

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jakewharton.threetenabp.AndroidThreeTen

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    var appTheme: SupportedTheme = SupportedTheme.Default

    var isLight: Boolean = true

    val typography: AppTypography = AppTypography()

    val dimensions: AppDimensions = AppDimensions()
}

val Dimensions = AppTheme.dimensions

@Composable
fun AppTheme(
    isLight: Boolean = !isSystemInDarkTheme(),
    theme: SupportedTheme = AppTheme.appTheme,
    changeSystemUi: Boolean = true,
    content: @Composable () -> Unit
) {
    AppTheme.appTheme = theme
    AppTheme.isLight = isLight

    val colors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && theme == SupportedTheme.MaterialYou && isLight -> {
            SupportedTheme.MaterialYou.lightColors(LocalContext.current)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && theme == SupportedTheme.MaterialYou && !isLight -> {
            SupportedTheme.MaterialYou.darkColors(LocalContext.current)
        }
        (theme == SupportedTheme.Default || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) && isLight -> {
            SupportedTheme.Default.lightColors
        }
        (theme == SupportedTheme.Default || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) && !isLight -> {
            SupportedTheme.Default.darkColors
        }
        else -> SupportedTheme.Default.lightColors
    }

    LocalColors.provides(colors)

    CompositionLocalProvider(
        LocalColors provides colors
    ) {
        if (changeSystemUi) {
            val systemUiController = rememberSystemUiController()
            systemUiController.setNavigationBarColor(
                color = AppTheme.colors.backgroundNav
            )
        }

        MaterialTheme(
            colors = colors.appColors
        ) {
            content()
        }
    }
}

@Composable
fun AppThemePreview(
    isLight: Boolean = !isSystemInDarkTheme(),
    theme: SupportedTheme = SupportedTheme.Default,
    content: @Composable () -> Unit
) {
    AndroidThreeTen.init(LocalContext.current)
    return AppTheme(
        isLight = isLight,
        theme = theme,
        changeSystemUi = false,
        content = {
            content()
        }
    )
}

object FlashbackTheme {
    internal var colors = SupportedTheme.Default.lightColors
}

sealed class SupportedTheme{

    object Default: SupportedTheme() {
        val lightColors: AppColors = lightColours
        val darkColors: AppColors = darkColours
    }

    @RequiresApi(Build.VERSION_CODES.S)
    object MaterialYou: SupportedTheme() {
        fun lightColors(context: Context): AppColors {
            return lightColours.dynamic(dynamicLightColorScheme(context))
        }
        fun darkColors(context: Context): AppColors {
            return darkColours.dynamic(dynamicDarkColorScheme(context))
        }
    }

}