package tmg.flashback.formula1.model

fun DriverHistorySeasonRace.Companion.model(
    status: String = "status",
    finished: Int = 1,
    points: Double = 1.0,
    qualified: Int? = 1,
    gridPos: Int? = 1,
    constructor: Constructor? = Constructor.model(),
    raceInfo: RaceInfo = RaceInfo.model(),
): DriverHistorySeasonRace = DriverHistorySeasonRace(
    status = status,
    finished = finished,
    points = points,
    qualified = qualified,
    gridPos = gridPos,
    constructor = constructor,
    raceInfo = raceInfo
)