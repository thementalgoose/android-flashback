package tmg.flashback.ui.extensions

import androidx.annotation.DrawableRes
import tmg.flashback.ui.R
import tmg.flashback.strings.R.string
import tmg.flashback.ui.model.NightMode

val NightMode.label: Int
    get() = when (this) {
        NightMode.DAY -> string.settings_theme_nightmode_light
        NightMode.DEFAULT -> string.settings_theme_nightmode_follow_system
        NightMode.NIGHT -> string.settings_theme_nightmode_dark
    }

val NightMode.icon: Int
    @DrawableRes
    get() = when (this) {
        NightMode.DAY -> R.drawable.ic_nightmode_light
        NightMode.DEFAULT -> R.drawable.ic_nightmode_auto
        NightMode.NIGHT -> R.drawable.ic_nightmode_dark
    }