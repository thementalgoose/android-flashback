package tmg.flashback.formula1.model

data class RaceQualifyingResult(
    val driver: DriverConstructor,
    val time: LapTime?,
    val position: Int
)