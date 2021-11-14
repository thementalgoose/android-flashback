package tmg.flashback.formula1.model

data class RaceQualifyingResult_Legacy(
    val driver: DriverConstructor,
    val time: LapTime?,
    val position: Int
) {
    companion object
}