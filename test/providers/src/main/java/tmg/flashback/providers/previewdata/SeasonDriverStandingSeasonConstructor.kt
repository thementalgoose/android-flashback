package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.SeasonDriverStandingSeasonConstructor

internal fun SeasonDriverStandingSeasonConstructor.Companion.model(
    constructor: Constructor = Constructor.model(),
    points: Double = 1.0
): SeasonDriverStandingSeasonConstructor = SeasonDriverStandingSeasonConstructor(
    constructor = constructor,
    points = points
)