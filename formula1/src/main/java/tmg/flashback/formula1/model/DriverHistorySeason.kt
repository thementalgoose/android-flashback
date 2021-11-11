package tmg.flashback.formula1.model

import tmg.flashback.formula1.enums.isStatusFinished

data class DriverHistorySeason(
    val championshipStanding: Int?,
    val isInProgress: Boolean,
    val points: Double,
    val season: Int,
    val constructors: List<Constructor>,
    val raceOverview: List<DriverHistorySeasonRace>
) {
    val bestFinish: Int? by lazy {
        return@lazy raceOverview.minByOrNull { it.finished }?.finished
    }
    val bestFinishQuantity: Int by lazy {
        return@lazy raceOverview.count { it.finished == bestFinish }
    }
    val bestQualifying: Int? by lazy {
        return@lazy raceOverview.minByOrNull { it.qualified ?: Int.MAX_VALUE }?.qualified
    }
    val bestQualifyingQuantity: Int by lazy {
        return@lazy raceOverview.count { it.finished == bestQualifying }
    }
    val podiums: Int by lazy {
        return@lazy raceOverview.count { it.finished <= 3 }
    }
    val races: Int by lazy {
        return@lazy raceOverview.size
    }
    val wins: Int by lazy {
        return@lazy raceOverview.count { it.finished == 1}
    }

    val qualifyingPoles: Int by lazy {
        return@lazy totalQualifyingIn(1)
    }
    val qualifyingTop3: Int by lazy {
        return@lazy totalQualifyingAbove(3)
    }
    val qualifyingFrontRow: Int by lazy {
        return@lazy totalQualifyingIn(1) + totalQualifyingIn(2)
    }
    val qualifyingSecondRow: Int by lazy {
        return@lazy totalQualifyingIn(3) + totalQualifyingIn(4)
    }

    val finishesInPoints: Int by lazy {
        return@lazy raceOverview
            .count { it.points > 0 }
    }
    val finishesInTop5: Int by lazy {
        return@lazy totalFinishesAbove(5)
    }

    val raceStarts: Int by lazy {
        return@lazy races
    }
    val raceFinishes: Int by lazy {
        return@lazy raceOverview.filter { it.status.isStatusFinished() }.size
    }
    val raceRetirements: Int by lazy {
        return@lazy raceOverview.filter { !it.status.isStatusFinished() }.size
    }

    fun totalFinishesIn(position: Int): Int {
        return raceOverview
            .count { it.finished == position }
    }

    fun totalFinishesAbove(position: Int): Int {
        return raceOverview
            .count {
                @Suppress("ConvertTwoComparisonsToRangeCheck")
                it.finished >= 1 && it.finished <= position
            }
    }
    fun totalQualifyingAbove(position: Int): Int {
        return raceOverview
            .count {
                @Suppress("ConvertTwoComparisonsToRangeCheck")
                it.qualified != null && it.qualified >= 1 && it.qualified <= position
            }
    }

    fun totalQualifyingIn(position: Int): Int {
        return raceOverview
            .count { it.qualified == position }
    }

    companion object
}