package tmg.flashback.style

import androidx.compose.material.Colors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalColors = staticCompositionLocalOf { lightColours }

class AppColors(
    // Theme
    primary: Color,
    primaryDark: Color,
    accent: Color,
    error: Color,
    // Content
    contentPrimary: Color,
    contentSecondary: Color,
    contentTertiary: Color,
    contentPrimaryInverse: Color,
    contentSecondaryInverse: Color,
    contentTertiaryInverse: Color,
    // Background
    backgroundContainer: Color,
    backgroundPrimary: Color,
    backgroundSecondary: Color,
    backgroundTertiary: Color,
    backgroundPrimaryInverse: Color,
    backgroundSecondaryInverse: Color,
    backgroundTertiaryInverse: Color,
    backgroundNav: Color,
    // F1
    f1Podium1: Color,
    f1Podium2: Color,
    f1Podium3: Color,
    f1DeltaPositive: Color,
    f1DeltaNeutral: Color = contentSecondary,
    f1DeltaNegative: Color,
    f1ResultsFull: Color,
    f1ResultsNeutral: Color = contentSecondary,
    f1ResultsPartial: Color,
    f1FastestSector: Color,
    f1FavouriteSeason: Color,
    f1Championship: Color,
    f1PipeColor: Color = contentTertiary,
    // RSS
    rssAdd: Color,
    rssRemove: Color,
    rssNewsBar: Color = backgroundSecondary,
    isLight: Boolean
) {

    // Theme
    var primary by mutableStateOf(primary)
        internal set
    var primaryDark by mutableStateOf(primaryDark)
        internal set
    var accent by mutableStateOf(accent)
        internal set
    var error by mutableStateOf(error)
        internal set
    // Content
    var contentPrimary by mutableStateOf(contentPrimary)
        internal set
    var contentSecondary by mutableStateOf(contentSecondary)
        internal set
    var contentTertiary by mutableStateOf(contentTertiary)
        internal set
    var contentPrimaryInverse by mutableStateOf(contentPrimaryInverse)
        internal set
    var contentSecondaryInverse by mutableStateOf(contentSecondaryInverse)
        internal set
    var contentTertiaryInverse by mutableStateOf(contentTertiaryInverse)
        internal set
    // Background
    var backgroundContainer by mutableStateOf(backgroundContainer)
        internal set
    var backgroundPrimary by mutableStateOf(backgroundPrimary)
        internal set
    var backgroundSecondary by mutableStateOf(backgroundSecondary)
        internal set
    var backgroundTertiary by mutableStateOf(backgroundTertiary)
        internal set
    var backgroundPrimaryInverse by mutableStateOf(backgroundPrimaryInverse)
        internal set
    var backgroundSecondaryInverse by mutableStateOf(backgroundSecondaryInverse)
        internal set
    var backgroundTertiaryInverse by mutableStateOf(backgroundTertiaryInverse)
        internal set
    var backgroundNav by mutableStateOf(backgroundNav)
        internal set
    // F1
    var f1Podium1 by mutableStateOf(f1Podium1)
        internal set
    var f1Podium2 by mutableStateOf(f1Podium2)
        internal set
    var f1Podium3 by mutableStateOf(f1Podium3)
        internal set
    var f1DeltaPositive by mutableStateOf(f1DeltaPositive)
        internal set
    var f1DeltaNeutral by mutableStateOf(f1DeltaNeutral)
        internal set
    var f1DeltaNegative by mutableStateOf(f1DeltaNegative)
        internal set
    var f1ResultsFull by mutableStateOf(f1ResultsFull)
        internal set
    var f1ResultsNeutral by mutableStateOf(f1ResultsNeutral)
        internal set
    var f1ResultsPartial by mutableStateOf(f1ResultsPartial)
        internal set
    var f1FastestSector by mutableStateOf(f1FastestSector)
        internal set
    var f1FavouriteSeason by mutableStateOf(f1FavouriteSeason)
        internal set
    var f1Championship by mutableStateOf(f1Championship)
        internal set
    var f1PipeColor by mutableStateOf(f1PipeColor)
        internal set
    // RSS
    var rssAdd by mutableStateOf(rssAdd)
        internal set
    var rssRemove by mutableStateOf(rssRemove)
        internal set
    var rssNewsBar by mutableStateOf(rssNewsBar)
        internal set
    var isLight by mutableStateOf(isLight)
        internal set

    fun copy(
        primary: Color = this.primary,
        primaryDark: Color = this.primaryDark,
        accent: Color = this.accent,
        error: Color = this.error,
        contentPrimary: Color = this.contentPrimary,
        contentSecondary: Color = this.contentSecondary,
        contentTertiary: Color = this.contentTertiary,
        contentPrimaryInverse: Color = this.contentPrimaryInverse,
        contentSecondaryInverse: Color = this.contentSecondaryInverse,
        contentTertiaryInverse: Color = this.contentTertiaryInverse,
        backgroundContainer: Color = this.backgroundContainer,
        backgroundPrimary: Color = this.backgroundPrimary,
        backgroundSecondary: Color = this.backgroundSecondary,
        backgroundTertiary: Color = this.backgroundTertiary,
        backgroundPrimaryInverse: Color = this.backgroundPrimaryInverse,
        backgroundSecondaryInverse: Color = this.backgroundSecondaryInverse,
        backgroundTertiaryInverse: Color = this.backgroundTertiaryInverse,
        backgroundNav: Color = this.backgroundNav,
        f1Podium1: Color = this.f1Podium1,
        f1Podium2: Color = this.f1Podium2,
        f1Podium3: Color = this.f1Podium3,
        f1DeltaPositive: Color = this.f1DeltaPositive,
        f1DeltaNeutral: Color = this.f1DeltaNeutral,
        f1DeltaNegative: Color = this.f1DeltaNegative,
        f1ResultsFull: Color = this.f1ResultsFull,
        f1ResultsNeutral: Color = this.f1ResultsNeutral,
        f1ResultsPartial: Color = this.f1ResultsPartial,
        f1FastestSector: Color = this.f1FastestSector,
        f1FavouriteSeason: Color = this.f1FavouriteSeason,
        f1Championship: Color = this.f1Championship,
        f1PipeColor: Color = this.f1PipeColor,
        rssAdd: Color = this.rssAdd,
        rssRemove: Color = this.rssRemove,
        rssNewsBar: Color = this.rssNewsBar,
        isLight: Boolean = this.isLight
    ): AppColors = AppColors(
        primary,
        primaryDark,
        accent,
        error,
        contentPrimary,
        contentSecondary,
        contentTertiary,
        contentPrimaryInverse,
        contentSecondaryInverse,
        contentTertiaryInverse,
        backgroundContainer,
        backgroundPrimary,
        backgroundSecondary,
        backgroundTertiary,
        backgroundPrimaryInverse,
        backgroundSecondaryInverse,
        backgroundTertiaryInverse,
        backgroundNav,
        f1Podium1,
        f1Podium2,
        f1Podium3,
        f1DeltaPositive,
        f1DeltaNeutral,
        f1DeltaNegative,
        f1ResultsFull,
        f1ResultsNeutral,
        f1ResultsPartial,
        f1FastestSector,
        f1FavouriteSeason,
        f1Championship,
        f1PipeColor,
        rssAdd,
        rssRemove,
        rssNewsBar,
        isLight
    )

    fun updateFrom(other: AppColors) {
        primary = other.primary
        primaryDark = other.primaryDark
        accent = other.accent
        error = other.error
        contentPrimary = other.contentPrimary
        contentSecondary = other.contentSecondary
        contentTertiary = other.contentTertiary
        contentPrimaryInverse = other.contentPrimaryInverse
        contentSecondaryInverse = other.contentSecondaryInverse
        contentTertiaryInverse = other.contentTertiaryInverse
        backgroundContainer = other.backgroundContainer
        backgroundPrimary = other.backgroundPrimary
        backgroundSecondary = other.backgroundSecondary
        backgroundTertiary = other.backgroundTertiary
        backgroundPrimaryInverse = other.backgroundPrimaryInverse
        backgroundSecondaryInverse = other.backgroundSecondaryInverse
        backgroundTertiaryInverse = other.backgroundTertiaryInverse
        backgroundNav = other.backgroundNav
        f1Podium1 = other.f1Podium1
        f1Podium2 = other.f1Podium2
        f1Podium3 = other.f1Podium3
        f1DeltaPositive = other.f1DeltaPositive
        f1DeltaNeutral = other.f1DeltaNeutral
        f1DeltaNegative = other.f1DeltaNegative
        f1ResultsFull = other.f1ResultsFull
        f1ResultsNeutral = other.f1ResultsNeutral
        f1ResultsPartial = other.f1ResultsPartial
        f1FastestSector = other.f1FastestSector
        f1FavouriteSeason = other.f1FavouriteSeason
        f1Championship = other.f1Championship
        f1PipeColor = other.f1PipeColor
        rssAdd = other.rssAdd
        rssRemove = other.rssRemove
        rssNewsBar = other.rssNewsBar
        isLight = other.isLight
    }

    val materialColours: Colors = Colors(
        primary = primary,
        primaryVariant = primary,
        secondary = accent,
        secondaryVariant = accent,
        background = backgroundContainer,
        surface = backgroundPrimary,
        error = error,
        onPrimary = contentPrimaryInverse,
        onSecondary = contentPrimaryInverse,
        onBackground = contentPrimaryInverse,
        onSurface = contentPrimary,
        onError = Color.White,
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