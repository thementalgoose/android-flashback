package tmg.flashback.statistics.room.models.race

fun QualifyingSprintResult.Companion.model(
    points: Double = 1.0,
    gridPos: Int? = 1,
    finished: Int = 1,
    status: String = "status",
    time: String? = "1:02.005"
): QualifyingSprintResult = QualifyingSprintResult(
    points = points,
    gridPos = gridPos,
    finished = finished,
    status = status,
    time = time
)