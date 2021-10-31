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

    fun mapHistory(input: FHistorySeason): List<tmg.flashback.formula1.model.SeasonOverview> {
        return input.all
            .mapNotNull { (key, rounds) ->
                val season = getSeason(rounds)
                if (season == -1) {
                    return@mapNotNull null
                }

                return@mapNotNull tmg.flashback.formula1.model.SeasonOverview(
                    season = season,
//                    winner = input.win?.get(key)?.let { mapWinnerSeason(it) },
                    roundOverviews = rounds?.mapNotNull mapRounds@{ (_, value) ->
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

    fun mapHistoryRound(input: FHistorySeasonRound): tmg.flashback.formula1.model.RoundOverview {
        return tmg.flashback.formula1.model.RoundOverview(
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

    fun mapWinnerSeason(input: FHistorySeasonWin): tmg.flashback.formula1.model.WinnerSeason {
        return tmg.flashback.formula1.model.WinnerSeason(
            season = input.s,
            driver = input.driver
                ?.map { mapWinnerSeasonDriver(it) }
                ?: emptyList(),
            constructor = input.constr
                ?.map { mapWinnerSeasonConstructor(it) }
                ?: emptyList()
        )
    }

    fun mapWinnerSeasonDriver(input: FHistorySeasonWinDriver): tmg.flashback.formula1.model.WinnerSeasonDriver {
        return tmg.flashback.formula1.model.WinnerSeasonDriver(
            id = input.id,
            name = input.name,
            image = input.img,
            points = input.p
        )
    }

    fun mapWinnerSeasonConstructor(input: FHistorySeasonWinConstructor): tmg.flashback.formula1.model.WinnerSeasonConstructor {
        return tmg.flashback.formula1.model.WinnerSeasonConstructor(
            id = input.id,
            name = input.name,
            color = input.color,
            points = input.p
        )
    }
}