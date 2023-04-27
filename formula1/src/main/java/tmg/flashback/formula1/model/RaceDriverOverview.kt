package tmg.flashback.formula1.model

data class RaceDriverOverview(
    val driver: DriverConstructor,
    val q1: QualifyingResult?,
    val q2: QualifyingResult?,
    val q3: QualifyingResult?,
    val qSprint: SprintRaceResult?,
    val race: RaceResult?,
    val qualified: Int? = q3?.position ?: q2?.position ?: q1?.position,
    val officialQualifyingPosition: Int? = race?.qualified ?: qSprint?.finish ?: qualified,
) {
    companion object
}