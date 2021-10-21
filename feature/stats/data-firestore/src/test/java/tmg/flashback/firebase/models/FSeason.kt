package tmg.flashback.firebase.models

internal fun FSeason.Companion.model(
    drivers: Map<String, FSeasonOverviewDriver>? = mapOf(
        "driverId" to FSeasonOverviewDriver.model()
    ),
    constructors: Map<String, FSeasonOverviewConstructor>? = mapOf(
        "constructorId" to FSeasonOverviewConstructor.model()
    ),
    race: Map<String, FRound>? = mapOf(
        "r1" to FRound.model()
    ),
    standings: FSeasonStatistics? = FSeasonStatistics.model()
): FSeason = FSeason(
    drivers = drivers,
    constructors = constructors,
    race = race,
    standings = standings,
)

internal fun FSeasonStatistics.Companion.model(
    constructors: Map<String, FSeasonStatisticsPoints>? = mapOf(
        "constructorId" to FSeasonStatisticsPoints.model()
    ),
    drivers: Map<String, FSeasonStatisticsPoints>? = mapOf(
        "driverId" to FSeasonStatisticsPoints.model()
    )
): FSeasonStatistics = FSeasonStatistics(
    constructors = constructors,
    drivers = drivers
)

internal fun FSeasonStatisticsPoints.Companion.model(
    p: Double? = 1.0,
    pos: Int? = 2
): FSeasonStatisticsPoints = FSeasonStatisticsPoints(
    p = p,
    pos = pos
)

internal fun FSeasonOverviewDriver.Companion.model(
    id: String = "driverId",
    firstName: String = "firstName",
    lastName: String = "lastName",
    code: String? = "ALB",
    number: Int? = 23,
    wikiUrl: String = "wikiUrl",
    photoUrl: String? = "photoUrl",
    dob: String = "1995-10-12",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    constructorId: String = "constructorId"
): FSeasonOverviewDriver = FSeasonOverviewDriver(
    id = id,
    firstName = firstName,
    lastName = lastName,
    code = code,
    number = number,
    wikiUrl = wikiUrl,
    photoUrl = photoUrl,
    dob = dob,
    nationality = nationality,
    nationalityISO = nationalityISO,
    constructorId = constructorId,
)

internal fun FSeasonOverviewConstructor.Companion.model(
    id: String = "constructorId",
    name: String = "constructorName",
    wikiUrl: String = "wikiUrl",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    colour: String = "#ff0000"
): FSeasonOverviewConstructor = FSeasonOverviewConstructor(
    id = id,
    name = name,
    wikiUrl = wikiUrl,
    nationality = nationality,
    nationalityISO = nationalityISO,
    colour = colour,
)

internal fun FRound.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    name: String = "name",
    date: String = "2020-01-01",
    time: String? = "12:00:00",
    wiki: String? = "wikiUrl",
    driverCon: Map<String, String>? = mapOf(
        "driverId" to "constructorId"
    ),
    circuit: FSeasonOverviewRaceCircuit = FSeasonOverviewRaceCircuit.model(),
    qualifying: Map<String, FSeasonOverviewRaceQualifying>? = mapOf(
        "driverId" to FSeasonOverviewRaceQualifying.model()
    ),
    sprintQualifying: Map<String, FSeasonOverviewRaceSprintQualifying>? = emptyMap(),
    race: Map<String, FSeasonOverviewRaceRace>? = mapOf(
        "driverId" to FSeasonOverviewRaceRace.model()
    )
): FRound = FRound(
    season = season,
    round = round,
    name = name,
    date = date,
    time = time,
    wiki = wiki,
    driverCon = driverCon,
    circuit = circuit,
    qualifying = qualifying,
    sprintQualifying = sprintQualifying,
    race = race,
)

internal fun FSeasonOverviewRaceCircuit.Companion.model(
    id: String = "circuitId",
    name: String = "circuitName",
    wikiUrl: String = "wikiUrl",
    locality: String = "locality",
    country: String = "country",
    countryISO: String = "countryISO",
    location: FCircuitLocation = FCircuitLocation.model()
): FSeasonOverviewRaceCircuit = FSeasonOverviewRaceCircuit(
    id = id,
    name = name,
    wikiUrl = wikiUrl,
    locality = locality,
    country = country,
    countryISO = countryISO,
    location = location
)

internal fun FSeasonOverviewRaceQualifying.Companion.model(
    pos: Int? = 3,
    q1: String? = "1:03.123",
    q2: String? = "1:02.123",
    q3: String? = "1:01.123"
): FSeasonOverviewRaceQualifying = FSeasonOverviewRaceQualifying(
    pos = pos,
    q1 = q1,
    q2 = q2,
    q3 = q3,
)

internal fun FSeasonOverviewRaceSprintQualifying.Companion.model(
    result: Int? = 1,
    grid: Int? = 3,
    resultText: String = "2",
    qualified: Int? = 3,
    status: String? = "Finished",
    points: Double? = 15.0,
    time: String? = "26:23.123"
): FSeasonOverviewRaceSprintQualifying = FSeasonOverviewRaceSprintQualifying(
    result = result,
    grid = grid,
    resultText = resultText,
    qualified = qualified,
    status = status,
    points = points,
    time = time
)

internal fun FSeasonOverviewRaceRace.Companion.model(
    result: Int? = 1,
    grid: Int? = 2,
    resultText: String = "1",
    qualified: Int? = 2,
    status: String? = "Finished",
    points: Double? = 12.0,
    time: String? = "1:23:23.130",
    fastestLap: FSeasonOverviewRaceRaceFastestLap? = FSeasonOverviewRaceRaceFastestLap.model()
): FSeasonOverviewRaceRace = FSeasonOverviewRaceRace(
    result = result,
    grid = grid,
    resultText = resultText,
    qualified = qualified,
    status = status,
    points = points,
    time = time,
    fastestLap = fastestLap,
)

internal fun FSeasonOverviewRaceRaceFastestLap.Companion.model(
    pos: Int = 1,
    lap: Int = 34,
    time: String = "1:43.203"
): FSeasonOverviewRaceRaceFastestLap = FSeasonOverviewRaceRaceFastestLap(
    pos = pos,
    lap = lap,
    time = time,
)