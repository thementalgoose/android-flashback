package tmg.flashback.presentation.settings.appearance.theme

import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.ui.model.Theme

@get:StringRes
val Theme.title: Int
    get() = when (this) {
        Theme.DEFAULT -> R.string.settings_theme_theme_default
        Theme.MATERIAL_YOU -> R.string.settings_theme_theme_material_you
    }