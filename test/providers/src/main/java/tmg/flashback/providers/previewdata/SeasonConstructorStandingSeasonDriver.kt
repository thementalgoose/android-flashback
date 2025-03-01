package tmg.flashback.providers.previewdata

import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.SeasonConstructorStandingSeasonDriver

internal fun SeasonConstructorStandingSeasonDriver.Companion.model(
    driver: Driver = Driver.model(),
    points: Double = 1.0
): SeasonConstructorStandingSeasonDriver = SeasonConstructorStandingSeasonDriver(
    driver = driver,
    points = points
)