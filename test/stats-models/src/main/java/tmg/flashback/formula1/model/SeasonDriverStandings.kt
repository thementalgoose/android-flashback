package tmg.flashback.formula1.model

fun SeasonDriverStandings.Companion.model(
    driver: Driver = Driver.model(),
    standings: List<SeasonDriverStandingSeason> = listOf(
        SeasonDriverStandingSeason.model()
    )
): SeasonDriverStandings = SeasonDriverStandings(
    driver = driver,
    standings = standings
)