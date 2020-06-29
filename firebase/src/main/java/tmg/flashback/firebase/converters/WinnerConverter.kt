package tmg.flashback.firebase.converters

import tmg.flashback.firebase.models.*
import tmg.flashback.repo.models.stats.WinnerSeason
import tmg.flashback.repo.models.stats.WinnerSeasonConstructor
import tmg.flashback.repo.models.stats.WinnerSeasonDriver

fun FHistorySeason.convertWin(): List<WinnerSeason> {
    return win?.map { (_, value) ->
        value.convert()
    } ?: emptyList()
}

fun FHistorySeasonWin.convert(): WinnerSeason {
    return WinnerSeason(
        season = s ?: -1,
        driver = this.driver?.map { it.convert() } ?: emptyList(),
        constructor = this.constr?.map { it.convert() } ?: emptyList()
    )
}

fun FHistorySeasonWinDriver.convert(): WinnerSeasonDriver {
    return WinnerSeasonDriver(
        id,
        name,
        img ?: "",
        p
    )
}

fun FHistorySeasonWinConstructor.convert(): WinnerSeasonConstructor {
    return WinnerSeasonConstructor(
        id,
        name,
        color ?: "",
        p
    )
}