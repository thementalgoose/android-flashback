package tmg.flashback.data.persistence.models.race

fun FastestLap.Companion.model(
    position: Int = 1,
    time: String = "1:01.001"
): FastestLap = FastestLap(
    position = position,
    time = time
)