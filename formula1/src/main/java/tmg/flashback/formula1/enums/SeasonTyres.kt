package tmg.flashback.formula1.enums

import tmg.flashback.formula1.R
import tmg.flashback.formula1.enums.Tyre.*

fun SeasonTyres.Companion.getBySeason(season: Int): SeasonTyres? {
    return SeasonTyres.values().firstOrNull { model -> model.season == season }
}

enum class SeasonTyres(
    val season: Int,
    val tyres: List<TyreLabel>
) {
    S2022(
        season = 2022,
        tyres = listOf(
            TyreLabel(tyre = RED_DRY_18, label = R.string.tyre_soft),
            TyreLabel(tyre = YELLOW_DRY_18, label = R.string.tyre_medium),
            TyreLabel(tyre = WHITE_DRY_18, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_18, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_18, label = R.string.tyre_wet)
        )
    ),
    S2021(
        season = 2021,
        tyres = listOf(
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2020(
        season = 2020,
        tyres = listOf(
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2019(
        season = 2019,
        tyres = listOf(
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2018(
        season = 2018,
        tyres = listOf(
            TyreLabel(tyre = PINK_DRY_13, label = R.string.tyre_hyper_soft),
            TyreLabel(tyre = PURPLE_DRY_13, label = R.string.tyre_ultra_soft),
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_super_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = BLUE_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = ORANGE_DRY_13, label = R.string.tyre_super_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2017(
        season = 2017,
        tyres = listOf(
            TyreLabel(tyre = PURPLE_DRY_13, label = R.string.tyre_ultra_soft),
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_super_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = ORANGE_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2016(
        season = 2016,
        tyres = listOf(
            TyreLabel(tyre = PURPLE_DRY_13, label = R.string.tyre_ultra_soft),
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_super_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = ORANGE_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2015(
        season = 2015,
        tyres = listOf(
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_super_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = ORANGE_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2014(
        season = 2014,
        tyres = listOf(
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_super_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = ORANGE_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2013(
        season = 2013,
        tyres = listOf(
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_super_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = ORANGE_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2012(
        season = 2012,
        tyres = listOf(
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_super_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = GRAY_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = GREEN_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_wet)
        )
    ),
    S2011(
        season = 2011,
        tyres = listOf(
            TyreLabel(tyre = RED_DRY_13, label = R.string.tyre_super_soft),
            TyreLabel(tyre = YELLOW_DRY_13, label = R.string.tyre_soft),
            TyreLabel(tyre = WHITE_DRY_13, label = R.string.tyre_medium),
            TyreLabel(tyre = GRAY_DRY_13, label = R.string.tyre_hard),
            TyreLabel(tyre = BLUE_WET_13, label = R.string.tyre_intermediate),
            TyreLabel(tyre = ORANGE_WET_13, label = R.string.tyre_wet)
        )
    );

    companion object
}