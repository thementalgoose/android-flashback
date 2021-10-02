package tmg.flashback.firebase.models

internal fun FConstructorOverview.Companion.model(
    data: FConstructorOverviewData = FConstructorOverviewData.model(),
    drivers: Map<String, FConstructorOverviewDrivers>? = mapOf(
        "driverId" to FConstructorOverviewDrivers.model()
    ),
    standings: Map<String, FConstructorOverviewStandings>? = mapOf(
        "standing" to FConstructorOverviewStandings.model()
    )
): FConstructorOverview = FConstructorOverview(
    data = data,
    drivers = drivers,
    standings = standings,
)

internal fun FConstructorOverviewStandings.Companion.model(
    championshipStanding: Int? = 1,
    drivers: Map<String, FConstructorOverviewStandingsDriver>? = mapOf(
        "driverId" to FConstructorOverviewStandingsDriver.model()
    ),
    inProgress: Boolean? = false,
    p: Double? = 10.0, // Points
    races: Int? = 1,
    s: Int = 2020 // Season
): FConstructorOverviewStandings = FConstructorOverviewStandings(
    championshipStanding = championshipStanding,
    drivers = drivers,
    inProgress = inProgress,
    p = p,
    races = races,
    s = s,
)

internal fun FConstructorOverviewStandingsDriver.Companion.model(
    bF: Int? = 1, // Best Finish
    bQ: Int? = 1, // Best Qualifying
    p: Double? = 25.0, // Points
    p1: Int? = 1, // P1 finishes
    p2: Int? = 2, // P2 finishes
    p3: Int? = 3, // P3 finishes
    pF: Int? = 10, // Points finishes
    q1: Int? = 1, // Qualified P1
    q2: Int? = 2, // Qualified P2
    q3: Int? = 3, // Qualified P3
    q10: Int? = 10, // Qualified top 10      OPTIONAL
    races: Int? = 1,
    pos: Int? = 2 // Championship position
): FConstructorOverviewStandingsDriver = FConstructorOverviewStandingsDriver(
    bF = bF,
    bQ = bQ,
    p = p,
    p1 = p1,
    p2 = p2,
    p3 = p3,
    pF = pF,
    q1 = q1,
    q2 = q2,
    q3 = q3,
    q10 = q10,
    races = races,
    pos = pos,
)

internal fun FConstructorOverviewDrivers.Companion.model(
    dob: String = "1995-10-12",
    driverCode: String? = "ALB",
    driverNumber: String? = "23",
    firstName: String = "firstName",
    id: String = "driverId",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    photoUrl: String? = "photoUrl",
    surname: String = "lastName",
    wikiUrl: String = "wikiUrl"
): FConstructorOverviewDrivers = FConstructorOverviewDrivers(
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

internal fun FConstructorOverviewData.Companion.model(
    colour: String = "#ff0000",
    id: String = "constructorId",
    name: String = "constructorName",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    wikiUrl: String = "wikiUrl"
): FConstructorOverviewData = FConstructorOverviewData(
    colour = colour,
    id = id,
    name = name,
    nationality = nationality,
    nationalityISO = nationalityISO,
    wikiUrl = wikiUrl,
)