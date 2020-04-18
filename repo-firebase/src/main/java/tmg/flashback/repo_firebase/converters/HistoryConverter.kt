package tmg.flashback.repo_firebase.converters

import tmg.flashback.repo.models.History
import tmg.flashback.repo.models.HistoryRound
import tmg.flashback.repo_firebase.models.FHistorySeason
import tmg.flashback.repo_firebase.models.FHistorySeasonRound

fun FHistorySeason.convert(): List<History> {
    val list: MutableList<History> = mutableListOf()
    for ((_, rounds) in this.all) {
        var season: Int = -1
        val historyRounds = rounds
            .mapValues { (_, round) ->
                season = round.season
                round.convert()
            }
            .map { it.value.second }

        if (season != -1) {
            list.add(History(season, historyRounds))
        }
    }
    return list
}

fun FHistorySeasonRound.convert(): Pair<Int, HistoryRound> {
    return Pair(
        this.season, HistoryRound(
            season = this.season,
            round = this.round,
            raceName = this.raceName,
            country = this.country,
            countryISO = this.countryISO,
            circuitName = this.circuit
        )
    )
}