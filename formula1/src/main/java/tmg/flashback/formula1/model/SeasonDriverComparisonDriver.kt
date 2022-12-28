package tmg.flashback.formula1.model

data class SeasonDriverComparisonDriver(
    val finishes: Int,
    val qualifying: Int,
    val podiumCount: Int,
    val winCount: Int,
    val poleCount: Int,
    val bestFinishPosition: Int?,
    val bestQualifyingPosition: Int?,
    val dnfCount: Int,
) {
    companion object
}