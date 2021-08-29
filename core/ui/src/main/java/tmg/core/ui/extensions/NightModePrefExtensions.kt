package tmg.core.ui.extensions

import androidx.annotation.DrawableRes
import tmg.core.ui.R
import tmg.core.ui.model.NightMode

val NightMode.label: Int
    get() = when (this) {
        NightMode.DAY -> R.string.settings_theme_nightmode_light
        NightMode.DEFAULT -> R.string.settings_theme_nightmode_follow_system
        NightMode.NIGHT -> R.string.settings_theme_nightmode_dark
    }

val NightMode.icon: Int
    @DrawableRes
    get() = when (this) {
        NightMode.DAY -> R.drawable.ic_nightmode_light
        NightMode.DEFAULT -> R.drawable.ic_nightmode_auto
        NightMode.NIGHT -> R.drawable.ic_nightmode_dark
    }