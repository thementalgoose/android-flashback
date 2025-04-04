package tmg.flashback.providers.model

import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.formula1.model.DriverHistorySeason

fun DriverHistory.Companion.model(
    driver: Driver = Driver.model(),
    standings: List<DriverHistorySeason> = listOf(
        DriverHistorySeason.model()
    )
): DriverHistory = DriverHistory(
    driver = driver,
    standings = standings
)