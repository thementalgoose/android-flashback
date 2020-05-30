package tmg.flashback.repo_firebase.converters

import tmg.flashback.repo.models.WinnerSeason
import tmg.flashback.repo.models.WinnerSeasonConstructor
import tmg.flashback.repo.models.WinnerSeasonDriver
import tmg.flashback.repo_firebase.models.FWinner
import tmg.flashback.repo_firebase.models.FWinnerSeason
import tmg.flashback.repo_firebase.models.FWinnerSeasonConstructor
import tmg.flashback.repo_firebase.models.FWinnerSeasonDriver

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