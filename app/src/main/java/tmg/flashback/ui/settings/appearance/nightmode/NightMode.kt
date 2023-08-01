package tmg.flashback.ui.settings.appearance.nightmode

import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.ui.model.NightMode

@get:StringRes
val NightMode.title: Int
    get() = when (this) {
        NightMode.DEFAULT -> R.string.settings_theme_nightmode_follow_system
        NightMode.DAY -> R.string.settings_theme_nightmode_light
        NightMode.NIGHT -> R.string.settings_theme_nightmode_dark
    }