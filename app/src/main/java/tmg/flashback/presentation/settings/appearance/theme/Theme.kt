package tmg.flashback.presentation.settings.appearance.theme

import androidx.annotation.StringRes
import tmg.flashback.strings.R.string
import tmg.flashback.ui.model.Theme

@get:StringRes
val Theme.title: Int
    get() = when (this) {
        Theme.DEFAULT -> string.settings_theme_theme_default
        Theme.MATERIAL_YOU -> string.settings_theme_theme_material_you
    }