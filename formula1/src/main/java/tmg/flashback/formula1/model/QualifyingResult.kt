package tmg.flashback.formula1.model

data class QualifyingResult(
    val driver: DriverConstructor,
    val lapTime: LapTime?,
    val position: Int
) {
    companion object
}