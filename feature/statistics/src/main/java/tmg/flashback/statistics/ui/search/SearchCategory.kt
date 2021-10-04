package tmg.flashback.statistics.ui.search

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.statistics.R

enum class SearchCategory(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
) {
    DRIVER(
        label = R.string.search_category_driver,
        icon = R.drawable.ic_search_driver
    ),
    CONSTRUCTOR(
        label = R.string.search_category_constructor,
        icon = R.drawable.ic_search_constructor
    ),
    CIRCUIT(
        label = R.string.search_category_circuits,
        icon = R.drawable.ic_search_circuits
    ),
    RACE(
        label = R.string.search_category_races,
        icon = R.drawable.ic_search_race
    ),
}