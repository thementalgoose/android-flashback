package tmg.flashback.formula1.model

import tmg.utilities.extensions.positive

data class ConstructorOverview(
    val constructor: Constructor,
    val standings: List<ConstructorOverviewStanding>
) {
    val championshipWins: Int by lazy {
        return@lazy standings
            .filter { !it.isInProgress }
            .count { it.championshipStanding == 1 }
    }

    val bestChampionship: Int? by lazy {
        return@lazy standings
            .filter { it.championshipStanding != null }
            .filter { !it.isInProgress && it.championshipStanding!! > 0 }
            .map { it.championshipStanding }
            .minByOrNull { it!! }
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

    val totalQualifyingPoles: Int by lazy {
        return@lazy standings
            .map { it.qualifyingPole }
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
    val championshipStanding: Int?,
    val points: Double,
    val season: Int,
    val races: Int
) {
    val qualifyingPole: Int? by lazy {
        return@lazy drivers.values
            .sumOf { it.polePosition }
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
            .sumOf { it.wins }
    }

    val podiums: Int by lazy {
        return@lazy drivers.values
            .sumOf { it.podiums }
    }
}

data class ConstructorOverviewDriverStanding(
    val driver: DriverConstructor,
    val points: Double,
    val wins: Int,
    val races: Int,
    val podiums: Int,
    val finishesInPoints: Int,
    val polePosition: Int,
    val championshipStanding: Int?
)