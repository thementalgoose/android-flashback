package tmg.flashback.firebase.converters

import tmg.flashback.repo.models.stats.WinnerSeason
import tmg.flashback.repo.models.stats.WinnerSeasonConstructor
import tmg.flashback.repo.models.stats.WinnerSeasonDriver
import tmg.flashback.firebase.models.FWinner
import tmg.flashback.firebase.models.FWinnerSeason
import tmg.flashback.firebase.models.FWinnerSeasonConstructor
import tmg.flashback.firebase.models.FWinnerSeasonDriver

fun FWinner.convert(): List<WinnerSeason> {
    return win?.map { (_, value) ->
        value.convert()
    } ?: emptyList()
}

fun FWinnerSeason.convert(): WinnerSeason {
    return WinnerSeason(
        season = s ?: -1,
        driver = this.driver?.map { it.convert() } ?: emptyList(),
        constructor = this.constr?.map { it.convert() } ?: emptyList()
    )
}

fun FWinnerSeasonDriver.convert(): WinnerSeasonDriver {
    return WinnerSeasonDriver(
        id,
        name,
        img ?: "",
        p
    )
}

fun FWinnerSeasonConstructor.convert(): WinnerSeasonConstructor {
    return WinnerSeasonConstructor(
        id,
        name,
        color ?: "",
        p
    )
}