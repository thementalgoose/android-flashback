package tmg.flashback.firebase.converters

import tmg.flashback.firebase.base.ConverterUtils.fromDate
import tmg.flashback.firebase.models.FHistorySeason
import tmg.flashback.firebase.models.FHistorySeasonRound
import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.models.stats.HistoryRound

fun FHistorySeason.convert(): List<History> {
    val list: MutableList<History> = mutableListOf()
    val winners = this.convertWin()
    for ((_, rounds) in this.all) {
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
                    winner = winners.firstOrNull { it.season == season },
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
            hasQualifying = this.hasQ ?: this.data ?: (s <= 2019),
            hasResults = this.hasR ?: this.data ?: (s <= 2019)
        )
    )
}