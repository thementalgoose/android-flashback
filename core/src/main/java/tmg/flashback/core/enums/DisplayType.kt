package tmg.flashback.core.enums

import androidx.annotation.StyleRes
import tmg.flashback.core.R

enum class DisplayType(
    @StyleRes
    val lightTheme: Int,
    @StyleRes
    val darkTheme: Int
) {
    DEFAULT(lightTheme = R.style.LightTheme, darkTheme = R.style.DarkTheme),
    TRANSLUCENT(lightTheme = R.style.LightTheme_Translucent, darkTheme = R.style.DarkTheme_Translucent)
}