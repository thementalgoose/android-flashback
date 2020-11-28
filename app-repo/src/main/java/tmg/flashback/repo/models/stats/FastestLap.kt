package tmg.flashback.repo.models.stats

data class FastestLap(
    val rank: Int,
    val lap: Int,
    val lapTime: LapTime
)