package tmg.flashback.formula1.model

data class RaceDriverOverview(
    val q1: RaceQualifyingResult?,
    val q2: RaceQualifyingResult?,
    val q3: RaceQualifyingResult?,
    val qSprint: RaceSprintQualifyingResult?,
    val race: RaceRaceResult?
)