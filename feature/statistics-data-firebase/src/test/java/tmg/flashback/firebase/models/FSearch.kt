package tmg.flashback.firebase.models

internal fun FSearchDriver.Companion.model(
    drivers: Map<String, FSearchDriverModel>? = mapOf(
        "driverId" to FSearchDriverModel.model()
    )
): FSearchDriver = FSearchDriver(
    drivers = drivers
)

internal fun FSearchDriverModel.Companion.model(
    dob: String? = "1995-10-12",
    fname: String? = "firstName",
    image: String? = "imageUrl",
    nat: String? = "nationality",
    natISO: String? = "nationalityISO",
    sname: String? = "lastName",
    wikiUrl: String? = "wikiUrl"
): FSearchDriverModel = FSearchDriverModel(
    dob = dob,
    fname = fname,
    image = image,
    nat = nat,
    natISO = natISO,
    sname = sname,
    wikiUrl = wikiUrl
)

internal fun FSearchConstructor.Companion.model(
    constructors: Map<String, FSearchConstructorModel> = mapOf(
        "constructorId" to FSearchConstructorModel.model()
    )
): FSearchConstructor = FSearchConstructor(
    constructors = constructors
)

internal fun FSearchConstructorModel.Companion.model(
    name: String? = "constructorName",
    nat: String? = "nationality",
    natISO: String? = "nationalityISO",
    wikiUrl: String? = "wikiUrl",
    color: String? = "#ff0000"
): FSearchConstructorModel = FSearchConstructorModel(
    name = name,
    nat = nat,
    natISO = natISO,
    wikiUrl = wikiUrl,
    color = color
)

internal fun FSearchCircuit.Companion.model(
    circuits: Map<String, FSearchCircuitModel> = mapOf(
        "circuitId" to FSearchCircuitModel.model()
    )
): FSearchCircuit = FSearchCircuit(
    circuits = circuits
)

internal fun FSearchCircuitModel.Companion.model(
    country: String? = "country",
    countryISO: String? = "countryISO",
    location: FCircuitLocation? = FCircuitLocation.model(),
    loc: String? = "location",
    name: String? = "name",
    wikiUrl: String? = "wikiUrl"
): FSearchCircuitModel = FSearchCircuitModel(
    country = country,
    countryISO = countryISO,
    location = location,
    loc = loc,
    name = name,
    wikiUrl = wikiUrl,
)