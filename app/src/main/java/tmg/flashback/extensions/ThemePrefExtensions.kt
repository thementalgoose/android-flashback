package tmg.flashback.extensions

import android.content.Context
import tmg.flashback.R
import tmg.flashback.repo.enums.ThemePref
import tmg.utilities.extensions.isInDayMode

val ThemePref.label: Int
    get() = when (this) {
        ThemePref.DAY -> R.string.settings_theme_theme_light
        ThemePref.AUTO -> R.string.settings_theme_theme_follow_system
        ThemePref.NIGHT -> R.string.settings_theme_theme_dark
    }

val ThemePref.icon: Int
    get() = when (this) {
        ThemePref.DAY -> R.drawable.ic_theme_light
        ThemePref.AUTO -> R.drawable.ic_theme_auto
        ThemePref.NIGHT -> R.drawable.ic_theme_dark
    }

fun ThemePref.isLightMode(context: Context): Boolean {
    return this == ThemePref.DAY || (this == ThemePref.AUTO && context.isInDayMode())
}