package tmg.f1stats.repo.models

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class SeasonRound(
    val wikiUrl: String,
    val raceName: String,
    val date: LocalDate,
    val time: LocalTime,
    val season: Int,
    val round: Int,
    val raceKey: String,
    val drivers: List<DriverOnWeekend>,
    val circuit: Circuit,
    val constructors: List<Constructor>,
    val raceResults: List<RaceResult>,
    val fastestLaps: List<RaceResultFastestLap>,
    val q1Results: List<QualifyingResult>,
    val q2Results: List<QualifyingResult>,
    val q3Results: List<QualifyingResult>
)
