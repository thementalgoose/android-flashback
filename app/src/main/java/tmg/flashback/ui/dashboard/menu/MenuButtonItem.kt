package tmg.flashback.ui.dashboard.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.R

sealed class MenuItems(
    val id: String
) {

    sealed class Button(
        @StringRes
        val label: Int,
        @DrawableRes
        val icon: Int
    ): MenuItems(label.toString()) {
        object Search: Button(
            label = R.string.dashboard_links_search,
            icon = R.drawable.dashboard_search
        )
        object Rss: Button(
            label = R.string.dashboard_links_rss,
            icon = R.drawable.dashboard_rss
        )
        object Settings: Button(
            label = R.string.dashboard_links_settings,
            icon = R.drawable.dashboard_settings
        )
        object Contact: Button(
            label = R.string.dashboard_links_contact,
            icon = R.drawable.dashboard_contact
        )
    }

    object Divider: MenuItems("divider")

    sealed class Toggle(
        @StringRes
        val label: Int,
        @DrawableRes
        val icon: Int,
        val isEnabled: Boolean
    ): MenuItems(label.toString()) {
        data class DarkMode(
            private val _isEnabled: Boolean
        ): Toggle(
            label = R.string.dashboard_links_dark_mode,
            icon = R.drawable.dashboard_darkmode,
            isEnabled = _isEnabled
        )
    }
}

