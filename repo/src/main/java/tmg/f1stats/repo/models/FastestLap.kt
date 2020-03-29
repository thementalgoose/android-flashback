package tmg.f1stats.repo.models

data class FastestLap(
    val rank: Int,
    val lap: Int,
    val lapTime: LapTime
)