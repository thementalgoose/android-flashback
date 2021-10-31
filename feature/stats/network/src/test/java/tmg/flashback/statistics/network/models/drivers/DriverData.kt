package tmg.flashback.statistics.network.models.drivers

fun DriverData.Companion.model(
    id: String = "driverId",
    firstName: String = "firstName",
    lastName: String = "lastName",
    dob: String = "1995-10-12",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    photoUrl: String? = "photoUrl",
    wikiUrl: String? = "wikiUrl",
    code: String? = "code",
    permanentNumber: String? = "permanentNumber"
): DriverData = DriverData(
    id = id,
    firstName = firstName,
    lastName = lastName,
    dob = dob,
    nationality = nationality,
    nationalityISO = nationalityISO,
    photoUrl = photoUrl,
    wikiUrl = wikiUrl,
    code = code,
    permanentNumber = permanentNumber
)