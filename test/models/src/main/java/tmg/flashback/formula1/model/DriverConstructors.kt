package tmg.flashback.formula1.model

fun DriverConstructors.Companion.model(
    driver: Driver = Driver.model(),
    constructors: List<Constructor> = listOf(Constructor.model())
): DriverConstructors = DriverConstructors(
    driver = driver,
    constructors = constructors
)