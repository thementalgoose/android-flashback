package tmg.flashback.data.models.stats

import org.threeten.bp.LocalDate
import tmg.flashback.data.enums.isStatusFinished

data class DriverOverview(
        val id: String,
        val firstName: String,
        val lastName: String,
        val code: String?,
        val number: Int,
        val wikiUrl: String,
        val photoUrl: String?,
        val dateOfBirth: LocalDate,
        val nationality: String,
        val nationalityISO: String,
        val standings: List<DriverOverviewStanding>
) {
    val championshipWins: Int by lazy {
        return@lazy standings
            .filter { !it.isInProgress }
            .count { it.championshipStanding == 1 }
    }

    val careerBestChampionship: Int? by lazy {
        return@lazy standings
                .filter { !it.isInProgress && it.championshipStanding > 0 }
                .map { it.championshipStanding }
                .minByOrNull { it }
    }

    val hasChampionshipCurrentlyInProgress: Boolean by lazy {
        return@lazy standings.any { it.isInProgress }
    }

    val careerWins: Int by lazy {
        return@lazy standings
                .sumOf { it.wins }
    }
    val careerPodiums: Int by lazy {
        return@lazy standings
                .sumOf { it.podiums }
    }
    val careerPoints: Double by lazy {
        return@lazy standings
                .sumOf { it.points }
    }
    val careerRaces: Int by lazy {
        return@lazy standings
                .sumOf { it.races }
    }

    val constructors: List<Pair<Int, SlimConstructor>> by lazy {
        return@lazy standings
                .sortedBy { it.season }
                .map { standings ->
                    standings.constructors.map {
                        Pair(standings.season, it)
                    }
                }
                .flatten()
    }

    val raceStarts: Int by lazy {
        return@lazy standings
                .map { it.raceStarts }
                .sum()
    }
    val raceFinishes: Int by lazy {
        return@lazy standings
                .map { it.raceFinishes }
                .sum()
    }
    val raceRetirements: Int by lazy {
        return@lazy standings
                .map { it.raceRetirements }
                .sum()
    }

    val careerBestFinish: Int by lazy {
        return@lazy standings.minByOrNull { it.bestFinish }?.bestFinish ?: -1
    }
    val careerBestQualifying: Int by lazy {
        return@lazy standings.minByOrNull { it.bestQualifying }?.bestQualifying ?: -1
    }

    val careerConstructorStanding: Int by lazy {
        return@lazy standings.minByOrNull { it.championshipStanding }?.championshipStanding ?: -1
    }

    val careerQualifyingPoles: Int by lazy {
        return@lazy totalQualifyingIn(1)
    }
    val careerQualifyingFrontRow: Int by lazy {
        return@lazy totalQualifyingIn(1) + totalQualifyingIn(2)
    }
    val careerQualifyingTop3: Int by lazy {
        return@lazy totalFinishesAbove(3)
    }
    val careerQualifyingSecondRow: Int by lazy {
        return@lazy totalQualifyingIn(3) + totalQualifyingIn(4)
    }
    val careerQualifyingTop10: Int by lazy {
        return@lazy totalFinishesAbove(10)
    }

    val careerFinishesInPoints: Int by lazy {
        return@lazy standings
                .map { it.raceOverview }
                .flatten()
                .count { it.points > 0 }
    }
    val careerFinishesTop5: Int by lazy {
        return@lazy totalFinishesAbove(5)
    }

    fun totalFinishesIn(position: Int): Int {
        return standings
                .map { it.raceOverview }
                .flatten()
                .count { it.finished == position }
    }

    fun totalFinishesAbove(position: Int): Int {
        return standings
                .map { it.raceOverview }
                .flatten()
                .count {
                    @Suppress("ConvertTwoComparisonsToRangeCheck")
                    it.finished >= 1 && it.finished <= position
                }
    }

    fun totalQualifyingAbove(position: Int): Int {
        return standings
                .map { it.raceOverview }
                .flatten()
                .count {
                    @Suppress("ConvertTwoComparisonsToRangeCheck")
                    it.qualified >= 1 && it.qualified <= position
                }
    }

    fun totalQualifyingIn(position: Int): Int {
        return standings
                .map { it.raceOverview }
                .flatten()
                .count { it.qualified == position }
    }

    fun isWorldChampionFor(season: Int): Boolean {
        return standings
            .filter { it.season == season }
            .filter { !it.isInProgress }
            .filter { it.championshipStanding == 1 }
            .count() > 0
    }
}

data class DriverOverviewStanding(
        val bestFinish: Int,
        val bestFinishQuantity: Int,
        val bestQualifying: Int,
        val bestQualifyingQuantity: Int,
        val championshipStanding: Int,
        val isInProgress: Boolean,
        val points: Double,
        val podiums: Int,
        val races: Int,
        val season: Int,
        val wins: Int,
        val constructors: List<SlimConstructor>,
        val raceOverview: List<DriverOverviewRace>
) {
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
                    it.qualified >= 1 && it.qualified <= position
                }
    }

    fun totalQualifyingIn(position: Int): Int {
        return raceOverview
                .count { it.qualified == position }
    }
}

data class DriverOverviewRace(
        val status: String,
        val finished: Int,
        val points: Double,
        val qualified: Int,
        val round: Int,
        val season: Int,
        val raceName: String,
        val date: LocalDate,
        val constructor: SlimConstructor?,
        val circuitName: String,
        val circuitId: String,
        val circuitNationality: String,
        val circuitNationalityISO: String
)