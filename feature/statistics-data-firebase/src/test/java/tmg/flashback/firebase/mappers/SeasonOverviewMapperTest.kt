package tmg.flashback.firebase.mappers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeParseException
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.CircuitSummary
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.ConstructorDriver
import tmg.flashback.data.models.stats.ConstructorStandings
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.data.models.stats.DriverStandings
import tmg.flashback.data.models.stats.FastestLap
import tmg.flashback.data.models.stats.Round
import tmg.flashback.data.models.stats.RoundQualifyingResult
import tmg.flashback.data.models.stats.RoundRaceResult
import tmg.flashback.data.models.stats.RoundSprintQualifyingResult
import tmg.flashback.data.models.stats.Season
import tmg.flashback.data.models.stats.SeasonStanding
import tmg.flashback.data.models.stats.noTime
import tmg.flashback.data.utils.toLapTime
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewConstructorMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewDriverMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q1
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q2
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q3
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewStandingsMapper
import tmg.flashback.firebase.models.FRound
import tmg.flashback.firebase.models.FSeason
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.FSeasonOverviewDriver
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuitLocation
import tmg.flashback.firebase.models.FSeasonOverviewRaceRaceFastestLap
import tmg.flashback.firebase.models.FSeasonStatistics
import tmg.flashback.firebase.models.FSeasonStatisticsPoints
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class SeasonOverviewMapperTest: BaseTest() {

    private val mockDriverMapper: SeasonOverviewDriverMapper = mockk(relaxed = true)
    private val mockRaceMapper: SeasonOverviewRaceMapper = mockk(relaxed = true)
    private val mockStandingMapper: SeasonOverviewStandingsMapper = mockk(relaxed = true)
    private val mockConstructorMapper: SeasonOverviewConstructorMapper = mockk(relaxed = true)

    private val mockQ1: Map<String, RoundQualifyingResult> = mockk(relaxed = true)
    private val mockQ2: Map<String, RoundQualifyingResult> = mockk(relaxed = true)
    private val mockQ3: Map<String, RoundQualifyingResult> = mockk(relaxed = true)
    private val mockQSprint: Map<String, RoundSprintQualifyingResult> = mockk(relaxed = true)
    private val mockQRace: Map<String, RoundRaceResult> = mockk(relaxed = true)

    private val mockDriverStandings: DriverStandings = mockk(relaxed = true)
    private val mockConstructorStandings: ConstructorStandings = mockk(relaxed = true)

    private lateinit var sut: SeasonOverviewMapper

    private fun initSUT() {
        sut = SeasonOverviewMapper(
            mockRaceMapper,
            mockDriverMapper,
            mockStandingMapper,
            mockConstructorMapper
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockRaceMapper.mapQualifying(any(), Q1, FRound.model().qualifying, any()) } returns mockQ1
        every { mockRaceMapper.mapQualifying(any(), Q2, FRound.model().qualifying, any()) } returns mockQ2
        every { mockRaceMapper.mapQualifying(any(), Q3, FRound.model().qualifying, any()) } returns mockQ3
        every { mockRaceMapper.mapSprintQualifying(any(), FRound.model().sprintQualifying, any()) } returns mockQSprint
        every { mockRaceMapper.mapRace(any(), FRound.model().race, any(), any()) } returns mockQRace
        every { mockRaceMapper.mapCircuit(FSeasonOverviewRaceCircuit.model()) } returns mockCircuit()

        every { mockDriverMapper.mapDriver(any(), "driverId") } returns mockDriver()

        every { mockConstructorMapper.mapConstructor(any()) } returns mockConstructor()

        every { mockStandingMapper.mapDriverStandings(any(), any()) } returns mockDriverStandings
        every { mockStandingMapper.mapConstructorStandings(any(), any()) } returns mockConstructorStandings
    }

    @Test
    fun `Season maps fields correctly`() {
        initSUT()

        val input = FSeason.model()
        val inputSeason = 2020
        val expected = Season(
            season = 2020,
            drivers = listOf(mockDriver()),
            constructors = listOf(mockConstructor()),
            rounds = listOf(
                Round(
                    season = 2020,
                    round = 1,
                    date = LocalDate.of(2020, 1, 1),
                    time = LocalTime.of(12, 0, 0),
                    name = "name",
                    wikipediaUrl = "wikiUrl",
                    drivers = listOf(
                        mockDriver().toConstructorDriver(1)
                    ),
                    constructors = listOf(
                        mockConstructor()
                    ),
                    circuit = mockCircuit(),
                    q1 = mockQ1,
                    q2 = mockQ2,
                    q3 = mockQ3,
                    qSprint = mockQSprint,
                    race = mockQRace,
                )
            ),
            driverStandings = mockDriverStandings,
            constructorStandings = mockConstructorStandings
        )

        assertEquals(expected, sut.mapSeason(input, inputSeason))
    }

    @Test
    fun `Season driver standings is empty list if standings are null`() {
        initSUT()

        val input = FSeason.model(standings = null)
        assertEquals(emptyList<SeasonStanding<Driver>>(), sut.mapSeason(input, 2020).driverStandings)
    }

    @Test
    fun `Season constructor standings is empty list if standings are null`() {
        initSUT()

        val input = FSeason.model(standings = null)
        assertEquals(emptyList<SeasonStanding<Constructor>>(), sut.mapSeason(input, 2020).constructorStandings)
    }

    @Test
    fun `Season race is empty map if race is null`() {
        initSUT()

        val input = FSeason.model(race = null)
        assertEquals(emptyList<Round>(), sut.mapSeason(input, 2020).rounds)
    }

    @Test
    fun `Season drivers is empty map if driver is null`() {
        initSUT()

        val input = FSeason.model(drivers = null)
        assertEquals(emptyList<Driver>(), sut.mapSeason(input, 2020).drivers)
    }

    @Test
    fun `Season constructors is empty map if constructor is null`() {
        initSUT()

        val input = FSeason.model(constructors = null)
        assertEquals(emptyList<Constructor>(), sut.mapSeason(input, 2020).constructors)
    }

    @Test
    fun `Round maps fields correctly`() {
        initSUT()

        val input = FRound.model()
        val inputDrivers = listOf(mockDriver())
        val inputConstructors = listOf(mockConstructor())
        val expected = Round(
            season = 2020,
            round = 1,
            date = LocalDate.of(2020, 1, 1),
            time = LocalTime.of(12, 0, 0),
            name = "name",
            wikipediaUrl = "wikiUrl",
            drivers = listOf(
                mockDriver().toConstructorDriver(1)
            ),
            constructors = listOf(
                mockConstructor()
            ),
            circuit = mockCircuit(),
            q1 = mockQ1,
            q2 = mockQ2,
            q3 = mockQ3,
            qSprint = mockQSprint,
            race = mockQRace,
        )

        assertEquals(expected, sut.mapRound(input, inputDrivers, inputConstructors))
    }

    @Test
    fun `Round throws exception if date is invalid`() {
        initSUT()

        val input = FRound.model(date = "invalid")
        val inputDrivers = listOf(mockDriver())
        val inputConstructors = listOf(mockConstructor())

        assertThrows(DateTimeParseException::class.java) {
            sut.mapRound(input, inputDrivers, inputConstructors)
        }
    }
}

private fun mockDriver(driverId: String = "driverId", constructorsMap: Map<Int, String> = mapOf(1 to "constructorId")): Driver {
    return Driver(
        id = driverId,
        firstName = "firstName",
        lastName = "lastName",
        code = "ALB",
        number = 23,
        wikiUrl = "wikiUrl",
        photoUrl = "photoUrl",
        dateOfBirth = LocalDate.of(1995, 10, 12),
        nationality = "nationality",
        nationalityISO = "nationalityISO",
        constructors = constructorsMap
            .map { (key, value) ->
                key to Constructor(
                    id = value,
                    name = "constructorName",
                    wikiUrl = "wikiUrl",
                    nationality = "nationality",
                    nationalityISO = "nationalityISO",
                    color = 0
                )
            }
            .toMap(),
        startingConstructor = Constructor(
            id = "constructorId",
            name = "constructorName",
            wikiUrl = "wikiUrl",
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            color = 0,
        ),
    )
}
private fun mockConstructor(id: String = "constructorId"): Constructor {
    return Constructor(
        id = id,
        name = "constructorName",
        wikiUrl = "wikiUrl",
        nationality = "nationality",
        nationalityISO = "nationalityISO",
        color = 0
    )
}
private fun mockCircuit(id: String = "circuitId"): CircuitSummary {
    return CircuitSummary(
        id = id,
        name = "circuitName",
        wikiUrl = "wikiUrl",
        locality = "locality",
        country = "country",
        countryISO = "countryISO",
        locationLat = 51.101,
        locationLng = -1.101
    )
}