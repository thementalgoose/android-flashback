package tmg.flashback.repo.models.stats

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import java.awt.Color

val mockRoundName: String = "Round name"
val mockLocalDateOfRound1: LocalDate = LocalDate.of(2020, 5, 10)
val mockLocalTimeOfRound1: LocalTime = LocalTime.of(14, 10)
val mockLocalDateOfRound2: LocalDate = LocalDate.of(2020, 5, 17)
val mockLocalTimeOfRound2: LocalTime = LocalTime.of(14, 10)

val mockConstructorAlpha: Constructor = Constructor(
        id = "alpha",
        name = "alphaName",
        wikiUrl = "https://www.wiki.com",
        nationality = "British",
        nationalityISO = "GBR",
        color = Color.GREEN.rgb
)
val mockConstructorBeta: Constructor = Constructor(
        id = "beta",
        name = "betaName",
        wikiUrl = "https://www.wiki.com",
        nationality = "German",
        nationalityISO = "DEU",
        color = Color.BLUE.rgb
)











val mockDriver1: RoundDriver = RoundDriver(
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
val mockDriver2 = mockDriver1.copy(
        id = "2",
        firstName = "name2",
        lastName = "name2",
        constructor = mockConstructorBeta,
        constructorAtEndOfSeason = mockConstructorBeta
)
val mockDriver3 = mockDriver1.copy(
        id = "3",
        firstName = "name3",
        lastName = "name3",
        constructor = mockConstructorAlpha,
        constructorAtEndOfSeason = mockConstructorAlpha
)
val mockDriver4 = mockDriver1.copy(
        id = "4",
        firstName = "name4",
        lastName = "name4",
        constructor = mockConstructorBeta,
        constructorAtEndOfSeason = mockConstructorBeta
)











val mockCircuitCharlie = CircuitSummary(
        id = "charlie",
        name = "circuit",
        wikiUrl = "https://www.wiki.com",
        locality = "Italian",
        country = "Italy",
        countryISO = "ITA",
        locationLat = 51.1,
        locationLng = 1.03
)
val mockCircuitDelta = CircuitSummary(
        id = "delta",
        name = "circuit2",
        wikiUrl = "https://www.wiki.com",
        locality = "German",
        country = "Germany",
        countryISO = "DEU",
        locationLat = 51.1,
        locationLng = 1.03
)










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

val mockRound1Driver1Q1 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 1, 0), position = 1)
val mockRound1Driver2Q1 = RoundQualifyingResult(driver = mockDriver2, time = LapTime(0,1, 3, 0), position = 3)
val mockRound1Driver3Q1 = RoundQualifyingResult(driver = mockDriver3, time = LapTime(0,1, 2, 0), position = 2)
val mockRound1Driver4Q1 = RoundQualifyingResult(driver = mockDriver4, time = LapTime(0,1, 4, 0), position = 4)

val mockRound1Driver1Q2 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 2, 0), position = 2)
val mockRound1Driver2Q2 = RoundQualifyingResult(driver = mockDriver2, time = LapTime(0,1, 1, 0), position = 1)
val mockRound1Driver3Q2 = RoundQualifyingResult(driver = mockDriver3, time = LapTime(0,1, 3, 0), position = 3)

val mockRound1Driver1Q3 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 1, 0), position = 1)
val mockRound1Driver2Q3 = RoundQualifyingResult(driver = mockDriver2, time = LapTime(0,1, 2, 0), position = 2)

val round1RaceResultDriver4 = RoundRaceResult(
        driver = mockDriver4,
        time = LapTime(1,0,0,0),
        points = 20,
        grid = 3,
        qualified = 4,
        finish = 1,
        status = "Finished",
        fastestLap = null
)
val round1RaceResultDriver3 = RoundRaceResult(
        driver = mockDriver3,
        time = LapTime(1,1,0,0),
        points = 15,
        grid = 4,
        qualified = 3,
        finish = 2,
        status = "Finished",
        fastestLap = null
)
val round1RaceResultDriver2 = RoundRaceResult(
        driver = mockDriver2,
        time = LapTime(1,2,0,0),
        points = 10,
        grid = 2,
        qualified = 2,
        finish = 3,
        status = "Finished",
        fastestLap = null
)
val round1RaceResultDriver1 = RoundRaceResult(
        driver = mockDriver1,
        time = LapTime(1,0,0,0),
        points = 5,
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
val mockRound1: Round = Round(
        season = 2019,
        round = 1,
        date = mockLocalDateOfRound1,
        time = mockLocalTimeOfRound1,
        name = "Round 1",
        drivers = listOf(mockDriver1, mockDriver2, mockDriver3, mockDriver4),
        constructors = listOf(mockConstructorAlpha, mockConstructorBeta),
        circuit = mockCircuitCharlie,
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
        )
)














val mockRound2Driver1Q1 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 3, 0), position = 3)
val mockRound2Driver2Q1 = RoundQualifyingResult(driver = mockDriver2, time = LapTime(0,1, 4, 0), position = 4)
val mockRound2Driver3Q1 = RoundQualifyingResult(driver = mockDriver3, time = LapTime(0,1, 1, 0), position = 1)
val mockRound2Driver4Q1 = RoundQualifyingResult(driver = mockDriver4, time = LapTime(0,1, 2, 0), position = 2)

val mockRound2Driver1Q2 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 2, 0), position = 2)
val mockRound2Driver3Q2 = RoundQualifyingResult(driver = mockDriver3, time = LapTime(0,1, 3, 0), position = 3)
val mockRound2Driver4Q2 = RoundQualifyingResult(driver = mockDriver4, time = LapTime(0,1, 1, 0), position = 1)

val mockRound2Driver1Q3 = RoundQualifyingResult(driver = mockDriver1, time = LapTime(0,1, 2, 0), position = 4)
val mockRound2Driver4Q3 = RoundQualifyingResult(driver = mockDriver4, time = LapTime(0,1, 1, 0), position = 1)

val round2RaceResultDriver4 = RoundRaceResult(
        driver = mockDriver4,
        time = LapTime(1,0,0,0),
        points = 4,
        grid = 1,
        qualified = 1,
        finish = 4,
        status = "Finished",
        fastestLap = null
)
val round2RaceResultDriver3 = RoundRaceResult(
        driver = mockDriver3,
        time = LapTime(1,1,0,0),
        points = 12,
        grid = 3,
        qualified = 3,
        finish = 2,
        status = "Finished",
        fastestLap = null
)
val round2RaceResultDriver2 = RoundRaceResult(
        driver = mockDriver2,
        time = LapTime(1,2,0,0),
        points = 8,
        grid = 4,
        qualified = 4,
        finish = 3,
        status = "Finished",
        fastestLap = null
)
val round2RaceResultDriver1 = RoundRaceResult(
        driver = mockDriver1,
        time = LapTime(1,0,0,0),
        points = 16,
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
val mockRound2: Round = Round(
        season = 2019,
        round = 2,
        date = mockLocalDateOfRound2,
        time = mockLocalTimeOfRound2,
        name = "Round 2",
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
        )
)




















val mockDriverSlimConstructor = SlimConstructor(
        id = "slim",
        name = "slim",
        color = Color.CYAN.rgb
)
val mockDriverRound11 = DriverOverviewRace(
        status = "Finished",
        finished = 2,
        points = 18,
        qualified = 4,
        round = 1,
        season = 2019,
        raceName = "Race 1",
        date = LocalDate.now(),
        circuitName = mockCircuitCharlie.name,
        circuitId = mockCircuitCharlie.id,
        circuitNationality = mockCircuitCharlie.country,
        circuitNationalityISO = mockCircuitCharlie.countryISO
)
val mockDriverRound12 = DriverOverviewRace(
        status = "Finished",
        finished = 4,
        points = 12,
        qualified = 3,
        round = 2,
        season = 2019,
        raceName = "Race 2",
        date = LocalDate.now(),
        circuitName = mockCircuitCharlie.name,
        circuitId = mockCircuitCharlie.id,
        circuitNationality = mockCircuitCharlie.country,
        circuitNationalityISO = mockCircuitCharlie.countryISO
)
val mockDriverStanding1 = DriverOverviewStanding(
        bestFinish = 2,
        bestFinishQuantity = 1,
        bestQualifying = 3,
        bestQualifyingQuantity = 1,
        championshipStanding = 2,
        isInProgress = false,
        points = 30,
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
val mockDriverRound21 = DriverOverviewRace(
        status = "Finished",
        finished = 7,
        points = 4,
        qualified = 2,
        round = 1,
        season = 2020,
        raceName = "Race 1",
        date = LocalDate.now(),
        circuitName = mockCircuitCharlie.name,
        circuitId = mockCircuitCharlie.id,
        circuitNationality = mockCircuitCharlie.country,
        circuitNationalityISO = mockCircuitCharlie.countryISO
)
val mockDriverRound22 = DriverOverviewRace(
        status = "Finished",
        finished = 12,
        points = 0,
        qualified = 15,
        round = 2,
        season = 2020,
        raceName = "Race 2",
        date = LocalDate.now(),
        circuitName = mockCircuitDelta.name,
        circuitId = mockCircuitDelta.id,
        circuitNationality = mockCircuitDelta.country,
        circuitNationalityISO = mockCircuitDelta.countryISO
)
val mockDriverStanding2 = DriverOverviewStanding(
        bestFinish = 7,
        bestFinishQuantity = 1,
        bestQualifying = 2,
        bestQualifyingQuantity = 1,
        championshipStanding = 5,
        isInProgress = false,
        points = 4,
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
val mockDriverOverview = DriverOverview(
        id = mockDriver1.id,
        firstName = mockDriver1.firstName,
        lastName = mockDriver1.lastName,
        code = mockDriver1.code,
        number = mockDriver1.number,
        wikiUrl = mockDriver1.wikiUrl,
        photoUrl = mockDriver1.photoUrl,
        dateOfBirth = mockDriver1.dateOfBirth,
        nationality = mockDriver1.nationality,
        nationalityISO = mockDriver1.nationalityISO,
        standings = listOf(
                mockDriverStanding1,
                mockDriverStanding2
        )
)

//region Season

val mockSeason = Season(
        season = 2019,
        drivers = listOf(mockDriver1.toDriver(), mockDriver2.toDriver(), mockDriver3.toDriver(), mockDriver4.toDriver()),
        constructors = listOf(mockConstructorAlpha, mockConstructorBeta),
        rounds = listOf(mockRound1, mockRound2),
        constructorStandings = emptyMap(),
        driverStandings = emptyMap()
)

//endregion

