package tmg.flashback.repo.models

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.repo.enums.RaceStatus

data class Round(
    val season: Int,
    val round: Int,
    val date: LocalDate,
    val time: LocalTime,
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

    val constructor: List<Constructor>
        get() {
            return race.values
                .map { it.driver.constructor }
                .distinctBy { it.id }
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
    val qualified: Int,
    val finish: Int,
    val status: RaceStatus,
    val fastestLap: FastestLap?
)