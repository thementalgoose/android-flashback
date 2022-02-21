package tmg.flashback.regulations.domain

import tmg.flashback.regulations.BuildConfig
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
    );
}