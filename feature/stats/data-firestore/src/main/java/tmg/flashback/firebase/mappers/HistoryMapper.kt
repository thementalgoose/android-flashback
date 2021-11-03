package tmg.flashback.firebase.mappers

import java.lang.NullPointerException
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.firebase.models.FHistorySeason
import tmg.flashback.firebase.models.FHistorySeasonRound
import tmg.flashback.firebase.models.FHistorySeasonWin
import tmg.flashback.firebase.models.FHistorySeasonWinConstructor
import tmg.flashback.firebase.models.FHistorySeasonWinDriver
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate

class HistoryMapper(
    // TODO: Move this to DI from Flashback module and build config field
    private val allDataUpTo: Int,
    private val crashController: CrashController
) {

    fun mapHistory(input: FHistorySeason): List<tmg.flashback.formula1.model.Overview> {
        return input.all
            .mapNotNull { (key, rounds) ->
                val season = getSeason(rounds)
                if (season == -1) {
                    return@mapNotNull null
                }

                return@mapNotNull tmg.flashback.formula1.model.Overview(
                    season = season,
                    overviewRaces = rounds?.mapNotNull mapRounds@{ (_, value) ->
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

    fun mapHistoryRound(input: FHistorySeasonRound): tmg.flashback.formula1.model.OverviewRace {
        return tmg.flashback.formula1.model.OverviewRace(
            date = requireFromDate(input.date),
            time = null,
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
}