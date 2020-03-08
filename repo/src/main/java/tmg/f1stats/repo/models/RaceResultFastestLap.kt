package tmg.f1stats.repo.models

data class RaceResultFastestLap(
    val lapNumber: String,
    val rank: String,
    val averageSpeed: String,
    val time: LapTime?,
    val driver: DriverOnWeekend
)