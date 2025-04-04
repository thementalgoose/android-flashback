package tmg.flashback.providers.model

import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings

fun SeasonDriverStandings.Companion.model(
    standings: List<SeasonDriverStandingSeason> = listOf(
        SeasonDriverStandingSeason.model()
    )
): SeasonDriverStandings = SeasonDriverStandings(
    standings = standings
)