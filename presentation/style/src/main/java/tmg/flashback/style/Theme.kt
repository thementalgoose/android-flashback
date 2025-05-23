package tmg.flashback.style

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    var appTheme: SupportedTheme = SupportedTheme.Default

    var isLight: Boolean = true

    val typography: AppTypography = AppTypography()

    val dimens: AppDimensions = AppDimensions()
}

val Dimens = AppTheme.dimens

@Composable
fun AppTheme(
    isLight: Boolean = !isSystemInDarkTheme(),
    theme: SupportedTheme = AppTheme.appTheme,
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
        MaterialTheme(
            colorScheme = colors.appColors
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
    return AppTheme(
        isLight = isLight,
        theme = theme,
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

    object MaterialYou: SupportedTheme() {
        @RequiresApi(Build.VERSION_CODES.S)
        fun lightColors(context: Context): AppColors {
            return lightColours.dynamic(dynamicLightColorScheme(context), isLightMode = true)
        }

        @RequiresApi(Build.VERSION_CODES.S)
        fun darkColors(context: Context): AppColors {
            return darkColours.dynamic(dynamicDarkColorScheme(context), isLightMode = false)
        }
    }

}