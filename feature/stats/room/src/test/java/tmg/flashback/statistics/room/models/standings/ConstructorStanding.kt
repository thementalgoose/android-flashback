package tmg.flashback.statistics.room.models.standings

fun ConstructorStanding.Companion.model(
    constructorId: String = "constructorId",
    season: Int = 2020,
    points: Double = 1.0,
    position: Int? = 1,
    inProgress: Boolean = true,
    races: Int = 1,
): ConstructorStanding = ConstructorStanding(
    constructorId = constructorId,
    season = season,
    points = points,
    position = position,
    inProgress = inProgress,
    races = races
)