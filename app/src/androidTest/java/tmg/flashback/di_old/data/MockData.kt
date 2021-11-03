package tmg.flashback.di_old.data

import android.graphics.Color
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.data.models.stats.*
import tmg.flashback.formula1.model.*
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver
import tmg.flashback.formula1.model.ConstructorHistorySeason

/**
 * Mock data used for UI testing:
 *   - 25, 18, 15, 12 points per driver
 *
 * Circuits:
 *   - nottingham
 *   - leicester
 *
 * Constructors:
 *   - green: [alex, brian]
 *   - blue: [charlie, daniel]
 *
 * Drivers:
 *   - alex
 *   - brian
 *   - charlie
 *   - daniel
 *
 * Season:
 *   - round1:
 *      - Held at nottingham
 *      - q1: [charlie, brian, alex, daniel]
 *      - q2: [brian, alex, charlie]
 *      - q3: [alex, brian]
 *      - finish: [daniel, charlie, brian, alex]
 *   - round2:
 *      - Held at leicester
 *      - q1: [alex, brian, daniel, charlie]
 *      - q2: [daniel, brian, alex]
 *      - q3: [daniel, brian]
 *      - finish: [charlie, brian, daniel, alex]
 */

//region Circuit

internal val mockCircuitLeicester = CircuitSummary(
        id = "leicester",
        name = "Leicester Grand Prix",
        wikiUrl = "https://www.wikipedia.com/leicester",
        locality = "British",
        country = "England",
        countryISO = "GBR",
        locationLat = 51.1,
        locationLng = -1.0
)

internal val mockCircuitNottingham = CircuitSummary(
        id = "nottingham",
        name = "Nottingham Grand Prix",
        wikiUrl = "https://www.wikipedia.com/nottingham",
        locality = "British",
        country = "England",
        countryISO = "GBR",
        locationLat = 51.1,
        locationLng = -1.0
)

//endregion

//region Constructors

internal val mockConstructorGreen: Constructor = Constructor(
        id = "green",
        name = "Green",
        wikiUrl = "https://www.wikipedia.com/green",
        nationality = "British",
        nationalityISO = "GBR",
        color = Color.GREEN
)

internal val mockConstructorBlue: Constructor = Constructor(
        id = "blue",
        name = "Blue",
        wikiUrl = "https://www.wikipedia.com/blue",
        nationality = "British",
        nationalityISO = "GBR",
        color = Color.BLUE
)

internal val mockAllConstructors = listOf(
        mockConstructorGreen,
        mockConstructorBlue
)

//endregion

//region Drivers

internal val MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX: DriverWithEmbeddedConstructor = DriverWithEmbeddedConstructor(
        id = "alex",
        firstName = "Alex",
        lastName = "Driver1",
        code = "AL",
        number = 1,
        wikiUrl = "https://www.wikipedia.com/alex",
        photoUrl = null,
        dateOfBirth = LocalDate.of(1980, 1, 1),
        nationality = "American",
        nationalityISO = "USA",
        constructorAtEndOfSeason = mockConstructorGreen
)

internal val MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN: DriverWithEmbeddedConstructor = DriverWithEmbeddedConstructor(
        id = "brian",
        firstName = "Brian",
        lastName = "Driver2",
        code = "BR",
        number = 2,
        wikiUrl = "https://www.wikipedia.com/brian",
        photoUrl = null,
        dateOfBirth = LocalDate.of(1980, 1, 2),
        nationality = "British",
        nationalityISO = "GBR",
        constructorAtEndOfSeason = mockConstructorGreen
)

internal val MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE: DriverWithEmbeddedConstructor = DriverWithEmbeddedConstructor(
        id = "charlie",
        firstName = "Charlie",
        lastName = "Driver3",
        code = "CH",
        number = 3,
        wikiUrl = "https://www.wikipedia.com/charlie",
        photoUrl = null,
        dateOfBirth = LocalDate.of(1980, 1, 3),
        nationality = "Czech",
        nationalityISO = "CZE",
        constructorAtEndOfSeason = mockConstructorBlue
)


internal val MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL: DriverWithEmbeddedConstructor = DriverWithEmbeddedConstructor(
        id = "daniel",
        firstName = "Daniel",
        lastName = "Driver4",
        code = "DA",
        number = 4,
        wikiUrl = "https://www.wikipedia.com/daniel",
        photoUrl = null,
        dateOfBirth = LocalDate.of(1980, 1, 4),
        nationality = "Danish",
        nationalityISO = "DNK",
        constructorAtEndOfSeason = mockConstructorBlue
)

internal val mockAllDrivers = listOf(
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX,
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN,
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE,
        MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL
)

//endregion

//region Mock Rounds

internal val MOCK_RACE_1: Race = mockRound(
        season = 2019,
        round = 1,
        date = LocalDate.of(2019, 3, 4),
        name = "First Grand Prix",
        driverWithEmbeddedConstructors = mockAllDrivers,
        constructors = mockAllConstructors,
        circuit = mockCircuitLeicester,
        q1Order = listOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL
        ),
        q2Order = listOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE
        ),
        q3Order = listOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN
        ),
        finishOrder = listOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL
        )
)

internal val MOCK_RACE_2: Race = mockRound(
        season = 2019,
        round = 2,
        date = LocalDate.of(2019, 4, 4),
        name = "Second Grand Prix",
        driverWithEmbeddedConstructors = mockAllDrivers,
        constructors = mockAllConstructors,
        circuit = mockCircuitLeicester,
        q1Order = listOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE,
        ),
        q2Order = listOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX
        ),
        q3Order = listOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN
        ),
        finishOrder = listOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL,
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX
        )
)

//endregion

//region Season

internal val mockSeason: Season = Season(
        season = 2019,
        driverWithEmbeddedConstructors = listOf(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX, MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN, MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE, MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL),
        constructors = listOf(mockConstructorBlue, mockConstructorGreen),
        races = listOf(
                MOCK_RACE_1,
                MOCK_RACE_2
        ),
        driverStandings = mapOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX.id to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_ALEX, 24),
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN.id to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_BRIAN, 33),
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE.id to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE, 43),
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.id to Pair(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL, 40)
        ),
        constructorStandings = mapOf(
                mockConstructorGreen.id to Pair(mockConstructorGreen, 57),
                mockConstructorBlue.id to Pair(mockConstructorBlue, 83)
        )
)

//endregion

//region History

internal val MOCK_1_OVERVIEW_RACE: OverviewRace = MOCK_RACE_1.toHistory()
internal val MOCK_2_OVERVIEW_RACE: OverviewRace = MOCK_RACE_2.toHistory()

internal val MOCK_OVERVIEW: Overview =
        Overview(
            season = 2019,
            winner = WinnerSeason(
                    season = 2019,
                    driver = listOf(WinnerSeasonDriver(MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE.id, MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE.name, MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_CHARLIE.photoUrl, 43)),
                    constructor = listOf(WinnerSeasonConstructor(mockConstructorBlue.id, mockConstructorBlue.name, "#0000ff", 83))
            ),
            overviewRaces = listOf(
                    MOCK_1_OVERVIEW_RACE,
                    MOCK_2_OVERVIEW_RACE
            )
        )

//endregion

//region DriverOverview

internal val MOCK_DRIVER_DANIEL_STANDING: DriverHistorySeason = DriverHistorySeason(
        bestFinish = 1,
        bestFinishQuantity = 1,
        bestQualifying = 4,
        bestQualifyingQuantity = 1,
        championshipStanding = 1,
        isInProgress = false,
        points = 25,
        podiums = 1,
        races = 1,
        season = mockSeason.season,
        wins = 1,
        constructors = listOf(mockConstructorBlue.toSlim()),
        raceOverview = listOf(
                DriverHistorySeasonRace(
                        status = "Finished",
                        finished = 1,
                        points = 25,
                        qualified = 4,
                        round = MOCK_RACE_1.round,
                        season = MOCK_RACE_1.season,
                        raceName = MOCK_RACE_1.name,
                        date = MOCK_RACE_1.date,
                        constructor = mockConstructorBlue.toSlim(),
                        circuitName = MOCK_RACE_1.circuit.name,
                        circuitId = MOCK_RACE_1.circuit.id,
                        circuitNationality = MOCK_RACE_1.circuit.country,
                        circuitNationalityISO = MOCK_RACE_1.circuit.countryISO
               )
        )
)
internal val MOCK_DRIVER_DANIEL_HISTORY: DriverHistory = DriverHistory(
        id = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.id,
        firstName = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.firstName,
        lastName = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.lastName,
        code = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.code,
        number = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.number,
        wikiUrl = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.wikiUrl,
        photoUrl = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.photoUrl,
        dateOfBirth = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.dateOfBirth,
        nationality = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.nationality,
        nationalityISO = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.nationalityISO,
        standings = listOf(MOCK_DRIVER_DANIEL_STANDING)
)

//endregion

//region ConstructorOverview

internal val mockConstructorBlueOverviewDriverStanding = ConstructorHistorySeasonDriver(
        driver = MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.forRound().toConstructorDriver(),
        bestFinish = 1,
        bestQualifying = 4,
        points = 25,
        finishesInP1 = 1,
        finishesInP2 = 0,
        finishesInP3 = 0,
        finishesInPoints = 1,
        qualifyingP1 = 0,
        qualifyingP2 = 0,
        qualifyingP3 = 0,
        qualifyingTop10 = 1,
        races = 1,
        championshipStanding = 1
)

internal val mockConstructorBlueOverviewStanding = ConstructorHistorySeason(
        drivers = mapOf(
                MOCK_DRIVER_WITH_EMBEDDED_CONSTRUCTOR_DANIEL.id to mockConstructorBlueOverviewDriverStanding
        ),
        isInProgress = false,
        championshipStanding = 1,
        points = 25,
        season = 2019,
        races = 1
)

internal val mockConstructorBlueOverview = ConstructorHistory(
        id = mockConstructorBlue.id,
        name = mockConstructorBlue.name,
        wikiUrl = mockConstructorBlue.wikiUrl,
        nationality = mockConstructorBlue.nationality,
        nationalityISO = mockConstructorBlue.nationalityISO,
        color = mockConstructorBlue.color,
        standings = listOf(mockConstructorBlueOverviewStanding)
)

//endregion

//region Utilities

private fun mockRound(
        season: Int,
        round: Int,
        date: LocalDate,
        name: String,
        driverWithEmbeddedConstructors: List<DriverWithEmbeddedConstructor>,
        constructors: List<Constructor>,
        circuit: CircuitSummary,
        q1Order: List<DriverWithEmbeddedConstructor>,
        q2Order: List<DriverWithEmbeddedConstructor>,
        q3Order: List<DriverWithEmbeddedConstructor>,
        finishOrder: List<DriverWithEmbeddedConstructor>
): Race = Race(
        season = season,
        round = round,
        date = date,
        time = LocalTime.of(14, 10),
        name = name,
        wikipediaUrl = "https://www.wikipedia.com",
        drivers = driverWithEmbeddedConstructors.map { it.forRound() },
        constructors = constructors,
        circuit = circuit,
        q1 = buildQualifying(mockAllDrivers.map { it.forRound() }, q1Order),
        q2 = buildQualifying(mockAllDrivers.map { it.forRound() }, q2Order),
        q3 = buildQualifying(mockAllDrivers.map { it.forRound() }, q3Order),
        race = buildRace(mockAllDrivers.map { it.forRound() }, finishOrder)
)

private fun Race.toHistory(): OverviewRace {
    return OverviewRace(
            date = this.date,
            season = this.season,
            round = this.round,
            raceName = this.name,
            circuitId = this.circuit.id,
            circuitName = this.circuit.name,
            country = this.circuit.country,
            countryISO = this.circuit.countryISO,
            hasQualifying = true,
            hasResults = true
    )
}

private fun buildQualifying(drivers: List<RoundDriver>, order: List<DriverWithEmbeddedConstructor>) = order
        .mapIndexed { index, driverItem ->
            val driver = drivers.first { it.id == driverItem.id }
            return@mapIndexed driver.id to RaceQualifyingResult(driver, LapTime(0, 1, index + 1, 0), index + 1)
        }
        .toMap()

private fun buildRace(drivers: List<RoundDriver>, order: List<DriverWithEmbeddedConstructor>) = order
        .mapIndexed { index, driverItem ->
            val driver = drivers.first { it.id == driverItem.id }
            return@mapIndexed driver.id to RaceRaceResult(
                    driver = driver,
                    time = LapTime(0, 1, index + 1, 0),
                    points = getPoints(index + 1),
                    grid = order.size - index,
                    qualified = order.size - index,
                    finish = index + 1,
                    status = "Finished",
                    fastestLap = null
            )
        }
        .toMap()

private fun getPoints(pos: Int): Int {
    return when (pos) {
        1 -> 25
        2 -> 18
        3 -> 15
        4 -> 12
        5 -> 10
        6 -> 8
        7 -> 6
        8 -> 4
        9 -> 2
        10 -> 1
        else -> 0
    }
}

//endregion