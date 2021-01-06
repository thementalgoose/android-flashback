package tmg.flashback.ui.base

import androidx.annotation.StyleRes
import tmg.flashback.R

enum class ThemeTypes(
        @StyleRes
        val lightTheme: Int,
        @StyleRes
        val darkTheme: Int
) {
    DEFAULT(lightTheme = R.style.LightTheme, darkTheme = R.style.DarkTheme),
    TRANSLUCENT(lightTheme = R.style.LightTheme_Translucent, darkTheme = R.style.DarkTheme_Translucent),
    ABOUT_APP(lightTheme = R.style.LightTheme_AboutThisApp, darkTheme = R.style.DarkTheme_AboutThisApp)
}