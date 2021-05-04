package tmg.flashback.shared.ui.extensions

import tmg.flashback.shared.ui.R
import tmg.flashback.shared.ui.model.Theme

val Theme.label: Int
    get() = when (this) {
        Theme.DAY -> R.string.settings_theme_theme_light
        Theme.DEFAULT -> R.string.settings_theme_theme_follow_system
        Theme.NIGHT -> R.string.settings_theme_theme_dark
    }

val Theme.icon: Int
    get() = when (this) {
        Theme.DAY -> R.drawable.ic_theme_light
        Theme.DEFAULT -> R.drawable.ic_theme_auto
        Theme.NIGHT -> R.drawable.ic_theme_dark
    }