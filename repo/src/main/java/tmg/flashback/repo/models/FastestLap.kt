package tmg.flashback.repo.models

data class FastestLap(
    val rank: Int,
    val lap: Int,
    val lapTime: LapTime
)