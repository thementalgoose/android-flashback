package tmg.flashback.repo_firebase.converters

import androidx.core.graphics.toColorInt
import tmg.flashback.repo.models.History
import tmg.flashback.repo.models.HistoryRound
import tmg.flashback.repo.models.HistoryWinConstructor
import tmg.flashback.repo.models.HistoryWinDriver
import tmg.flashback.repo_firebase.models.*

fun FHistorySeason.convert(): List<History> {
    val list: MutableList<History> = mutableListOf()
    for ((key, rounds) in this.all) {
        var season: Int = -1
        val historyRounds = rounds
            .mapValues { (_, round) ->
                season = round.s
                round.convert()
            }
            .map { it.value.second }
            .sortedBy { it.round }

        if (season != -1) {
            val (constructors, drivers) = this.win?.get(key)?.convert() ?: Pair(emptyList(), emptyList())
            list.add(History(
                season = season,
                rounds = historyRounds,
                driversChampion = drivers,
                constructorsChampion = constructors
            ))
        }
    }
    return list
}

fun FHistorySeasonRound.convert(): Pair<Int, HistoryRound> {
    return Pair(
        this.s, HistoryRound(
            date = fromDate(this.date),
            season = this.s,
            round = this.r,
            circuitId = this.circuitId,
            raceName = this.name,
            country = this.country,
            countryISO = this.countryISO,
            circuitName = this.circuit,
            hasResults = this.hasResults ?: false
        )
    )
}

fun FHistorySeasonWin.convert(): Pair<List<HistoryWinConstructor>, List<HistoryWinDriver>> {
    return Pair(
        this.constr?.map { it.convert() } ?: emptyList(),
        this.driver?.map { it.convert() } ?: emptyList()
    )
}

fun FHistorySeasonWinDriver.convert(): HistoryWinDriver {
    return HistoryWinDriver(
        id = this.id,
        name = this.name,
        photoUrl = this.img,
        points = this.p
    )
}

fun FHistorySeasonWinConstructor.convert(): HistoryWinConstructor {
    return HistoryWinConstructor(
        id = this.id,
        name = this.name,
        color = this.color,
        points = this.p
    )
}