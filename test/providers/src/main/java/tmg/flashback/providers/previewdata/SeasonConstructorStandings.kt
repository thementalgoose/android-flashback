package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.formula1.model.SeasonConstructorStandings

internal fun SeasonConstructorStandings.Companion.model(
    standings: List<SeasonConstructorStandingSeason> = listOf(
        SeasonConstructorStandingSeason.model()
    )
): SeasonConstructorStandings = SeasonConstructorStandings(
    standings = standings
)