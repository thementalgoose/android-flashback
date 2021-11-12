package tmg.flashback.formula1.model

data class RaceDriverOverview(
    val driver: DriverConstructor,
    val q1: RaceQualifyingResult_Legacy?,
    val q2: RaceQualifyingResult_Legacy?,
    val q3: RaceQualifyingResult_Legacy?,
    val qSprint: RaceSprintQualifyingResult_Legacy?,
    val race: RaceRaceResult?
) {
    companion object
}