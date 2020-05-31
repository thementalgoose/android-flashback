package tmg.flashback.repo.models

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.repo.enums.RaceStatus

data class Round(
    val season: Int,
    val round: Int,
    val date: LocalDate,
    val time: LocalTime?,
    val name: String,
    val circuit: Circuit,
    val q1: Map<String, RoundQualifyingResult>,
    val q2: Map<String, RoundQualifyingResult>,
    val q3: Map<String, RoundQualifyingResult>,
    val race: Map<String, RoundRaceResult>
) {
    val drivers: List<RoundDriver>
        get() {
            return race.values.map { it.driver }
        }

    fun driverOverview(driverId: String): RoundDriverOverview {
        return RoundDriverOverview(
            q1 = q1[driverId],
            q2 = q2[driverId],
            q3 = q3[driverId],
            race = race.getValue(driverId)
        )
    }

    val q1FastestLap: LapTime?
        get() = q1.fastest()
    val q2FastestLap: LapTime?
        get() = q2.fastest()
    val q3FastestLap: LapTime?
        get() = q3.fastest()

    val constructor: List<Constructor>
        get() {
            return race.values
                .map { it.driver.constructor }
                .distinctBy { it.id }
        }

    val constructorStandings: List<ConstructorStandings>
        get() {
            val standings: MutableMap<String, Int> = mutableMapOf()
            for ((driverId, raceResult) in race) {
                var previousPoints = standings.getOrPut(raceResult.driver.constructor.id) { 0 }
                previousPoints += raceResult.points
                standings[raceResult.driver.constructor.id] = previousPoints
            }
            return constructor.map {
                ConstructorStandings(standings.getOrElse(it.id) { 0 }, it)
            }
        }

    val driverStandings: List<DriverStandings>
        get() {
            return race.map { (key, value) ->
                DriverStandings(value.points, value.driver)
            }
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
    val race: RoundRaceResult
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

fun List<Round>.standingsConstructor(): Map<Constructor, Int> {

}

fun List<Round>.standingsDriver(): Map<String, Pair<Driver, Int>> {
    val returnMap:
}