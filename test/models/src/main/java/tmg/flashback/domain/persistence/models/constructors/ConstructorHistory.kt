package tmg.flashback.domain.persistence.models.constructors

fun ConstructorHistory.Companion.model(
    constructor: Constructor = Constructor.model(),
    seasons: List<ConstructorSeasonWithDrivers> = listOf(
        ConstructorSeasonWithDrivers.model()
    )
): ConstructorHistory = ConstructorHistory(
    constructor = constructor,
    seasons = seasons
)