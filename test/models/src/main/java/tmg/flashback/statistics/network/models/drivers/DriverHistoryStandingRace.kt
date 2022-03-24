package tmg.flashback.statistics.network.models.drivers

import tmg.flashback.statistics.network.models.constructors.Constructor
import tmg.flashback.statistics.network.models.constructors.model
import tmg.flashback.statistics.network.models.races.RaceData
import tmg.flashback.statistics.network.models.races.model

fun DriverHistoryStandingRace.Companion.model(
    construct: Constructor = Constructor.model(),
    race: RaceData = RaceData.model(),
    sprintQuali: Boolean? = true,
    qualified: Int = 1,
    gridPos: Int? = 1,
    finished: Int = 1,
    status: String = "status",
    points: Double = 1.0,
): DriverHistoryStandingRace = DriverHistoryStandingRace(
    construct = construct,
    race = race,
    sprintQuali = sprintQuali,
    qualified = qualified,
    gridPos = gridPos,
    finished = finished,
    status = status,
    points = points,
)