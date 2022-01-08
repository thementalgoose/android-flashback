package tmg.flashback.style

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import tmg.flashback.style.SupportedTheme.DEFAULT

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val dimensions: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current
}

@Composable
fun AppTheme(
    isLight: Boolean = !isSystemInDarkTheme(),
    theme: SupportedTheme = DEFAULT,
    typography: AppTypography = AppTheme.typography,
    dimensions: AppDimensions = AppTheme.dimensions,
    content: @Composable () -> Unit
) {
    val colors = if (isLight) theme.lightColors else theme.darkColors
    val rememberedColors = remember { colors.copy() }.apply { updateFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalDimensions provides dimensions,
        LocalTypography provides typography
    ) {
        MaterialTheme(
            colors = colors.materialColours,
            content = content
        )
    }
}

@Composable
fun AppThemePreview(
    isLight: Boolean = true,
    content: @Composable () -> Unit
) {
    return AppTheme(
        isLight = isLight,
        content = {
            Box(
                modifier = Modifier
                    .background(if (isLight) Color.White else Color.Black)
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        top = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium
                    )
            ) {
                content()
            }
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
    );
}