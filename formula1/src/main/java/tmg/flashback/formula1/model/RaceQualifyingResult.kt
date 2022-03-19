package tmg.flashback.formula1.model

data class RaceQualifyingResult(
    val driver: DriverConstructor,
    val lapTime: LapTime?,
    val position: Int
) {
    companion object
}