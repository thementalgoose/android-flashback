package tmg.flashback.ui.dashboard.list

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.upnext.repository.model.TimeListDisplayType
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.utilities.models.StringHolder

sealed class ListItem(
    @LayoutRes val layoutId: Int
) {
    object Hero: ListItem(R.layout.view_season_list_hero)

    object Divider: ListItem(R.layout.view_season_list_divider)

    data class Button(
        val itemId: String,
        @StringRes
        val label: Int,
        @DrawableRes
        val icon: Int
    ): ListItem(R.layout.view_season_list_button)

    data class Season(
        val season: Int,
        val isFavourited: Boolean,
        val fixed: HeaderType,
        val selected: Boolean = false,
        val default: Boolean = false,
        val showClearDefault: Boolean = false
    ): ListItem(R.layout.view_season_list_season)

    data class Header(
        val type: HeaderType,
        val expanded: Boolean? // null = hide option
    ): ListItem(R.layout.view_season_list_header)

    sealed class FeatureBanner(
        val key: String,
        val text: StringHolder,
        @DrawableRes
        val icon: Int? = null
    ): ListItem(R.layout.view_season_list_feature_banner) {

        object EnrolNotifications: FeatureBanner(
            key = "notifications",
            text = StringHolder(R.string.feature_banner_notifications)
        )
    }
}

enum class HeaderType(
    @StringRes val label: Int
) {
    FAVOURITED(R.string.home_season_header_favourited),
    ALL(R.string.home_season_header_All),
    LINKS(R.string.dashboard_season_list_extra_title)
}
