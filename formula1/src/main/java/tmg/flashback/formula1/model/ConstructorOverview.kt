package tmg.flashback.formula1.model

import tmg.utilities.extensions.positive

data class ConstructorOverview(
    val id: String,
    val name: String,
    val wikiUrl: String,
    val nationality: String,
    val nationalityISO: String,
    val color: Int,
    val standings: List<ConstructorOverviewStanding>
) {
    val championshipWins: Int by lazy {
        return@lazy standings
            .filter { !it.isInProgress }
            .count { it.championshipStanding == 1 }
    }

    val bestChampionship: Int? by lazy {
        return@lazy standings
            .filter { !it.isInProgress && it.championshipStanding > 0 }
            .map { it.championshipStanding }
            .minByOrNull { it }
    }

    val hasChampionshipCurrentlyInProgress: Boolean by lazy {
        return@lazy standings.any { it.isInProgress }
    }

    val races: Int by lazy {
        return@lazy standings
            .filter { it.races >= 0 }
            .sumOf { it.races }
    }

    val raceEntries: Int by lazy {
        return@lazy standings
            .sumOf { standing ->
                standing.drivers
                    .values
                    .filter { it.races >= 0 }
                    .sumOf { it.races }
            }
    }

    val totalWins: Int by lazy {
        return@lazy standings
            .filter { it.wins >= 0 }
            .sumOf { it.wins }
    }
    val totalPodiums: Int by lazy {
        return@lazy standings
            .filter { it.podiums >= 0 }
            .sumOf { it.podiums }
    }
    val totalPoints: Double by lazy {
        return@lazy standings
            .filter { it.points >= 0 }
            .sumOf { it.points }
    }

    val bestFinish: Int by lazy {
        return@lazy standings
            .filter { it.bestFinish != null }
            .minByOrNull { it.bestFinish!! }?.bestFinish ?: -1
    }
    val bestQualifying: Int by lazy {
        return@lazy standings
            .filter { it.bestQualifying != null }
            .minByOrNull { it.bestQualifying!! }?.bestQualifying ?: -1
    }

    val totalQualifyingPoles: Int by lazy {
        return@lazy standings
            .map { it.qualifyingPole }
            .filterNotNull()
            .filter { it >= 0 }
            .sumOf { it }
    }
    val totalQualifyingFrontRow: Int by lazy {
        return@lazy standings
            .map { it.qualifyingFrontRow }
            .filterNotNull()
            .filter { it >= 0 }
            .sumOf { it }
    }
    val totalQualifyingTop3: Int by lazy {
        return@lazy standings
            .map { it.qualifyingTop3 }
            .filterNotNull()
            .filter { it >= 0 }
            .sumOf { it }
    }

    val finishesInPoints: Int by lazy {
        return@lazy standings
            .filter { it.finishInPoints >= 0 }
            .map { it.finishInPoints }
            .sumOf { it }
    }

    fun isWorldChampionFor(season: Int): Boolean {
        return standings
            .filter { it.season == season }
            .filter { !it.isInProgress }
            .filter { it.championshipStanding == 1 }
            .count() > 0
    }
}

data class ConstructorOverviewStanding(
    val drivers: Map<String, ConstructorOverviewDriverStanding>,
    val isInProgress: Boolean,
    val championshipStanding: Int,
    val points: Double,
    val season: Int,
    val races: Int
) {
    val bestFinish: Int? by lazy {
        return@lazy drivers.values
            .filter { it.bestFinish > 0 }
            .map { it.bestFinish }
            .minByOrNull { it }
    }

    val bestQualifying: Int? by lazy {
        return@lazy drivers.values
            .filter { it.bestQualifying > 0 }
            .map { it.bestQualifying }
            .minByOrNull { it }
    }

    val qualifyingPole: Int? by lazy {
        return@lazy drivers.values
            .filter { it.qualifyingP1 >= 0 }
            .sumOf { it.qualifyingP1 }
    }

    val qualifyingFrontRow: Int? by lazy {
        return@lazy drivers.values
            .sumOf { it.qualifyingP1.positive() + it.qualifyingP2.positive() }
    }

    val qualifyingTop3: Int? by lazy {
        return@lazy drivers.values
            .sumOf {
                it.qualifyingP1.positive() +
                        it.qualifyingP2.positive() +
                        it.qualifyingP3.positive()
            }
    }

    val driverPoints: Double? by lazy {
        return@lazy drivers.values
            .filter { it.points >= 0 }
            .sumOf { it.points }
    }

    val finishInPoints: Int by lazy {
        return@lazy drivers.values
            .filter { it.finishesInPoints >= 0 }
            .sumOf { it.finishesInPoints }
    }

    val wins: Int by lazy {
        return@lazy drivers.values
            .filter { it.finishesInP1 >= 0 }
            .sumOf { it.finishesInP1 }
    }

    val podiums: Int by lazy {
        return@lazy drivers.values
            .sumOf {
                it.finishesInP1.positive() +
                        it.finishesInP2.positive() +
                        it.finishesInP3.positive()
            }
    }
}

data class ConstructorOverviewDriverStanding(
    val driver: ConstructorDriver,
    val bestFinish: Int,
    val bestQualifying: Int,
    val points: Double,
    val finishesInP1: Int,
    val finishesInP2: Int,
    val finishesInP3: Int,
    val finishesInPoints: Int,
    val qualifyingP1: Int,
    val qualifyingP2: Int,
    val qualifyingP3: Int,
    val qualifyingTop10: Int?,
    val races: Int,
    val championshipStanding: Int
)