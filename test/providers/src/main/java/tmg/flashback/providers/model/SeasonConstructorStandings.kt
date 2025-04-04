package tmg.flashback.providers.model

import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.formula1.model.SeasonConstructorStandings

fun SeasonConstructorStandings.Companion.model(
    standings: List<SeasonConstructorStandingSeason> = listOf(
        SeasonConstructorStandingSeason.model()
    )
): SeasonConstructorStandings = SeasonConstructorStandings(
    standings = standings
)