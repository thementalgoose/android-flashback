package tmg.flashback.formula1.model

fun DriverConstructor.Companion.model(
    driver: Driver = Driver.model(),
    constructor: Constructor = Constructor.model()
): DriverConstructor = DriverConstructor(
    driver = driver,
    constructor = constructor
)