package tmg.flashback.providers.model

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.RaceConstructorStandings

fun RaceConstructorStandings.Companion.model(
    points: Double = 1.0,
    constructor: Constructor = Constructor.model()
): RaceConstructorStandings = RaceConstructorStandings(
    points = points,
    constructor = constructor
)