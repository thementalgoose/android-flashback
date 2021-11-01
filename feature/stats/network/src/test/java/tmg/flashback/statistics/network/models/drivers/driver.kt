package tmg.flashback.statistics.network.models.drivers

fun Driver.Companion.model(
    driver: DriverData = DriverData.model(),
    standings: Map<String, DriverStanding> = mapOf(
        "s2020r1" to DriverStanding.model()
    )
): Driver = Driver(
    driver = driver,
    standings = standings
)