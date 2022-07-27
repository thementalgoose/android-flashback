package tmg.flashback.ui.dashboard.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.R
import java.util.*

sealed class MenuItems(
    val id: String
) {

    sealed class Button(
        @StringRes
        val label: Int,
        @DrawableRes
        val icon: Int,
        id: String = label.toString()
    ): MenuItems(id) {
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

        class Custom(
            @StringRes
            label: Int,
            @DrawableRes
            icon: Int,
            id: String
        ): Button(
            label = label,
            icon = icon,
            id = id
        ) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                return true
            }

            override fun hashCode(): Int {
                return javaClass.hashCode()
            }
        }

        companion object
    }

    data class Divider(
        val guid: String = UUID.randomUUID().toString()
    ): MenuItems(guid)

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

    sealed class Feature(
        @StringRes
        val label: Int
    ): MenuItems(label.toString()) {

        object Notifications: Feature(
            label = R.string.feature_banner_notifications
        )
    }
}

