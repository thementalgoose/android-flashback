package tmg.flashback.data.models.stats

import android.graphics.Color
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

internal val mockLocalDateOfRound1: LocalDate = LocalDate.of(2020, 5, 10)
internal val mockLocalTimeOfRound1: LocalTime = LocalTime.of(14, 10)
internal val mockLocalDateOfRound2: LocalDate = LocalDate.of(2020, 5, 17)
internal val mockLocalTimeOfRound2: LocalTime = LocalTime.of(14, 10)
internal val mockRoundName: String = "Round name"

//region Circuits

internal val mockCircuitCharlie = tmg.flashback.formula1.model.CircuitSummary(
    id = "charlie",
    name = "circuit",
    wikiUrl = "https://www.wiki.com",
    locality = "Italian",
    country = "Italy",
    countryISO = "ITA",
    location = tmg.flashback.formula1.model.Location(51.1, 1.03)
)
internal val mockCircuitDelta = tmg.flashback.formula1.model.CircuitSummary(
    id = "delta",
    name = "circuit2",
    wikiUrl = "https://www.wiki.com",
    locality = "German",
    country = "Germany",
    countryISO = "DEU",
    location = tmg.flashback.formula1.model.Location(51.1, 1.03)
)

//endregion

//region Constructors

/**
 * MOCK Constructor data
 *
 * Because these objects are quite complicated, there are scenario test data that is setup and tested
 *   against
 */

internal val mockConstructorAlpha: tmg.flashback.formula1.model.Constructor =
    tmg.flashback.formula1.model.Constructor(
        id = "alpha",
        name = "alphaName",
        wikiUrl = "https://www.wiki.com",
        nationality = "British",
        nationalityISO = "GBR",
        color = Color.GREEN
    )
internal val mockConstructorBeta: tmg.flashback.formula1.model.Constructor =
    tmg.flashback.formula1.model.Constructor(
        id = "beta",
        name = "betaName",
        wikiUrl = "https://www.wiki.com",
        nationality = "German",
        nationalityISO = "DEU",
        color = Color.BLUE
    )

//endregion

//region Drivers

internal val MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1: tmg.flashback.formula1.model.DriverWithEmbeddedConstructor = tmg.flashback.formula1.model.DriverWithEmbeddedConstructor(
    id = "1",
    firstName = "name1",
    lastName = "last1",
    code = "#123123",
    number = 12,
    wikiUrl = "https://www.wiki.com",
    photoUrl = "https://www.images.com",
    dateOfBirth = LocalDate.of(1995, 1, 1),
    nationality = "British",
    nationalityISO = "GBR",
    constructors = mapOf(
        1 to mockConstructorAlpha,
        2 to mockConstructorAlpha
    ),
    startingConstructor = mockConstructorAlpha
)
internal val mockDriver2 = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.copy(
        id = "2",
        firstName = "name2",
        lastName = "name2",
        constructors = mapOf(
                1 to mockConstructorBeta,
                2 to mockConstructorBeta
        ),
        startingConstructor = mockConstructorBeta
)
internal val mockDriver3 = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.copy(
        id = "3",
        firstName = "name3",
        lastName = "name3",
        constructors = mapOf(
                1 to mockConstructorAlpha,
                2 to mockConstructorAlpha
        ),
        startingConstructor = mockConstructorAlpha
)
internal val mockDriver4 = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.copy(
        id = "4",
        firstName = "name4",
        lastName = "name4",
        constructors = mapOf(
                1 to mockConstructorBeta,
                2 to mockConstructorBeta
        ),
        startingConstructor = mockConstructorBeta
)

//endregion

//region Driver Overview

internal val mockDriverSlimConstructor = tmg.flashback.formula1.model.SlimConstructor(
    id = "slim",
    name = "slim",
    color = Color.CYAN
)
internal val mockDriverRound11 = tmg.flashback.formula1.model.DriverOverviewRace(
    status = "Finished",
    finished = 2,
    points = 18.0,
    qualified = 4,
    round = 1,
    season = 2019,
    raceName = "Race 1",
    date = LocalDate.now(),
    constructor = mockDriverSlimConstructor,
    circuitName = mockCircuitCharlie.name,
    circuitId = mockCircuitCharlie.id,
    circuitNationality = mockCircuitCharlie.country,
    circuitNationalityISO = mockCircuitCharlie.countryISO
)
internal val mockDriverRound12 = tmg.flashback.formula1.model.DriverOverviewRace(
    status = "Finished",
    finished = 4,
    points = 12.0,
    qualified = 3,
    round = 2,
    season = 2019,
    raceName = "Race 2",
    constructor = mockDriverSlimConstructor,
    date = LocalDate.now(),
    circuitName = mockCircuitCharlie.name,
    circuitId = mockCircuitCharlie.id,
    circuitNationality = mockCircuitCharlie.country,
    circuitNationalityISO = mockCircuitCharlie.countryISO
)
internal val mockDriverStanding1 = tmg.flashback.formula1.model.DriverOverviewStanding(
    bestFinish = 2,
    bestFinishQuantity = 1,
    bestQualifying = 3,
    bestQualifyingQuantity = 1,
    championshipStanding = 2,
    isInProgress = false,
    points = 30.0,
    podiums = 1,
    races = 3,
    season = 2019,
    wins = 0,
    constructors = listOf(
        mockDriverSlimConstructor
    ),
    raceOverview = listOf(
        mockDriverRound11,
        mockDriverRound12
    )
)
internal val mockDriverRound21 = tmg.flashback.formula1.model.DriverOverviewRace(
    status = "Finished",
    finished = 7,
    points = 4.0,
    qualified = 2,
    round = 1,
    season = 2020,
    raceName = "Race 1",
    date = LocalDate.now(),
    constructor = mockDriverSlimConstructor,
    circuitName = mockCircuitCharlie.name,
    circuitId = mockCircuitCharlie.id,
    circuitNationality = mockCircuitCharlie.country,
    circuitNationalityISO = mockCircuitCharlie.countryISO
)
internal val mockDriverRound22 = tmg.flashback.formula1.model.DriverOverviewRace(
    status = "Finished",
    finished = 12,
    points = 0.0,
    qualified = 15,
    round = 2,
    season = 2020,
    raceName = "Race 2",
    date = LocalDate.now(),
    constructor = mockDriverSlimConstructor,
    circuitName = mockCircuitDelta.name,
    circuitId = mockCircuitDelta.id,
    circuitNationality = mockCircuitDelta.country,
    circuitNationalityISO = mockCircuitDelta.countryISO
)
internal val mockDriverStanding2 = tmg.flashback.formula1.model.DriverOverviewStanding(
    bestFinish = 7,
    bestFinishQuantity = 1,
    bestQualifying = 2,
    bestQualifyingQuantity = 1,
    championshipStanding = 5,
    isInProgress = false,
    points = 4.0,
    podiums = 0,
    races = 2,
    season = 2019,
    wins = 0,
    constructors = listOf(
        mockDriverSlimConstructor
    ),
    raceOverview = listOf(
        mockDriverRound21,
        mockDriverRound22
    )
)

/**
 * championships: 0
 * careerBestChampionship: 2
 *
 */
internal val mockDriverOverview = tmg.flashback.formula1.model.DriverHistory(
    id = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id,
    firstName = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.firstName,
    lastName = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.lastName,
    code = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.code,
    number = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.number,
    wikiUrl = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.wikiUrl,
    photoUrl = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.photoUrl,
    dateOfBirth = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.dateOfBirth,
    nationality = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.nationality,
    nationalityISO = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.nationalityISO,
    standings = listOf(
        mockDriverStanding1,
        mockDriverStanding2
    )
)


//endregion

//region Constructor Overview

/**
 * Setup a mock constructor overview object, with
 * - Constructor doing two seasons
 * - Each season had two drivers
 */

internal val mockConstructorOverviewStanding1Driver1 =
    tmg.flashback.formula1.model.ConstructorOverviewDriverStanding(
        driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1),
        bestFinish = 2,
        bestQualifying = 3,
        points = 12.0,
        finishesInP1 = 0,
        finishesInP2 = 1,
        finishesInP3 = 0,
        finishesInPoints = 1,
        qualifyingP1 = 0,
        qualifyingP2 = 0,
        qualifyingP3 = 1,
        qualifyingTop10 = 2,
        races = 2,
        championshipStanding = 1
    )
internal val mockConstructorOverviewStanding1Driver2 =
    tmg.flashback.formula1.model.ConstructorOverviewDriverStanding(
        driver = mockDriver2.toConstructorDriver(1),
        bestFinish = 3,
        bestQualifying = 1,
        points = 10.0,
        finishesInP1 = 0,
        finishesInP2 = 0,
        finishesInP3 = 1,
        finishesInPoints = 2,
        qualifyingP1 = 1,
        qualifyingP2 = 0,
        qualifyingP3 = 1,
        qualifyingTop10 = 2,
        races = 2,
        championshipStanding = 2
    )
internal val mockConstructorOverviewStanding1 =
    tmg.flashback.formula1.model.ConstructorOverviewStanding(
        drivers = mapOf(
            MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to mockConstructorOverviewStanding1Driver1,
            mockDriver2.id to mockConstructorOverviewStanding1Driver2
        ),
        isInProgress = false,
        championshipStanding = 2,
        points = 22.0,
        season = 2019,
        races = 2
    )

internal val mockConstructorOverviewStanding2Driver1 =
    tmg.flashback.formula1.model.ConstructorOverviewDriverStanding(
        driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(2),
        bestFinish = 3,
        bestQualifying = 3,
        points = 12.0,
        finishesInP1 = 0,
        finishesInP2 = 0,
        finishesInP3 = 2,
        finishesInPoints = 1,
        qualifyingP1 = 0,
        qualifyingP2 = 0,
        qualifyingP3 = 1,
        qualifyingTop10 = 2,
        races = 2,
        championshipStanding = 1
    )
internal val mockConstructorOverviewStanding2Driver2 =
    tmg.flashback.formula1.model.ConstructorOverviewDriverStanding(
        driver = mockDriver2.toConstructorDriver(2),
        bestFinish = 4,
        bestQualifying = 1,
        points = 17.0,
        finishesInP1 = 0,
        finishesInP2 = 0,
        finishesInP3 = 0,
        finishesInPoints = 3,
        qualifyingP1 = 1,
        qualifyingP2 = 0,
        qualifyingP3 = 1,
        qualifyingTop10 = 2,
        races = 3,
        championshipStanding = 2
    )
internal val mockConstructorOverviewStanding2 =
    tmg.flashback.formula1.model.ConstructorOverviewStanding(
        drivers = mapOf(
            MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to mockConstructorOverviewStanding2Driver1,
            mockDriver2.id to mockConstructorOverviewStanding2Driver2
        ),
        isInProgress = false,
        championshipStanding = 1,
        points = 29.0,
        season = 2018,
        races = 3
    )

internal val mockConstructorOverview = tmg.flashback.formula1.model.ConstructorOverview(
    id = mockConstructorAlpha.id,
    name = mockConstructorAlpha.name,
    wikiUrl = mockConstructorAlpha.wikiUrl,
    nationality = mockConstructorAlpha.nationality,
    nationalityISO = mockConstructorAlpha.nationalityISO,
    color = mockConstructorAlpha.color,
    standings = listOf(
        mockConstructorOverviewStanding1,
        mockConstructorOverviewStanding2
    )
)

//endregion





























//region Round data

/**
 * #### ROUND 1
 *
 * Q1: 1,3,2,4
 * Q2: 2,1,3
 * Q3: 1,2
 *
 * Grid penalty for driver3.
 * Grid: 1,2,4,3
 *
 * Finish: 4,3,2,1(DNF)
 *
 *
 *
 *
 * #### ROUND 2
 *
 * Q1: 3,4,1,2
 * Q2: 4.1.3
 * Q3: 4.1
 *
 * Grid: 4,1,3,2
 *
 * Finish: 1,3,2,4
 */

internal val mockRound1Driver1Q1 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 1, 0),
    position = 1
)
internal val mockRound1Driver2Q1 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver2.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 3, 0),
    position = 3
)
internal val mockRound1Driver3Q1 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver3.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 2, 0),
    position = 2
)
internal val mockRound1Driver4Q1 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver4.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 4, 0),
    position = 4
)

internal val mockRound1Driver1Q2 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 2, 0),
    position = 2
)
internal val mockRound1Driver2Q2 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver2.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 1, 0),
    position = 1
)
internal val mockRound1Driver3Q2 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver3.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 3, 0),
    position = 3
)

internal val mockRound1Driver1Q3 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 1, 0),
    position = 1
)
internal val mockRound1Driver2Q3 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver2.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 2, 0),
    position = 2
)

internal val round1RaceResultDriver4 = tmg.flashback.formula1.model.RoundRaceResult(
    driver = mockDriver4.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(1, 0, 0, 0),
    points = 20.0,
    grid = 3,
    qualified = 4,
    finish = 1,
    status = "Finished",
    fastestLap = null
)
internal val round1RaceResultDriver3 = tmg.flashback.formula1.model.RoundRaceResult(
    driver = mockDriver3.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(1, 1, 0, 0),
    points = 15.0,
    grid = 4,
    qualified = 3,
    finish = 2,
    status = "Finished",
    fastestLap = null
)
internal val round1RaceResultDriver2 = tmg.flashback.formula1.model.RoundRaceResult(
    driver = mockDriver2.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(1, 2, 0, 0),
    points = 10.0,
    grid = 2,
    qualified = 2,
    finish = 3,
    status = "Finished",
    fastestLap = null
)
internal val round1RaceResultDriver1 = tmg.flashback.formula1.model.RoundRaceResult(
    driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1),
    time = tmg.flashback.formula1.model.LapTime(1, 0, 0, 0),
    points = 5.0,
    grid = 1,
    qualified = 1,
    finish = 4,
    status = "Engine",
    fastestLap = null
)

/**
 * Q1: 1,3,2,4
 * Q2: 2,1,3
 * Q3: 1,2
 *
 * Grid penalty for driver3.
 * Grid: 1,2,4,3
 *
 * Finish: 4,3,2,1(DNF)
 */
internal val mockRound1: tmg.flashback.formula1.model.Round = tmg.flashback.formula1.model.Round(
    season = 2019,
    round = 1,
    date = mockLocalDateOfRound1,
    time = mockLocalTimeOfRound1,
    name = "Round 1",
    wikipediaUrl = "http://www.wikiurl.com",
    drivers = listOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(1),
        mockDriver2.toConstructorDriver(1),
        mockDriver3.toConstructorDriver(1),
        mockDriver4.toConstructorDriver(1)
    ),
    constructors = listOf(mockConstructorAlpha, mockConstructorBeta),
    circuit = mockCircuitCharlie,
    q1 = mapOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to mockRound1Driver1Q1,
        mockDriver2.id to mockRound1Driver2Q1,
        mockDriver3.id to mockRound1Driver3Q1,
        mockDriver4.id to mockRound1Driver4Q1
    ),
    q2 = mapOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to mockRound1Driver1Q2,
        mockDriver2.id to mockRound1Driver2Q2,
        mockDriver3.id to mockRound1Driver3Q2
    ),
    q3 = mapOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to mockRound1Driver1Q3,
        mockDriver2.id to mockRound1Driver2Q3
    ),
    race = mapOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to round1RaceResultDriver1,
        mockDriver2.id to round1RaceResultDriver2,
        mockDriver3.id to round1RaceResultDriver3,
        mockDriver4.id to round1RaceResultDriver4
    ),
    qSprint = emptyMap()
)














internal val mockRound2Driver1Q1 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 3, 0),
    position = 3
)
internal val mockRound2Driver2Q1 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver2.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 4, 0),
    position = 4
)
internal val mockRound2Driver3Q1 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver3.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 1, 0),
    position = 1
)
internal val mockRound2Driver4Q1 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver4.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 2, 0),
    position = 2
)

internal val mockRound2Driver1Q2 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 2, 0),
    position = 2
)
internal val mockRound2Driver3Q2 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver3.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 3, 0),
    position = 3
)
internal val mockRound2Driver4Q2 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver4.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 1, 0),
    position = 1
)

internal val mockRound2Driver1Q3 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 2, 0),
    position = 4
)
internal val mockRound2Driver4Q3 = tmg.flashback.formula1.model.RoundQualifyingResult(
    driver = mockDriver4.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(0, 1, 1, 0),
    position = 1
)

internal val round2RaceResultDriver4 = tmg.flashback.formula1.model.RoundRaceResult(
    driver = mockDriver4.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(1, 0, 0, 0),
    points = 4.0,
    grid = 1,
    qualified = 1,
    finish = 4,
    status = "Finished",
    fastestLap = null
)
internal val round2RaceResultDriver3 = tmg.flashback.formula1.model.RoundRaceResult(
    driver = mockDriver3.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(1, 1, 0, 0),
    points = 12.0,
    grid = 3,
    qualified = 3,
    finish = 2,
    status = "Finished",
    fastestLap = null
)
internal val round2RaceResultDriver2 = tmg.flashback.formula1.model.RoundRaceResult(
    driver = mockDriver2.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(1, 2, 0, 0),
    points = 8.0,
    grid = 4,
    qualified = 4,
    finish = 3,
    status = "Finished",
    fastestLap = null
)
internal val round2RaceResultDriver1 = tmg.flashback.formula1.model.RoundRaceResult(
    driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(2),
    time = tmg.flashback.formula1.model.LapTime(1, 0, 0, 0),
    points = 16.0,
    grid = 2,
    qualified = 2,
    finish = 1,
    status = "Engine",
    fastestLap = null
)

/**
 * Q1: 3,4,1,2
 * Q2: 4.1.3
 * Q3: 4.1
 *
 * Grid: 4,1,3,2
 *
 * Finish: 1,3,2,4
 */
internal val mockRound2: tmg.flashback.formula1.model.Round = tmg.flashback.formula1.model.Round(
    season = 2019,
    round = 2,
    date = mockLocalDateOfRound2,
    time = mockLocalTimeOfRound2,
    name = "Round 2",
    wikipediaUrl = "http://www.wikiurl.com",
    drivers = listOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.toConstructorDriver(2),
        mockDriver2.toConstructorDriver(2),
        mockDriver3.toConstructorDriver(2),
        mockDriver4.toConstructorDriver(2)
    ),
    constructors = listOf(mockConstructorAlpha, mockConstructorBeta),
    circuit = mockCircuitDelta,
    q1 = mapOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to mockRound2Driver1Q1,
        mockDriver2.id to mockRound2Driver2Q1,
        mockDriver3.id to mockRound2Driver3Q1,
        mockDriver4.id to mockRound2Driver4Q1
    ),
    q2 = mapOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to mockRound2Driver1Q2,
        mockDriver3.id to mockRound2Driver3Q2,
        mockDriver4.id to mockRound2Driver4Q2
    ),
    q3 = mapOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to mockRound2Driver1Q3,
        mockDriver4.id to mockRound2Driver4Q3
    ),
    race = mapOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1.id to round2RaceResultDriver1,
        mockDriver2.id to round2RaceResultDriver2,
        mockDriver3.id to round2RaceResultDriver3,
        mockDriver4.id to round2RaceResultDriver4
    ),
    qSprint = emptyMap()
)

//endregion

























//region Season

internal val mockSeason = tmg.flashback.formula1.model.Season(
    season = 2019,
    driverWithEmbeddedConstructors = listOf(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_1, mockDriver2, mockDriver3, mockDriver4),
    constructors = listOf(mockConstructorAlpha, mockConstructorBeta),
    rounds = listOf(mockRound1, mockRound2),
    constructorStandings = emptyList(),
    driverStandings = emptyList()
)

//endregion

