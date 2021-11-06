package tmg.flashback.statistics.room.models.race

fun FastestLap.Companion.model(
    position: Int = 1,
    time: String = "time"
): FastestLap = FastestLap(
    position = position,
    time = time
)