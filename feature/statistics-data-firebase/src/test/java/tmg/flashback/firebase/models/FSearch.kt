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