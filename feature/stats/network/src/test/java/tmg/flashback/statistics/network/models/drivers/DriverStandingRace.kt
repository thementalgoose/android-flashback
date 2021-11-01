package tmg.flashback.statistics.network.models.drivers

import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.constructors.model
import tmg.flashback.statistics.network.models.race.model
import tmg.flashback.statistics.network.models.races.RaceData

fun DriverStandingRace.Companion.model(
    construct: ConstructorData = ConstructorData.model(),
    race: RaceData = RaceData.model(),
    sprintQuali: Boolean? = false,
    qualified: Int = 3,
    gridPos: Int? = 2,
    finished: Int = 1,
    status: String = "status",
    points: Double = 1.0,
): DriverStandingRace = DriverStandingRace(
    construct = construct,
    race = race,
    sprintQuali = sprintQuali,
    qualified = qualified,
    gridPos = gridPos,
    finished = finished,
    status = status,
    points = points,
)