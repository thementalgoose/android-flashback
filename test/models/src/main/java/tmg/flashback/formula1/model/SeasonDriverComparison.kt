package tmg.flashback.formula1.model

fun SeasonDriverComparison.Companion.model(
    season: Int = 2020,
    driver1: DriverConstructors = DriverConstructors.model(),
    driver1Stats: SeasonDriverComparisonDriver = SeasonDriverComparisonDriver.model(),
    driver2: DriverConstructors = DriverConstructors.model(),
    driver2Stats: SeasonDriverComparisonDriver = SeasonDriverComparisonDriver.model()
): SeasonDriverComparison = SeasonDriverComparison(
    season = season,
    driver1 = driver1,
    driver1Stats = driver1Stats,
    driver2 = driver2,
    driver2Stats = driver2Stats,
)