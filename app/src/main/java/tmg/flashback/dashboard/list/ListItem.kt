package tmg.flashback.dashboard.list

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.R
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule

sealed class ListItem(
    @LayoutRes val layoutId: Int
) {
    object Hero: ListItem(R.layout.view_season_list_hero)

    data class UpNext(
        val upNextSchedule: UpNextSchedule
    ): ListItem(R.layout.view_season_list_up_next)

    data class Season(
        val season: Int,
        val isFavourited: Boolean,
        val fixed: HeaderType,
        val selected: Boolean = false
    ): ListItem(R.layout.view_season_list_season)

    data class Header(
        val type: HeaderType,
        val expanded: Boolean? // null = hide option
    ): ListItem(R.layout.view_season_list_header)
}

enum class HeaderType(
    @StringRes val label: Int
) {
    FAVOURITED(R.string.home_season_header_favourited),
    ALL(R.string.home_season_header_All)
}
