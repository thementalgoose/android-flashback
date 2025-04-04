package tmg.flashback.providers.model

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.formula1.model.ConstructorHistorySeason

fun ConstructorHistory.Companion.model(
    constructor: Constructor = Constructor.model(),
    standings: List<ConstructorHistorySeason> = listOf(
        ConstructorHistorySeason.model()
    )
): ConstructorHistory = ConstructorHistory(
    constructor = constructor,
    standings = standings
)