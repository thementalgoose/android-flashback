package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class Round(
    val season: Int,
    val round: Int,
    val date: LocalDate,
    val time: LocalTime?,
    val name: String,
    val wikipediaUrl: String?,
    val drivers: List<ConstructorDriver>,
    val constructors: List<Constructor>,
    val circuit: Circuit,
    val q1: Map<String, RoundQualifyingResult>,
    val q2: Map<String, RoundQualifyingResult>,
    val q3: Map<String, RoundQualifyingResult>,
    val qSprint: Map<String, RoundSprintQualifyingResult>,
    val race: Map<String, RoundRaceResult>,
) {
    fun driverOverview(driverId: String): RoundDriverOverview {
        return RoundDriverOverview(
                q1 = q1[driverId],
                q2 = q2[driverId],
                q3 = q3[driverId],
                qSprint = qSprint[driverId],
                race = race[driverId]
        )
    }

    val hasSprintQualifying: Boolean
        get() = qSprint.isNotEmpty()

    val q1FastestLap: LapTime?
        get() = q1.fastest()
    val q2FastestLap: LapTime?
        get() = q2.fastest()
    val q3FastestLap: LapTime?
        get() = q3.fastest()

    val constructorStandings: List<RoundConstructorStandings>
        get() {
            val standings: MutableMap<String, Double> = mutableMapOf()
            for ((_, raceResult) in race) {
                var previousPoints = standings.getOrPut(raceResult.driver.constructor.id) { 0.0 }
                previousPoints += raceResult.points
                standings[raceResult.driver.constructor.id] = previousPoints
            }
            return constructors.map {
                RoundConstructorStandings(
                        standings.getOrElse(
                                it.id
                        ) { 0.0 }, it
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
            .minByOrNull {
                it?.totalMillis ?: Int.MAX_VALUE
            }
}

data class RoundDriverOverview(
    val q1: RoundQualifyingResult?,
    val q2: RoundQualifyingResult?,
    val q3: RoundQualifyingResult?,
    val qSprint: RoundSprintQualifyingResult?,
    val race: RoundRaceResult?
)

data class RoundQualifyingResult(
    val driver: ConstructorDriver,
    val time: LapTime?,
    val position: Int
)

data class RoundSprintQualifyingResult(
    val driver: ConstructorDriver,
    val time: LapTime?,
    val points: Double,
    val grid: Int,
    val qualified: Int?,
    val finish: Int,
    val status: tmg.flashback.formula1.enums.RaceStatus,
)

data class RoundRaceResult(
    val driver: ConstructorDriver,
    val time: LapTime?,
    val points: Double,
    val grid: Int,
    val qualified: Int?,
    val finish: Int,
    val status: tmg.flashback.formula1.enums.RaceStatus,
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
 * Get the maximum points that a team has scored in the season
 * (ie. Points that the constructors champion has scored)
 */
fun Map<String, Triple<Constructor, Map<String, Pair<ConstructorDriver, Double>>, Double>>.maxConstructorPointsInSeason(): Double {
    return this.values.maxByOrNull { it.third }?.third ?: 0.0
}

/**
 * Get all the points that drivers in a constructor has achieved
 */
fun Map<String, Pair<DriverWithEmbeddedConstructor, Int>>.allPoints(): Int = this.map { it.value.second }.sum()






