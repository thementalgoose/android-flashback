package tmg.flashback.statistics.network.models.races

fun SprintQualifyingResult.Companion.model(
    points: Double = 1.0,
    qualified: Int = 1,
    gridPos: Int? = 1,
    finished: Int = 1,
    status: String = "status",
    time: String = "1:02.005"
): SprintQualifyingResult = SprintQualifyingResult(
    points = points,
    qualified = qualified,
    gridPos = gridPos,
    finished = finished,
    status = status,
    time = time
)