package tmg.flashback.statistics.network.models.races

fun FastestLap.Companion.model(
    position: Int = 1,
    time: String = "time"
): FastestLap = FastestLap(
    position = position,
    time = time
)