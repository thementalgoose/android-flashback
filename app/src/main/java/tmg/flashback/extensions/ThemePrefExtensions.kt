package tmg.flashback.extensions

val Theme.label: Int
    get() = when (this) {
        Theme.DAY -> R.string.settings_theme_theme_light
        Theme.AUTO -> R.string.settings_theme_theme_follow_system
        Theme.NIGHT -> R.string.settings_theme_theme_dark
    }

val Theme.icon: Int
    get() = when (this) {
        Theme.DAY -> R.drawable.ic_theme_light
        Theme.AUTO -> R.drawable.ic_theme_auto
        Theme.NIGHT -> R.drawable.ic_theme_dark
    }