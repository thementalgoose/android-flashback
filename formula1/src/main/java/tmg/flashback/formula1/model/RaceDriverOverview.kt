package tmg.flashback.formula1.model

data class RaceDriverOverview(
    val driver: DriverConstructor,
    val q1: RaceQualifyingRoundDriver.Qualifying?,
    val q2: RaceQualifyingRoundDriver.Qualifying?,
    val q3: RaceQualifyingRoundDriver.Qualifying?,
    val qSprint: RaceQualifyingRoundDriver.SprintQualifying?,
    val race: RaceRaceResult?,
    val qualified: Int? = race?.qualified ?: qSprint?.finished ?: q3?.position ?: q2?.position ?: q1?.position
) {
    companion object
}