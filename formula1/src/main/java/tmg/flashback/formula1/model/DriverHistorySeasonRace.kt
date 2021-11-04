package tmg.flashback.formula1.model

data class DriverHistorySeasonRace(
    val status: String,
    val finished: Int,
    val points: Double,
    val qualified: Int?,
    val gridPos: Int?,
    val constructor: Constructor?,
    val raceInfo: RaceInfo
) {
    companion object
}