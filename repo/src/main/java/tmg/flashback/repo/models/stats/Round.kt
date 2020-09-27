package tmg.flashback.repo.models.stats

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.repo.enums.RaceStatus

data class Round(
    val season: Int,
    val round: Int,
    val date: LocalDate,
    val time: LocalTime?,
    val name: String,
    val drivers: List<RoundDriver>,
    val constructors: List<Constructor>,
    val circuit: CircuitSummary,
    val q1: Map<String, RoundQualifyingResult>,
    val q2: Map<String, RoundQualifyingResult>,
    val q3: Map<String, RoundQualifyingResult>,
    val race: Map<String, RoundRaceResult>
) {
    fun driverOverview(driverId: String): RoundDriverOverview {
        return RoundDriverOverview(
            q1 = q1[driverId],
            q2 = q2[driverId],
            q3 = q3[driverId],
            race = race[driverId]
        )
    }

    val q1FastestLap: LapTime?
        get() = q1.fastest()
    val q2FastestLap: LapTime?
        get() = q2.fastest()
    val q3FastestLap: LapTime?
        get() = q3.fastest()

    val constructorStandings: List<RoundConstructorStandings>
        get() {
            val standings: MutableMap<String, Int> = mutableMapOf()
            for ((driverId, raceResult) in race) {
                var previousPoints = standings.getOrPut(raceResult.driver.constructor.id) { 0 }
                previousPoints += raceResult.points
                standings[raceResult.driver.constructor.id] = previousPoints
            }
            return constructors.map {
                RoundConstructorStandings(
                    standings.getOrElse(
                        it.id
                    ) { 0 }, it
                )
            }
        }

    val driverStandings: List<RoundDriverStandings>
        get() = race.map { (_, value) ->
            RoundDriverStandings(
                value.points,
                value.driver
            )
        }

    private fun Map<String, RoundQualifyingResult>.fastest(): LapTime? = this
        .map { it.value.time }
        .filter { it != null && !it.noTime && it.totalMillis != 0 }
        .minBy {
            it?.totalMillis ?: Int.MAX_VALUE
        }
}

data class RoundDriverOverview(
    val q1: RoundQualifyingResult?,
    val q2: RoundQualifyingResult?,
    val q3: RoundQualifyingResult?,
    val race: RoundRaceResult?
)

data class RoundQualifyingResult(
    val driver: RoundDriver,
    val time: LapTime?,
    val position: Int
)

data class RoundRaceResult(
    val driver: RoundDriver,
    val time: LapTime?,
    val points: Int,
    val grid: Int,
    val qualified: Int?,
    val finish: Int,
    val status: RaceStatus,
    val fastestLap: FastestLap?
)

/**
 * Get the number of races that have been completed (accurate to the nearest day)
 */
val List<Round>.completed: Int
    get() = this.count { it.date < LocalDate.now() }

/**
 * Get the number of races that have been upcoming (accurate to the nearest day)
 */
val List<Round>.upcoming: Int
    get() = this.count { it.date >= LocalDate.now() }

/**
 * Get the constructor standings for the season
 */
fun Season.constructorStandings(): ConstructorStandings {
    val returnMap: MutableMap<String, Triple<Constructor, MutableMap<String, Pair<Driver, Int>>, Int>> =
        mutableMapOf()

    this.rounds.forEach { round ->
        round.constructors.forEach {
            if (!returnMap.containsKey(it.id)) {
                returnMap[it.id] = Triple(it, mutableMapOf(), 0)
            }

            val (constructor, listOfDrivers, allPoints) = returnMap[it.id]!!

            val driversInRoundForConstructor = round.drivers
                .filter { roundDriver -> roundDriver.constructor.id == it.id }
                .map { it to (round.race[it.id]?.points ?: 0) }

            val points = driversInRoundForConstructor
                    .sumBy { driverInRound -> driverInRound.second }

            driversInRoundForConstructor.forEach { (roundDriver, points) ->
                if (listOfDrivers[roundDriver.id] == null) {
                    listOfDrivers[roundDriver.id] = Pair(roundDriver.toDriver(), 0)
                }

                val existing = listOfDrivers[roundDriver.id]!!

                listOfDrivers[roundDriver.id] = Pair(existing.first, existing.second + points)
            }

            returnMap[it.id] = Triple(constructor, listOfDrivers, allPoints + points)
        }
    }


    val penaltyPoints = this.constructorPenalties
            .filter { penalty -> penalty.season == this.season }

    penaltyPoints.forEach { penalty ->
        val existing = returnMap[penalty.constructor.id]
        if (existing != null) {
            returnMap[penalty.constructor.id] = Triple(existing.first, existing.second, existing.third + penalty.pointsDelta)
        }
    }

    return returnMap
}

/**
 * Get the maximum points that a team has scored in the season
 * (ie. Points that the constructors champion has scored)
 */
fun Map<String, Triple<Constructor, Map<String, Pair<Driver, Int>>, Int>>.maxConstructorPointsInSeason(): Int {
    return this.values.maxBy { it.third }?.third ?: 0
}

/**
 * Get the maximum points that a driver has scores in the season
 * (ie. Points that the drivers champion has scored)
 */
fun Map<String, Pair<RoundDriver, Int>>.maxDriverPointsInSeason(): Int {
    return this.values.maxBy { it.second }?.second ?: 0
}

/**
 * Get the driver standings for the season
 */
fun List<Round>.driverStandings(): DriverStandings {
    val returnMap: MutableMap<String, Pair<RoundDriver, Int>> = mutableMapOf()
    this.forEach { round ->
        round.drivers.forEach {
            if (!returnMap.containsKey(it.id)) {
                returnMap[it.id] = Pair(it, 0)
            }

            val (driver, points) = returnMap[it.id]!!
            returnMap[it.id] = Pair(driver, points + (round.race[it.id]?.points ?: 0))
        }
    }

    return returnMap
}

/**
 * Get all the points that drivers in a constructor has achieved
 */
fun Map<String, Pair<Driver, Int>>.allPoints(): Int = this.map { it.value.second }.sum()

/**
 * Get the best qualifying position for a given driver
 */
fun List<Round>.bestQualified(driverId: String): Int? {
    val round = this.minBy { it.race.get(driverId)?.qualified ?: Int.MAX_VALUE }
    return round?.race?.get(driverId)?.qualified
}

fun List<Round>.bestQualifyingResultFor(driverId: String): Pair<Int, List<Round>>? {
    val bestQualifyingPosition: Int = this.bestQualified(driverId) ?: return null
    val listOfCircuits = this
        .filter { it.race[driverId]?.qualified == bestQualifyingPosition }
    return Pair(bestQualifyingPosition, listOfCircuits)
}

/**
 * Get the best finishing position for a given driver
 */
fun List<Round>.bestFinish(driverId: String): Int? {
    val round = this.minBy { it.race.get(driverId)?.finish ?: Int.MAX_VALUE }
    return round?.race?.get(driverId)?.finish
}

fun List<Round>.bestRaceResultFor(driverId: String): Pair<Int, List<Round>>? {
    val bestQualifyingPosition: Int = this.bestFinish(driverId) ?: return null
    val listOfCircuits = this
        .filter { it.race[driverId]?.finish == bestQualifyingPosition }
    return Pair(bestQualifyingPosition, listOfCircuits)
}






