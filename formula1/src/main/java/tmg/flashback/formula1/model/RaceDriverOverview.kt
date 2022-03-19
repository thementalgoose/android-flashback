package tmg.flashback.formula1.model

data class RaceDriverOverview(
    val driver: DriverConstructor,
    val q1: RaceQualifyingResult?,
    val q2: RaceQualifyingResult?,
    val q3: RaceQualifyingResult?,
    val qSprint: RaceSprintResult?,
    val race: RaceRaceResult?,
    val qualified: Int? = race?.qualified ?: qSprint?.finish ?: q3?.position ?: q2?.position ?: q1?.position
) {
    companion object
}