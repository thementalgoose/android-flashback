package tmg.core.ui.extensions

import androidx.annotation.DrawableRes
import tmg.core.ui.R
import tmg.core.ui.model.NightMode
import tmg.core.ui.model.Theme

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