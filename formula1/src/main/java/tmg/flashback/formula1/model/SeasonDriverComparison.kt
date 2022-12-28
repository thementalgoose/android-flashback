package tmg.flashback.formula1.model

data class SeasonDriverComparison(
    val season: Int,
    val driver1: DriverConstructors,
    val driver1Stats: SeasonDriverComparisonDriver,
    val driver2: DriverConstructors,
    val driver2Stats: SeasonDriverComparisonDriver
) {
    companion object
}