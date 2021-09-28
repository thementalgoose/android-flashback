package tmg.flashback.firebase.mappers

import java.lang.NullPointerException
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.History
import tmg.flashback.data.models.stats.HistoryRound
import tmg.flashback.data.models.stats.WinnerSeason
import tmg.flashback.data.models.stats.WinnerSeasonConstructor
import tmg.flashback.data.models.stats.WinnerSeasonDriver
import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.firebase.models.FHistorySeason
import tmg.flashback.firebase.models.FHistorySeasonRound
import tmg.flashback.firebase.models.FHistorySeasonWin
import tmg.flashback.firebase.models.FHistorySeasonWinConstructor
import tmg.flashback.firebase.models.FHistorySeasonWinDriver

class HistoryMapper(
    // TODO: Move this to DI from Flashback module and build config field
    private val allDataUpTo: Int = 2020,
    private val crashController: CrashController
) {

    fun mapHistory(input: FHistorySeason): List<History> {
        return input.all
            .mapNotNull { (key, rounds) ->
                val season = getSeason(rounds)
                if (season == -1) {
                    return@mapNotNull null
                }

                return@mapNotNull History(
                    season = season,
                    winner = input.win?.get(key)?.let { mapWinnerSeason(it) },
                    rounds = rounds?.mapNotNull mapRounds@{ (_, value) ->
                        if (value == null) {
                            crashController.logException(NullPointerException("HistoryMapper.mapHistory season=$season round is set to null"))
                            return@mapRounds null
                        }
                        mapHistoryRound(value)
                    } ?: emptyList()
                )
            }
    }

    private fun getSeason(rounds: Map<String, FHistorySeasonRound?>?): Int {
        return (rounds ?: emptyMap())
            .mapNotNull { (_, value) ->
                value?.s
            }
            .firstOrNull() ?: -1
    }

    fun mapHistoryRound(input: FHistorySeasonRound): HistoryRound {
        return HistoryRound(
            date = fromDateRequired(input.date),
            season = input.s,
            round = input.r,
            circuitId = input.circuitId,
            raceName = input.name,
            country = input.country,
            countryISO = input.countryISO,
            circuitName = input.circuit,
            hasQualifying = input.hasQ ?: input.data ?: (input.s < allDataUpTo),
            hasResults = input.hasR ?: input.data ?: (input.s < allDataUpTo)
        )
    }

    fun mapWinnerSeason(input: FHistorySeasonWin): WinnerSeason {
        return WinnerSeason(
            season = input.s,
            driver = input.driver
                ?.map { mapWinnerSeasonDriver(it) }
                ?: emptyList(),
            constructor = input.constr
                ?.map { mapWinnerSeasonConstructor(it) }
                ?: emptyList()
        )
    }

    fun mapWinnerSeasonDriver(input: FHistorySeasonWinDriver): WinnerSeasonDriver {
        return WinnerSeasonDriver(
            id = input.id,
            name = input.name,
            image = input.img,
            points = input.p
        )
    }

    fun mapWinnerSeasonConstructor(input: FHistorySeasonWinConstructor): WinnerSeasonConstructor {
        return WinnerSeasonConstructor(
            id = input.id,
            name = input.name,
            color = input.color,
            points = input.p
        )
    }
}