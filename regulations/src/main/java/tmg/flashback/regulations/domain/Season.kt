package tmg.flashback.regulations.domain

import tmg.flashback.regulations.BuildConfig
import tmg.flashback.regulations.domain.seasons.*
import tmg.flashback.regulations.domain.seasons.season2019
import tmg.flashback.regulations.domain.seasons.season2020
import tmg.flashback.regulations.domain.seasons.season2021
import tmg.flashback.regulations.domain.seasons.season2022

fun hasFormatInfoFor(season: Int): Boolean {
    if (BuildConfig.DEBUG) {
        return Season.values().any { it.season == season }
    }
    return false
}

internal enum class Season(
    val season: Int,
    val sections: List<Section>
) {
    SEASON_2022(
        season = 2022,
        sections = season2022
    ),
    SEASON_2021(
        season = 2021,
        sections = season2021
    ),
    SEASON_2020(
        season = 2020,
        sections = season2020
    ),
    SEASON_2019(
        season = 2019,
        sections = season2019
    ),
    SEASON_2018(
        season = 2018,
        sections = season2018
    ),
    SEASON_2017(
        season = 2017,
        sections = season2017
    ),
    SEASON_2016(
        season = 2016,
        sections = season2016
    );
}