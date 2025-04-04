package tmg.flashback.providers.model

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.SeasonDriverStandingSeasonConstructor

fun SeasonDriverStandingSeasonConstructor.Companion.model(
    constructor: Constructor = Constructor.model(),
    points: Double = 1.0
): SeasonDriverStandingSeasonConstructor = SeasonDriverStandingSeasonConstructor(
    constructor = constructor,
    points = points
)