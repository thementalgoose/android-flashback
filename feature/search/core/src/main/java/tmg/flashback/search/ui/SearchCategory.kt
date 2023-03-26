package tmg.flashback.search.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.search.R

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
        label = R.string.search_category_constructors,
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