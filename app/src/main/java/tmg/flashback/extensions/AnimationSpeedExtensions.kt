package tmg.flashback.extensions

import tmg.flashback.R
import tmg.flashback.core.enums.AnimationSpeed

val AnimationSpeed.label: Int
    get() = when (this) {
        AnimationSpeed.NONE -> R.string.settings_animation_speed_instant
        AnimationSpeed.QUICK -> R.string.settings_animation_speed_quick
        AnimationSpeed.MEDIUM -> R.string.settings_animation_speed_medium
        AnimationSpeed.SLOW -> R.string.settings_animation_speed_slow
    }

val AnimationSpeed.icon: Int
    get() = when (this) {
        AnimationSpeed.NONE -> R.drawable.ic_bar_animation_none
        AnimationSpeed.QUICK -> R.drawable.ic_bar_animation_quick
        AnimationSpeed.MEDIUM -> R.drawable.ic_bar_animation_medium
        AnimationSpeed.SLOW -> R.drawable.ic_bar_animation_slow
    }