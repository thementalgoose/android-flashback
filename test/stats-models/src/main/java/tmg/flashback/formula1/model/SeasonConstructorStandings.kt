package tmg.flashback.formula1.model

fun SeasonConstructorStandings.Companion.model(
    constructor: Constructor = Constructor.model(),
    standings: List<SeasonConstructorStandingSeason> = listOf(
        SeasonConstructorStandingSeason.model()
    )
): SeasonConstructorStandings = SeasonConstructorStandings(
    constructor = constructor,
    standings = standings
)