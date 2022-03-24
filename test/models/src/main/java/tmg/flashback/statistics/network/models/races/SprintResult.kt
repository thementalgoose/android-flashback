package tmg.flashback.statistics.network.models.races

fun SprintResult.Companion.model(
    driverId: String = "driverId",
    driverNumber: String = "23",
    constructorId: String = "constructorId",
    points: Double = 1.0,
    gridPos: Int? = 1,
    finished: Int = 1,
    status: String = "status",
    time: String? = "1:02.005"
): SprintResult = SprintResult(
    driverId = driverId,
    driverNumber = driverNumber,
    constructorId = constructorId,
    points = points,
    gridPos = gridPos,
    finished = finished,
    status = status,
    time = time,
)