package tmg.flashback.presentation.settings.appearance.nightmode

import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.strings.R.string
import tmg.flashback.ui.model.NightMode

@get:StringRes
val NightMode.title: Int
    get() = when (this) {
        NightMode.DEFAULT -> string.settings_theme_nightmode_follow_system
        NightMode.DAY -> string.settings_theme_nightmode_light
        NightMode.NIGHT -> string.settings_theme_nightmode_dark
    }