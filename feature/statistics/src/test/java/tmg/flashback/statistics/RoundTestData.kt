package tmg.flashback.statistics

import android.graphics.Color
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.data.models.stats.*
import tmg.utilities.extensions.hexColor


//region Constructors

internal val mockRoundName: String = "Round name"

internal val mockConstructorAlpha: Constructor = Constructor(
    id = "alpha",
    name = "alphaName",
    wikiUrl = "https://www.wiki.com",
    nationality = "British",
    nationalityISO = "GBR",
    color = Color.GREEN
)
internal val mockConstructorBeta: Constructor = Constructor(
    id = "beta",
    name = "betaName",
    wikiUrl = "https://www.wiki.com",
    nationality = "German",
    nationalityISO = "DEU",
    color = Color.BLUE
)

//endregion









//region Drivers

internal val mockDriver1: RoundDriver = RoundDriver(
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
    constructor = mockConstructorAlpha,
    constructorAtEndOfSeason = mockConstructorAlpha
)
internal val mockDriver2 = mockDriver1.copy(
    id = "2",
    firstName = "name2",
    lastName = "name2",
    constructor = mockConstructorBeta,
    constructorAtEndOfSeason = mockConstructorBeta
)
internal val mockDriver3 = mockDriver1.copy(
    id = "3",
    firstName = "name3",
    lastName = "name3",
    constructor = mockConstructorAlpha,
    constructorAtEndOfSeason = mockConstructorAlpha
)
internal val mockDriver4 = mockDriver1.copy(
    id = "4",
    firstName = "name4",
    lastName = "name4",
    constructor = mockConstructorBeta,
    constructorAtEndOfSeason = mockConstructorBeta
)
//endregion








//region Circuits

internal val mockCircuitCharlie = CircuitSummary(
    id = "charlie",
    name = "circuit",
    wikiUrl = "https://www.wiki.com",
    locality = "Italian",
    country = "Italy",
    countryISO = "ITA",
    locationLat = 51.1,
    locationLng = 1.03
)
internal val mockCircuitDelta = CircuitSummary(
    id = "delta",
    name = "circuit2",
    wikiUrl = "https://www.wiki.com",
    locality = "German",
    country = "Germany",
    countryISO = "DEU",
    locationLat = 51.1,
    locationLng = 1.03
)

//endregion

















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
 * Points: 20,15,10,5
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
 *
 * Points: 16,12,8,4
 *
 *
 *
 * #### Results
 *
 * Drivers
 * - Driver3 - 27     1st   A
 * - Driver4 - 24     2nd   B
 * - Driver1 - 21     3rd   A
 * - Driver2 - 18     4th   B
 *
 * Constructors
 * - Alpha   - 48     1st
 * - Beta    - 42     2nd
 */

internal val mockLocalDateOfRound1: LocalDate = LocalDate.of(2020, 5, 10)
internal val mockLocalTimeOfRound1: LocalTime = LocalTime.of(14, 10)
internal val mockLocalDateOfRound2: LocalDate = LocalDate.of(2020, 5, 17)
internal val mockLocalTimeOfRound2: LocalTime = LocalTime.of(14, 10)
// Only added when testing partial data!
internal val mockLocalDateOfRound3: LocalDate = LocalDate.of(2020, 5, 24)
internal val mockLocalTimeOfRound3: LocalTime = LocalTime.of(14, 10)

//region Round 1

internal val mockRound1Driver1Q1 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 1, 0), position = 1)
internal val mockRound1Driver2Q1 = RoundQualifyingResult(driver = mockDriver2, time = LapTime(0,1, 3, 0), position = 3)
internal val mockRound1Driver3Q1 = RoundQualifyingResult(driver = mockDriver3, time = LapTime(0,1, 2, 0), position = 2)
internal val mockRound1Driver4Q1 = RoundQualifyingResult(driver = mockDriver4, time = LapTime(0,1, 4, 0), position = 4)

internal val mockRound1Driver1Q2 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 2, 0), position = 2)
internal val mockRound1Driver2Q2 = RoundQualifyingResult(driver = mockDriver2, time = LapTime(0,1, 1, 0), position = 1)
internal val mockRound1Driver3Q2 = RoundQualifyingResult(driver = mockDriver3, time = LapTime(0,1, 3, 0), position = 3)

internal val mockRound1Driver1Q3 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 1, 0), position = 1)
internal val mockRound1Driver2Q3 = RoundQualifyingResult(driver = mockDriver2, time = LapTime(0,1, 2, 0), position = 2)

internal val round1RaceResultDriver1 = RoundRaceResult(
    driver = mockDriver1,
    time = LapTime(1,0,0,0),
    points = 5,
    grid = 1,
    qualified = 1,
    finish = 4,
    status = "Engine",
    fastestLap = null
)
internal val round1RaceResultDriver2 = RoundRaceResult(
    driver = mockDriver2,
    time = LapTime(1,2,0,0),
    points = 10,
    grid = 2,
    qualified = 2,
    finish = 3,
    status = "Finished",
    fastestLap = null
)
internal val round1RaceResultDriver3 = RoundRaceResult(
    driver = mockDriver3,
    time = LapTime(1,1,0,0),
    points = 15,
    grid = 4,
    qualified = 3,
    finish = 2,
    status = "Finished",
    fastestLap = null
)
internal val round1RaceResultDriver4 = RoundRaceResult(
    driver = mockDriver4,
    time = LapTime(1,0,0,0),
    points = 20,
    grid = 3,
    qualified = 4,
    finish = 1,
    status = "Finished",
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
internal val mockRound1: Round = Round(
    season = 2019,
    round = 1,
    date = mockLocalDateOfRound1,
    time = mockLocalTimeOfRound1,
    name = "Round 1",
    drivers = listOf(mockDriver1, mockDriver2, mockDriver3, mockDriver4),
    constructors = listOf(mockConstructorAlpha, mockConstructorBeta),
    circuit = mockCircuitCharlie,
    wikipediaUrl = "http://www.mockwikiurl.com",
    q1 = mapOf(
        mockDriver1.id to mockRound1Driver1Q1,
        mockDriver2.id to mockRound1Driver2Q1,
        mockDriver3.id to mockRound1Driver3Q1,
        mockDriver4.id to mockRound1Driver4Q1
    ),
    q2 = mapOf(
        mockDriver1.id to mockRound1Driver1Q2,
        mockDriver2.id to mockRound1Driver2Q2,
        mockDriver3.id to mockRound1Driver3Q2
    ),
    q3 = mapOf(
        mockDriver1.id to mockRound1Driver1Q3,
        mockDriver2.id to mockRound1Driver2Q3
    ),
    race = mapOf(
        mockDriver1.id to round1RaceResultDriver1,
        mockDriver2.id to round1RaceResultDriver2,
        mockDriver3.id to round1RaceResultDriver3,
        mockDriver4.id to round1RaceResultDriver4
    ),
    qSprint = emptyMap()
)

//endregion

//region Round 2

internal val mockRound2Driver1Q1 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 3, 0), position = 3)
internal val mockRound2Driver2Q1 = RoundQualifyingResult(driver = mockDriver2, time = LapTime(0,1, 4, 0), position = 4)
internal val mockRound2Driver3Q1 = RoundQualifyingResult(driver = mockDriver3, time = LapTime(0,1, 1, 0), position = 1)
internal val mockRound2Driver4Q1 = RoundQualifyingResult(driver = mockDriver4, time = LapTime(0,1, 2, 0), position = 2)

internal val mockRound2Driver1Q2 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 2, 0), position = 2)
internal val mockRound2Driver3Q2 = RoundQualifyingResult(driver = mockDriver3, time = LapTime(0,1, 3, 0), position = 3)
internal val mockRound2Driver4Q2 = RoundQualifyingResult(driver = mockDriver4, time = LapTime(0,1, 1, 0), position = 1)

internal val mockRound2Driver1Q3 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 2, 0), position = 4)
internal val mockRound2Driver4Q3 = RoundQualifyingResult(driver = mockDriver4, time = LapTime(0,1, 1, 0), position = 1)

internal val round2RaceResultDriver1 = RoundRaceResult(
    driver = mockDriver1,
    time = LapTime(1,0,0,0),
    points = 16,
    grid = 2,
    qualified = 2,
    finish = 1,
    status = "Engine",
    fastestLap = null
)
internal val round2RaceResultDriver2 = RoundRaceResult(
    driver = mockDriver2,
    time = LapTime(1,2,0,0),
    points = 8,
    grid = 4,
    qualified = 4,
    finish = 3,
    status = "Finished",
    fastestLap = null
)
internal val round2RaceResultDriver3 = RoundRaceResult(
    driver = mockDriver3,
    time = LapTime(1,1,0,0),
    points = 12,
    grid = 3,
    qualified = 3,
    finish = 2,
    status = "Finished",
    fastestLap = null
)
internal val round2RaceResultDriver4 = RoundRaceResult(
    driver = mockDriver4,
    time = LapTime(1,0,0,0),
    points = 4,
    grid = 1,
    qualified = 1,
    finish = 4,
    status = "Finished",
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
internal val mockRound2: Round = Round(
    season = 2019,
    round = 2,
    date = mockLocalDateOfRound2,
    time = mockLocalTimeOfRound2,
    name = "Round 2",
    wikipediaUrl = "http://www.mockwikiurl.com",
    drivers = listOf(mockDriver1, mockDriver2, mockDriver3, mockDriver4),
    constructors = listOf(mockConstructorAlpha, mockConstructorBeta),
    circuit = mockCircuitDelta,
    q1 = mapOf(
        mockDriver1.id to mockRound2Driver1Q1,
        mockDriver2.id to mockRound2Driver2Q1,
        mockDriver3.id to mockRound2Driver3Q1,
        mockDriver4.id to mockRound2Driver4Q1
    ),
    q2 = mapOf(
        mockDriver1.id to mockRound2Driver1Q2,
        mockDriver3.id to mockRound2Driver3Q2,
        mockDriver4.id to mockRound2Driver4Q2
    ),
    q3 = mapOf(
        mockDriver1.id to mockRound2Driver1Q3,
        mockDriver4.id to mockRound2Driver4Q3
    ),
    race = mapOf(
        mockDriver1.id to round2RaceResultDriver1,
        mockDriver2.id to round2RaceResultDriver2,
        mockDriver3.id to round2RaceResultDriver3,
        mockDriver4.id to round2RaceResultDriver4
    ),
    qSprint = emptyMap()
)

//endregion

//region Round 3 (Single Qualifying)

internal val mockRound3 = mockRound1.copy(
    round = 3,
    name = "Round 3",
    q2 = emptyMap(),
    q3 = emptyMap()
)

//endregion











//region Season

internal val mockSeason = Season(
    season = 2019,
    drivers = listOf(mockDriver1.toDriver(), mockDriver2.toDriver(), mockDriver3.toDriver(), mockDriver4.toDriver()),
    constructors = listOf(mockConstructorAlpha, mockConstructorBeta),
    rounds = listOf(mockRound1, mockRound2),
    driverStandings = emptyList(),
    constructorStandings = emptyList()
)

//endregion







//region History

internal val mockHistoryRound1 = HistoryRound(
    date = mockLocalDateOfRound1,
    season = 2019,
    round = 1,
    raceName = "Round 1",
    circuitId = mockCircuitCharlie.id,
    circuitName = mockCircuitCharlie.name,
    country = mockCircuitCharlie.country,
    countryISO = mockCircuitCharlie.countryISO,
    hasQualifying = true,
    hasResults = true
)

internal val mockHistoryRound2 = HistoryRound(
    date = mockLocalDateOfRound2,
    season = 2019,
    round = 2,
    raceName = "Round 2",
    circuitId = mockCircuitDelta.id,
    circuitName = mockCircuitDelta.name,
    country = mockCircuitDelta.country,
    countryISO = mockCircuitDelta.countryISO,
    hasQualifying = true,
    hasResults = true
)
internal val mockHistoryRound3 = mockHistoryRound2.copy(
    season = 2019,
    round = 3,
    raceName = "Round 3"
)

internal val mockHistory = History(
    2019,
    winner = WinnerSeason(
        season = 2019,
        driver = listOf(
            WinnerSeasonDriver(mockDriver3.id, mockDriver3.name, mockDriver3.photoUrl ?: "", 27),
            WinnerSeasonDriver(mockDriver4.id, mockDriver4.name, mockDriver4.photoUrl ?: "", 24),
            WinnerSeasonDriver(mockDriver1.id, mockDriver1.name, mockDriver1.photoUrl ?: "", 21),
            WinnerSeasonDriver(mockDriver2.id, mockDriver2.name, mockDriver2.photoUrl ?: "", 18)
        ),
        constructor = listOf(
            WinnerSeasonConstructor(mockConstructorAlpha.id, mockConstructorAlpha.name, mockConstructorAlpha.color.hexColor, 48),
            WinnerSeasonConstructor(mockConstructorBeta.id, mockConstructorBeta.name, mockConstructorBeta.color.hexColor, 42)
        )
    ),
    rounds = listOf(
        mockHistoryRound1,
        mockHistoryRound2
    )
)

//endregion