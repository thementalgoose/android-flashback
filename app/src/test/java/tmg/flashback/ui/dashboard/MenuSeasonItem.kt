package tmg.flashback.ui.dashboard

import androidx.compose.ui.graphics.Color
import tmg.flashback.formula1.constants.Formula1

internal fun MenuSeasonItem.Companion.model(
    season: Int = 2020,
    isSelected: Boolean = false,
    isLast: Boolean = false,
    isFirst: Boolean = false,
    colour: Color = Formula1.decadeColours["${season.toString().substring(0, 3)}0"]!!,
): MenuSeasonItem = MenuSeasonItem(
    colour = colour,
    season = season,
    isSelected = isSelected,
    isLast = isLast,
    isFirst = isFirst
)

internal val expectedMenuItems = listOf(
    MenuSeasonItem(
        colour = Formula1.decadeColours["2020"]!!,
        season = 2022,
        isSelected = true,
        isFirst = true,
        isLast = false
    ),
    MenuSeasonItem(
        colour = Formula1.decadeColours["2020"]!!,
        season = 2021,
        isSelected = false,
        isFirst = false,
        isLast = false
    ),
    MenuSeasonItem(
        colour = Formula1.decadeColours["2020"]!!,
        season = 2020,
        isSelected = false,
        isFirst = false,
        isLast = false
    ),
    MenuSeasonItem(
        colour = Formula1.decadeColours["2010"]!!,
        season = 2019,
        isSelected = false,
        isFirst = false,
        isLast = true
    ),
    MenuSeasonItem(
        colour = Formula1.decadeColours["2010"]!!,
        season = 2017,
        isSelected = false,
        isFirst = true,
        isLast = true
    )
)