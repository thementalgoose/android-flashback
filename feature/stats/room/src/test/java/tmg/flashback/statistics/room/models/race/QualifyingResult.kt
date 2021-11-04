package tmg.flashback.statistics.room.models.race

fun QualifyingResult.Companion.model(
    driverId: String = "driverId",
    season: Int = 2020,
    round: Int = 1,
    constructorId: String = "constructorId",
    qualified: Int? = 1,
    q1: String? = "q1",
    q2: String? = "q2",
    q3: String? = "q3",
    qSprint: QualifyingSprintResult? = QualifyingSprintResult.model()
): QualifyingResult = QualifyingResult(
    driverId = driverId,
    season = season,
    round = round,
    constructorId = constructorId,
    qualified = qualified,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    qSprint = qSprint
)