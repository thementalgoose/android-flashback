package tmg.flashback.firebase.models

internal fun FDriverOverview.Companion.model(
    driver: FDriverOverviewDriver = FDriverOverviewDriver.model(),
    standings: Map<String, FDriverOverviewStanding>? = mapOf(
        "s2020" to FDriverOverviewStanding.model()
    )
): FDriverOverview = FDriverOverview(
    driver = driver,
    standings = standings
)

internal fun FDriverOverviewDriver.Companion.model(
    dob: String = "1995-10-12",
    driverCode: String? = "alb",
    driverNumber: String? = "23",
    firstName: String = "firstName",
    id: String = "driverId",
    nationality: String = "nationality",
    nationalityISO: String? = "nationalityISO",
    photoUrl: String? = "photoUrl",
    surname: String = "lastName",
    wikiUrl: String = "wikiUrl"
): FDriverOverviewDriver = FDriverOverviewDriver(
    dob = dob,
    driverCode = driverCode,
    driverNumber = driverNumber,
    firstName = firstName,
    id = id,
    nationality = nationality,
    nationalityISO = nationalityISO,
    photoUrl = photoUrl,
    surname = surname,
    wikiUrl = wikiUrl,
)

internal fun FDriverOverviewStanding.Companion.model(
    bestFinish: Int? = 1,
    bestFinishQuantity: Int? = 4,
    bestQualifying: Int? = 2,
    bestQualifyingQuantity: Int? = 5,
    championshipStanding: Int? = 1,
    constructor: List<FDriverOverviewStandingConstructor>? = listOf(
        FDriverOverviewStandingConstructor.model()
    ),
    history: Map<String, FDriverOverviewStandingHistory>? = mapOf(
        "s2020" to FDriverOverviewStandingHistory.model()
    ),
    inProgress: Boolean? = true,
    p: Int? = 25, // Points
    podiums: Int? = 1,
    races: Int? = 1,
    s: Int = 2020, // Season
    wins: Int? = 5
): FDriverOverviewStanding = FDriverOverviewStanding(
    bestFinish = bestFinish,
    bestFinishQuantity = bestFinishQuantity,
    bestQualifying = bestQualifying,
    bestQualifyingQuantity = bestQualifyingQuantity,
    championshipStanding = championshipStanding,
    constructor = constructor,
    history = history,
    inProgress = inProgress,
    p = p,
    podiums = podiums,
    races = races,
    s = s,
    wins = wins,
)

internal fun FDriverOverviewStandingHistory.Companion.model(
    cId: String = "circuitId", // Circuit Id
    cName: String = "circuitName", // Circuit Name
    cCountry: String = "circuitCountry", // Circuit Country
    cISO: String? = "circuitCountryISO", // Nationality iso
    con: String? = "constructorId", // Constructor id
    date: String = "2020-01-01",
    f: Int? = 1, // Finish
    fStatus: String? = "1",
    p: Int? = 25, // Points
    q: Int? = 1, // Qualifyied
    r: Int = 1, // Round
    rName: String? = "raceName"
): FDriverOverviewStandingHistory = FDriverOverviewStandingHistory(
    cId = cId,
    cName = cName,
    cCountry = cCountry,
    cISO = cISO, // Nationality iso
    con = con, // Constructor id
    date = date,
    f = f, // Finish
    fStatus = fStatus,
    p = p, // Points
    q = q, // Qualifyied
    r = r, // Round
    rName = rName,
)

internal fun FDriverOverviewStandingConstructor.Companion.model(
    color: String = "#888888",
    id: String = "constructorId",
    name: String = "constructorName"
): FDriverOverviewStandingConstructor = FDriverOverviewStandingConstructor(
    color = color,
    id = id,
    name = name
)