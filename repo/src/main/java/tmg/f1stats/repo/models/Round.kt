package tmg.f1stats.repo.models

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.f1stats.repo.enums.RaceStatus

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

    val constructor: List<Constructor>
        get() {
            return race.values
                .map { it.driver.constructor }
                .distinctBy { it.id }
        }
}

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
    val finish: Int,
    val status: RaceStatus,
    val fastestLap: FastestLap?
)