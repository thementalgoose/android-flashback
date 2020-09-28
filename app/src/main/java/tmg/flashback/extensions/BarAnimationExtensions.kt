package tmg.flashback.extensions

import tmg.flashback.R
import tmg.flashback.repo.enums.BarAnimation

val BarAnimation.label: Int
    get() = when (this) {
        BarAnimation.NONE -> R.string.settings_bar_animation_instant
        BarAnimation.QUICK -> R.string.settings_bar_animation_quick
        BarAnimation.MEDIUM -> R.string.settings_bar_animation_medium
        BarAnimation.SLOW -> R.string.settings_bar_animation_slow
    }

val BarAnimation.icon: Int
    get() = when (this) {
        BarAnimation.NONE -> R.drawable.ic_bar_animation_none
        BarAnimation.QUICK -> R.drawable.ic_bar_animation_quick
        BarAnimation.MEDIUM -> R.drawable.ic_bar_animation_medium
        BarAnimation.SLOW -> R.drawable.ic_bar_animation_slow
    }