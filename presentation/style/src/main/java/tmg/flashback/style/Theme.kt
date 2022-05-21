package tmg.flashback.style

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jakewharton.threetenabp.AndroidThreeTen
import tmg.flashback.style.SupportedTheme.DEFAULT

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    var appTheme: SupportedTheme = DEFAULT

    var isLight: Boolean = true
        set(value) {
            if (field != value) {
                LocalColors.provides(when (value) {
                    true -> appTheme.lightColors
                    false -> appTheme.darkColors
                })
            }
            field = value
        }

    val typography: AppTypography = AppTypography()

    val dimensions: AppDimensions = AppDimensions()
}

val Dimensions = AppTheme.dimensions

@Composable
fun AppTheme(
    isLight: Boolean = !isSystemInDarkTheme(),
    theme: SupportedTheme = DEFAULT,
    content: @Composable () -> Unit
) {
    AppTheme.appTheme = theme
    AppTheme.isLight = isLight
    val colors = if (isLight) theme.lightColors else theme.darkColors
    CompositionLocalProvider(
        LocalColors provides colors
    ) {
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
    theme: SupportedTheme = DEFAULT,
    content: @Composable () -> Unit
) {
    AndroidThreeTen.init(LocalContext.current)
    return AppTheme(
        isLight = isLight,
        theme = theme,
        content = {
            content()
        }
    )
}

object FlashbackTheme {
    internal var colors = DEFAULT.lightColors

}

enum class SupportedTheme(
    val lightColors: AppColors,
    val darkColors: AppColors
) {
    DEFAULT(
        lightColors = lightColours,
        darkColors = darkColours
    )
//    MATERIAL_YOU(
//        lightColors = lightColours,
//        darkColors = darkColours
//    );
}