package tmg.flashback.home.season

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.R

sealed class SeasonListItem {

    object Top: SeasonListItem()

    data class Season(
        val season: Int,
        val isFavourited: Boolean,
        val fixed: HeaderType
    ): SeasonListItem()

    data class Header(
        val type: HeaderType,
        val expanded: Boolean? // null = hide option
    ): SeasonListItem()
}

enum class HeaderType(
    @StringRes val label: Int
) {
    CURRENT(R.string.home_season_header_current),
    FAVOURITED(R.string.home_season_header_favourited),
    ALL(R.string.home_season_header_All)
}
