package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings

internal fun SeasonDriverStandings.Companion.model(
    standings: List<SeasonDriverStandingSeason> = listOf(
        SeasonDriverStandingSeason.model()
    )
): SeasonDriverStandings = SeasonDriverStandings(
    standings = standings
)