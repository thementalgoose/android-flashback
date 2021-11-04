package tmg.flashback.statistics.network.models.races

fun QualifyingResult.Companion.model(
    driverId: String = "driverId",
    driverNumber: String? = "driverNumber",
    constructorId: String = "constructorId",
    points: Double? = 1.0,
    qualified: Int? = 1,
    q1: String? = "q1",
    q2: String? = "q2",
    q3: String? = "q3",
    qSprint: SprintQualifyingResult? = SprintQualifyingResult.model(),
): QualifyingResult = QualifyingResult(
    driverId = driverId,
    driverNumber = driverNumber,
    constructorId = constructorId,
    points = points,
    qualified = qualified,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    qSprint = qSprint
)