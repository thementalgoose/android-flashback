package tmg.f1stats.repo.models

data class QualifyingResult(
    val driver: DriverOnWeekend,
    val time: LapTime,
    val position: Int
)