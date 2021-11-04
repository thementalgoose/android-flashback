package tmg.flashback.statistics.network.models.races

fun RaceResult.Companion.model(
    driverId: String = "driverId",
    driverNumber: String = "driverNumber",
    constructorId: String = "constructorId",
    points: Double = 1.0,
    qualified: Int? = 1,
    gridPos: Int? = 1,
    finished: Int = 1,
    status: String = "status",
    time: String? = "time",
    fastestLap: FastestLap? = FastestLap.model(),
): RaceResult = RaceResult(
    driverId = driverId,
    driverNumber = driverNumber,
    constructorId = constructorId,
    points = points,
    qualified = qualified,
    gridPos = gridPos,
    finished = finished,
    status = status,
    time = time,
    fastestLap = fastestLap
)