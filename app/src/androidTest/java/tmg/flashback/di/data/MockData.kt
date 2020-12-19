package tmg.flashback.di.data

import android.graphics.Color
import androidx.core.graphics.toColor
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.repo.models.ConstructorDriver
import tmg.flashback.repo.models.stats.*
import kotlin.math.round

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

internal val mockDriverAlex: Driver = Driver(
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

internal val mockDriverBrian: Driver = Driver(
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

internal val mockDriverCharlie: Driver = Driver(
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


internal val mockDriverDaniel: Driver = Driver(
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
        mockDriverAlex,
        mockDriverBrian,
        mockDriverCharlie,
        mockDriverDaniel
)

//endregion

//region Mock Rounds

internal val mockRound1: Round = mockRound(
        season = 2019,
        round = 1,
        date = LocalDate.of(2019, 3, 4),
        name = "First Grand Prix",
        drivers = mockAllDrivers,
        constructors = mockAllConstructors,
        circuit = mockCircuitLeicester,
        q1Order = listOf(
                mockDriverCharlie,
                mockDriverBrian,
                mockDriverAlex,
                mockDriverDaniel
        ),
        q2Order = listOf(
                mockDriverBrian,
                mockDriverAlex,
                mockDriverCharlie
        ),
        q3Order = listOf(
                mockDriverAlex,
                mockDriverBrian
        ),
        finishOrder = listOf(
                mockDriverAlex,
                mockDriverBrian,
                mockDriverCharlie,
                mockDriverDaniel
        )
)

internal val mockRound2: Round = mockRound(
        season = 2019,
        round = 2,
        date = LocalDate.of(2019, 4, 4),
        name = "Second Grand Prix",
        drivers = mockAllDrivers,
        constructors = mockAllConstructors,
        circuit = mockCircuitLeicester,
        q1Order = listOf(
                mockDriverAlex,
                mockDriverBrian,
                mockDriverDaniel,
                mockDriverCharlie,
        ),
        q2Order = listOf(
                mockDriverDaniel,
                mockDriverBrian,
                mockDriverAlex
        ),
        q3Order = listOf(
                mockDriverDaniel,
                mockDriverBrian
        ),
        finishOrder = listOf(
                mockDriverCharlie,
                mockDriverBrian,
                mockDriverDaniel,
                mockDriverAlex
        )
)

//endregion

//region Season

internal val mockSeason: Season = Season(
        season = 2019,
        drivers = listOf(mockDriverAlex, mockDriverBrian, mockDriverCharlie, mockDriverDaniel),
        constructors = listOf(mockConstructorBlue, mockConstructorGreen),
        rounds = listOf(
                mockRound1,
                mockRound2
        ),
        driverStandings = mapOf(
                mockDriverAlex.id to Pair(mockDriverAlex, 24),
                mockDriverBrian.id to Pair(mockDriverBrian, 33),
                mockDriverCharlie.id to Pair(mockDriverCharlie, 43),
                mockDriverDaniel.id to Pair(mockDriverDaniel, 40)
        ),
        constructorStandings = mapOf(
                mockConstructorGreen.id to Pair(mockConstructorGreen, 57),
                mockConstructorBlue.id to Pair(mockConstructorBlue, 83)
        )
)

//endregion

//region History

internal val mockRound1History: HistoryRound = mockRound1.toHistory()
internal val mockRound2History: HistoryRound = mockRound2.toHistory()

internal val mockHistory: History = History(
        season = 2019,
        winner = WinnerSeason(
                season = 2019,
                driver = listOf(WinnerSeasonDriver(mockDriverCharlie.id, mockDriverCharlie.name, mockDriverCharlie.photoUrl, 43)),
                constructor = listOf(WinnerSeasonConstructor(mockConstructorBlue.id, mockConstructorBlue.name, "#0000ff", 83))
        ),
        rounds = listOf(
                mockRound1History,
                mockRound2History
        )
)

//endregion

//region DriverOverview

internal val mockDriverDanielStanding: DriverOverviewStanding = DriverOverviewStanding(
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
                DriverOverviewRace(
                        status = "Finished",
                        finished = 1,
                        points = 25,
                        qualified = 4,
                        round = mockRound1.round,
                        season = mockRound1.season,
                        raceName = mockRound1.name,
                        date = mockRound1.date,
                        constructor = mockConstructorBlue.toSlim(),
                        circuitName = mockRound1.circuit.name,
                        circuitId = mockRound1.circuit.id,
                        circuitNationality = mockRound1.circuit.country,
                        circuitNationalityISO = mockRound1.circuit.countryISO
               )
        )
)
internal val mockDriverDanielOverview: DriverOverview = DriverOverview(
        id = mockDriverDaniel.id,
        firstName = mockDriverDaniel.firstName,
        lastName = mockDriverDaniel.lastName,
        code = mockDriverDaniel.code,
        number = mockDriverDaniel.number,
        wikiUrl = mockDriverDaniel.wikiUrl,
        photoUrl = mockDriverDaniel.photoUrl,
        dateOfBirth = mockDriverDaniel.dateOfBirth,
        nationality = mockDriverDaniel.nationality,
        nationalityISO = mockDriverDaniel.nationalityISO,
        standings = listOf(mockDriverDanielStanding)
)

//endregion

//region ConstructorOverview

internal val mockConstructorBlueOverviewDriverStanding = ConstructorOverviewDriverStanding(
        driver = mockDriverDaniel.forRound().toConstructorDriver(),
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

internal val mockConstructorBlueOverviewStanding = ConstructorOverviewStanding(
        drivers = mapOf(
                mockDriverDaniel.id to mockConstructorBlueOverviewDriverStanding
        ),
        isInProgress = false,
        championshipStanding = 1,
        points = 25,
        season = 2019,
        races = 1
)

internal val mockConstructorBlueOverview = ConstructorOverview(
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
        drivers: List<Driver>,
        constructors: List<Constructor>,
        circuit: CircuitSummary,
        q1Order: List<Driver>,
        q2Order: List<Driver>,
        q3Order: List<Driver>,
        finishOrder: List<Driver>
): Round = Round(
        season = season,
        round = round,
        date = date,
        time = LocalTime.of(14, 10),
        name = name,
        wikipediaUrl = "https://www.wikipedia.com",
        drivers = drivers.map { it.forRound() },
        constructors = constructors,
        circuit = circuit,
        q1 = buildQualifying(mockAllDrivers.map { it.forRound() }, q1Order),
        q2 = buildQualifying(mockAllDrivers.map { it.forRound() }, q2Order),
        q3 = buildQualifying(mockAllDrivers.map { it.forRound() }, q3Order),
        race = buildRace(mockAllDrivers.map { it.forRound() }, finishOrder)
)

private fun Round.toHistory(): HistoryRound {
    return HistoryRound(
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

private fun buildQualifying(drivers: List<RoundDriver>, order: List<Driver>) = order
        .mapIndexed { index, driverItem ->
            val driver = drivers.first { it.id == driverItem.id }
            return@mapIndexed driver.id to RoundQualifyingResult(driver, LapTime(0, 1, index + 1, 0), index + 1)
        }
        .toMap()

private fun buildRace(drivers: List<RoundDriver>, order: List<Driver>) = order
        .mapIndexed { index, driverItem ->
            val driver = drivers.first { it.id == driverItem.id }
            return@mapIndexed driver.id to RoundRaceResult(
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