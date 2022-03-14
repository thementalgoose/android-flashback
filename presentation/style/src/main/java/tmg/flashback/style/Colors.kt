package tmg.flashback.style

import androidx.compose.material.Colors
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalColors = staticCompositionLocalOf { lightColours }

class AppColors(
    // Theme
    val primary: Color,
    val primaryDark: Color,
    val accent: Color,
    val error: Color,
    // Content
    val contentPrimary: Color,
    val contentSecondary: Color,
    val contentTertiary: Color,
    val contentPrimaryInverse: Color,
    val contentSecondaryInverse: Color,
    val contentTertiaryInverse: Color,
    // Background
    val backgroundContainer: Color,
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,
    val backgroundPrimaryInverse: Color,
    val backgroundSecondaryInverse: Color,
    val backgroundTertiaryInverse: Color,
    val backgroundNav: Color,
    // F1
    val f1Podium1: Color,
    val f1Podium2: Color,
    val f1Podium3: Color,
    val f1DeltaPositive: Color,
    val f1DeltaNeutral: Color = contentSecondary,
    val f1DeltaNegative: Color,
    val f1ResultsFull: Color,
    val f1ResultsNeutral: Color = contentSecondary,
    val f1ResultsPartial: Color,
    val f1FastestSector: Color,
    val f1FavouriteSeason: Color,
    val f1Championship: Color,
    val f1PipeColor: Color = contentTertiary,
    // RSS
    val rssAdd: Color,
    val rssRemove: Color,
    val rssNewsBar: Color = backgroundSecondary,
    val isLight: Boolean
) {

    val appColors: Colors = Colors(
        primary = primary,
        primaryVariant = primary,
        secondary = accent,
        secondaryVariant = accent,
        background = backgroundPrimary,
        surface = backgroundSecondary,
        error = Color(0xFFCC0000),
        onPrimary = Color(0xFFF8F8F8),
        onSecondary = Color(0xFFF8F8F8),
        onBackground = contentPrimary,
        onSurface = contentSecondary,
        onError = Color(0xFFF8F8F8),
        isLight = isLight
    )
}

internal val textDark: Color = Color(0xFF181818)
internal val textLight: Color = Color(0xFFF8F8F8)

val lightColours = AppColors(
    primary = Color(0xFF0274D1),
    primaryDark = Color(0xFF0274D1),
    accent = Color(0xFF00E2E4),
    error = Color(0xFFF44336),
    contentPrimary = Color(0xFF181818),
    contentSecondary = Color(0xFF383838),
    contentTertiary = Color(0xFF585858),
    contentPrimaryInverse = Color(0xFFF8F8F8),
    contentSecondaryInverse = Color(0xFFF2F2F2),
    contentTertiaryInverse = Color(0xFFEEEEEE),
    backgroundContainer = Color(0xFFF4F4F4),
    backgroundPrimary = Color(0xFFF8F8F8),
    backgroundSecondary = Color(0xFFF2F2F2),
    backgroundTertiary = Color(0xFFDDDDDD),
    backgroundPrimaryInverse = Color(0xFF181818),
    backgroundSecondaryInverse = Color(0xFF383838),
    backgroundTertiaryInverse = Color(0xFF484848),
    backgroundNav = Color(0xFFFCFCFC),
    f1Podium1 = Color(0xFFD3BC4D),
    f1Podium2 = Color(0xFFC2C2C2),
    f1Podium3 = Color(0xFFD29342),
    f1DeltaPositive = Color(0xFFF44336),
//    f1DeltaNeutral = ,
    f1DeltaNegative = Color(0xFF4CAF50),
    f1ResultsFull = Color(0xFF4CAF50),
//    f1ResultsNeutral = ,
    f1ResultsPartial = Color(0xFFFFA000),
    f1FastestSector = Color(0xFF673AB7),
    f1FavouriteSeason = Color(0xFFE6CA4F),
    f1Championship = Color(0xFFE6CA4F),
//    f1PipeColor = ,
    rssAdd = Color(0xFF4CAF50),
    rssRemove = Color(0xFFF44336),
//    rssNewsBar =
    isLight = true
)

val darkColours = AppColors(
    primary = Color(0xFF00E2E4),
    primaryDark = Color(0xFF00E2E4),
    accent = Color(0xFF0274D1),
    error = Color(0xFFF44336),
    contentPrimary = Color(0xFFF8F8F8),
    contentSecondary = Color(0xFFF2F2F2),
    contentTertiary = Color(0xFFDDDDDD),
    contentPrimaryInverse = Color(0xFF181818),
    contentSecondaryInverse = Color(0xFF383838),
    contentTertiaryInverse = Color(0xFF484848),
    backgroundContainer = Color(0xFF040404),
    backgroundPrimary = Color(0xFF181818),
    backgroundSecondary = Color(0xFF383838),
    backgroundTertiary = Color(0xFF585858),
    backgroundPrimaryInverse = Color(0xFFF8F8F8),
    backgroundSecondaryInverse = Color(0xFFF2F2F2),
    backgroundTertiaryInverse = Color(0xFFEEEEEE),
    backgroundNav = Color(0xFF383838),
    f1Podium1 = Color(0xFFD3BC4D),
    f1Podium2 = Color(0xFFC2C2C2),
    f1Podium3 = Color(0xFFD29342),
    f1DeltaPositive = Color(0xFFF44336),
//    f1DeltaNeutral = ,
    f1DeltaNegative = Color(0xFF4CAF50),
    f1ResultsFull = Color(0xFF4CAF50),
//    f1ResultsNeutral = ,
    f1ResultsPartial = Color(0xFFFFA000),
    f1FastestSector = Color(0xFF673AB7),
    f1FavouriteSeason = Color(0xFFE6CA4F),
    f1Championship = Color(0xFFE6CA4F),
//    f1PipeColor = ,
    rssAdd = Color(0xFF4CAF50),
    rssRemove = Color(0xFFF44336),
//    rssNewsBar =
    isLight = false
)