package tmg.core.ui.extensions

import tmg.core.ui.R
import tmg.core.ui.model.NightMode

val NightMode.label: Int
    get() = when (this) {
        NightMode.DAY -> R.string.settings_theme_theme_light
        NightMode.DEFAULT -> R.string.settings_theme_theme_follow_system
        NightMode.NIGHT -> R.string.settings_theme_theme_dark
    }

val NightMode.icon: Int
    get() = when (this) {
        NightMode.DAY -> R.drawable.ic_theme_light
        NightMode.DEFAULT -> R.drawable.ic_theme_auto
        NightMode.NIGHT -> R.drawable.ic_theme_dark
    }