package tmg.flashback.ui.extensions

import androidx.annotation.DrawableRes
import tmg.flashback.ui.R
import tmg.flashback.ui.model.Theme

val Theme.label: Int
    get() = when (this) {
        Theme.DEFAULT -> R.string.settings_theme_theme_default
        Theme.MATERIAL_YOU -> R.string.settings_theme_theme_material_you
    }

val Theme.icon: Int
    @DrawableRes
    get() = when (this) {
        Theme.DEFAULT -> R.drawable.ic_theme_default
        Theme.MATERIAL_YOU -> R.drawable.ic_theme_material_you
    }