package tmg.flashback.firebase.converters

import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.models.stats.HistoryRound
import tmg.flashback.firebase.models.*
import java.time.Year

fun FHistorySeason.convert(): List<History> {
    val list: MutableList<History> = mutableListOf()
    for ((key, rounds) in this.all) {
        var season: Int = -1
        val historyRounds = rounds
            ?.mapValues { (_, round) ->
                if (round != null) {
                    season = round.s
                }
                round?.convert()
            }
            ?.map { it.value?.second }
            ?.filterNotNull()
            ?.sortedBy { it.round }

        if (season != -1) {
            list.add(
                History(
                    season = season,
                    rounds = historyRounds ?: listOf()
                )
            )
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
            hasResults = this.data ?: (this.s <= 2019)
        )
    )
}