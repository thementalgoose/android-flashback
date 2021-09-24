package tmg.flashback.firebase.converters

import tmg.flashback.firebase.models.*
import tmg.flashback.data.models.stats.WinnerSeason
import tmg.flashback.data.models.stats.WinnerSeasonConstructor
import tmg.flashback.data.models.stats.WinnerSeasonDriver

fun FHistorySeason.convertWin(): List<WinnerSeason> {
    return win?.map { (_, value) ->
        value.convert()
    } ?: emptyList()
}

private fun FHistorySeasonWin.convert(): WinnerSeason {
    return WinnerSeason(
        season = s,
        driver = this.driver?.map { it.convert() } ?: emptyList(),
        constructor = this.constr?.map { it.convert() } ?: emptyList()
    )
}

private fun FHistorySeasonWinDriver.convert(): WinnerSeasonDriver {
    return WinnerSeasonDriver(
        id,
        name,
        img,
        p
    )
}

private fun FHistorySeasonWinConstructor.convert(): WinnerSeasonConstructor {
    return WinnerSeasonConstructor(
        id,
        name,
        color,
        p
    )
}