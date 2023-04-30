package tmg.flashback.flashbackapi.api.models.constructors

fun ConstructorHistory.Companion.model(
    construct: Constructor = Constructor.model(),
    standings: Map<String, ConstructorHistoryStanding> = mapOf(

    )
): ConstructorHistory = ConstructorHistory(
    construct = construct,
    standings = standings
)