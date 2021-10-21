package tmg.flashback.firebase.mappers.seasonoverview

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.data.models.stats.CircuitSummary
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.data.models.stats.FastestLap
import tmg.flashback.data.models.stats.Location
import tmg.flashback.data.models.stats.RoundQualifyingResult
import tmg.flashback.data.models.stats.RoundRaceResult
import tmg.flashback.data.models.stats.RoundSprintQualifyingResult
import tmg.flashback.data.models.stats.noTime
import tmg.flashback.data.utils.toLapTime
import tmg.flashback.firebase.mappers.LocationMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q1
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q2
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q3
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit
import tmg.flashback.firebase.models.FSeasonOverviewRaceQualifying
import tmg.flashback.firebase.models.FSeasonOverviewRaceRace
import tmg.flashback.firebase.models.FSeasonOverviewRaceRaceFastestLap
import tmg.flashback.firebase.models.FSeasonOverviewRaceSprintQualifying
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class SeasonOverviewRaceMapperTest: BaseTest() {

    private val mockLocationMapper: LocationMapper = mockk(relaxed = true)

    private lateinit var sut: SeasonOverviewRaceMapper

    private fun initSUT() {
        sut = SeasonOverviewRaceMapper(mockLocationMapper)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockLocationMapper.mapCircuitLocation(any()) } returns Location(1.0, 2.0)
    }

    @Test
    fun `RoundRaceResult maps fields correctly`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceRace.model())
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        val expected = mapOf(
            "driverId" to RoundRaceResult(
                driver = mockDriver().toConstructorDriver(forRound),
                time = "1:23:23.130".toLapTime(),
                points = 12.0,
                grid = 2,
                qualified = 2,
                finish = 1,
                status = "Finished",
                fastestLap = FastestLap(
                    rank = 1,
                    lap = 34,
                    lapTime = "1:43.203".toLapTime()
                ),
            )
        )

        assertEquals(expected, sut.mapRace(forRound, input, emptyMap(), allDrivers))
    }

    @Test
    fun `RoundRaceResult throws exception for driver that isnt in list of all drivers`() {
        initSUT()

        val input = mapOf(
            "driverId1" to FSeasonOverviewRaceRace.model(),
            "ignoredDriver" to FSeasonOverviewRaceRace.model()
        )
        val forRound = 1
        val allDrivers = listOf(mockDriver("driverId1"))

        assertThrows(NoSuchElementException::class.java) {
            sut.mapRace(forRound, input, emptyMap(), allDrivers)
        }
    }

    @Test
    fun `RoundRaceResult uses sprint qualifying qualified value if available`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceRace.model())
        val forRound = 1
        val allDrivers = listOf(mockDriver())
        val sprintQuali = mapOf(
            "driverId" to FSeasonOverviewRaceSprintQualifying.model(
                qualified = 10
            )
        )

        assertEquals(10, sut.mapRace(forRound, input, sprintQuali, allDrivers)["driverId"]!!.qualified)
    }

    @Test
    fun `RoundRaceResult defaults points to 0 if null`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceRace.model(
            points = null
        ))
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        assertEquals(0.0, sut.mapRace(forRound, input, emptyMap(), allDrivers)["driverId"]!!.points)
    }

    @Test
    fun `RoundRaceResult defaults grid to 0 if null`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceRace.model(
            grid = null
        ))
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        assertEquals(0, sut.mapRace(forRound, input, emptyMap(), allDrivers)["driverId"]!!.grid)
    }

    @Test
    fun `RoundRaceResult defaults finished to 0 if null`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceRace.model(
            result = null
        ))
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        assertEquals(0, sut.mapRace(forRound, input, emptyMap(), allDrivers)["driverId"]!!.finish)
    }

    @Test
    fun `RoundRaceResult defaults race status to unknown if null`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceRace.model(
            status = null
        ))
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        assertEquals("Unknown", sut.mapRace(forRound, input, emptyMap(), allDrivers)["driverId"]!!.status)
    }

    @Test
    fun `RoundSprintQualifyingResult maps fields correctly`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceSprintQualifying.model())
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        val expected = mapOf(
            "driverId" to RoundSprintQualifyingResult(
                driver = mockDriver().toConstructorDriver(forRound),
                time = "26:23.123".toLapTime(),
                points = 15.0,
                grid = 3,
                qualified = 3,
                finish = 1,
                status = "Finished",
            )
        )

        assertEquals(expected, sut.mapSprintQualifying(forRound, input, allDrivers))
    }

    @Test
    fun `RoundSprintQualifyingResult throws exception for driver that isnt in list of all drivers`() {
        initSUT()

        val input = mapOf(
            "driverId1" to FSeasonOverviewRaceSprintQualifying.model(),
            "ignoredDriver" to FSeasonOverviewRaceSprintQualifying.model()
        )
        val forRound = 1
        val allDrivers = listOf(mockDriver("driverId1"))

        assertThrows(NoSuchElementException::class.java) {
            sut.mapSprintQualifying(forRound, input, allDrivers)
        }
    }

    @Test
    fun `RoundSprintQualifyingResult defaults points to 0 if null`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceSprintQualifying.model(
            points = null
        ))
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        assertEquals(0.0, sut.mapSprintQualifying(forRound, input, allDrivers)["driverId"]!!.points)
    }

    @Test
    fun `RoundSprintQualifyingResult defaults grid to 0 if null`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceSprintQualifying.model(
            grid = null
        ))
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        assertEquals(0, sut.mapSprintQualifying(forRound, input, allDrivers)["driverId"]!!.grid)
    }

    @Test
    fun `RoundSprintQualifyingResult defaults finished to 0 if null`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceSprintQualifying.model(
            result = null
        ))
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        assertEquals(0, sut.mapSprintQualifying(forRound, input, allDrivers)["driverId"]!!.finish)
    }

    @Test
    fun `RoundSprintQualifyingResult defaults race status to unknown if null`() {
        initSUT()

        val input = mapOf("driverId" to FSeasonOverviewRaceSprintQualifying.model(
            status = null
        ))
        val forRound = 1
        val allDrivers = listOf(mockDriver())

        assertEquals("Unknown", sut.mapSprintQualifying(forRound, input, allDrivers)["driverId"]!!.status)
    }

    @Test
    fun `RoundQualifyingResult maps fields correctly`() {
        initSUT()

        val input = mapOf(
            "driverId" to FSeasonOverviewRaceQualifying.model(
                q1 = "1:01.123",
                q2 = "1:02.123",
                q3 = "1:03.123",
            ),
            "driverId2" to FSeasonOverviewRaceQualifying.model(
                q1 = "1:01.122",
                q2 = "1:02.124",
                q3 = "1:04.000"
            )
        )
        val forRound = 1
        val allDrivers = listOf(mockDriver(driverId = "driverId"), mockDriver(driverId = "driverId2"))

        val expectedQ1 = mapOf(
            "driverId" to RoundQualifyingResult(
                driver = mockDriver(driverId = "driverId").toConstructorDriver(1),
                time = "1:01.123".toLapTime(),
                position = 2
            ),
            "driverId2" to RoundQualifyingResult(
                driver = mockDriver(driverId = "driverId2").toConstructorDriver(1),
                time = "1:01.122".toLapTime(),
                position = 1
            )
        )
        val expectedQ2 = mapOf(
            "driverId" to RoundQualifyingResult(
                driver = mockDriver(driverId = "driverId").toConstructorDriver(1),
                time = "1:02.123".toLapTime(),
                position = 1
            ),
            "driverId2" to RoundQualifyingResult(
                driver = mockDriver(driverId = "driverId2").toConstructorDriver(1),
                time = "1:02.124".toLapTime(),
                position = 2
            )
        )
        val expectedQ3 = mapOf(
            "driverId" to RoundQualifyingResult(
                driver = mockDriver(driverId = "driverId").toConstructorDriver(1),
                time = "1:03.123".toLapTime(),
                position = 1
            ),
            "driverId2" to RoundQualifyingResult(
                driver = mockDriver(driverId = "driverId2").toConstructorDriver(1),
                time = "1:04.000".toLapTime(),
                position = 2
            )
        )

        assertEquals(expectedQ1, sut.mapQualifying(forRound, Q1, input, allDrivers))
        assertEquals(expectedQ2, sut.mapQualifying(forRound, Q2, input, allDrivers))
        assertEquals(expectedQ3, sut.mapQualifying(forRound, Q3, input, allDrivers))
    }

    @Test
    fun `RoundQualifyingResult throws exception for driver ids that do not exist in all drivers map`() {
        initSUT()

        val input = mapOf(
            "driverId" to FSeasonOverviewRaceQualifying.model(
                q1 = "1:01:123"
            ),
            "driverId2" to FSeasonOverviewRaceQualifying.model(
                q1 = "1:01:122"
            )
        )
        val forRound = 1
        val allDrivers = listOf(mockDriver(driverId = "driverId"))

        assertThrows(NoSuchElementException::class.java) {
            sut.mapQualifying(forRound, Q1, input, allDrivers)
        }
    }

    @Test
    fun `CircuitSummary maps fields correctly`() {
        initSUT()

        val input = FSeasonOverviewRaceCircuit.model()
        val expected = CircuitSummary(
            id = "circuitId",
            name = "circuitName",
            wikiUrl = "wikiUrl",
            locality = "locality",
            country = "country",
            countryISO = "countryISO",
            location = Location(
                lat = 1.0,
                lng = 2.0
            )
        )

        assertEquals(expected, sut.mapCircuit(input))
    }

    @Test
    fun `FastestLap maps fields correctly`() {
        initSUT()

        val input = FSeasonOverviewRaceRaceFastestLap.model()
        val expected = FastestLap(
            rank = 1,
            lap = 34,
            lapTime = "1:43.203".toLapTime()
        )

        assertEquals(expected, sut.mapFastestLap(input))
    }

    @Test
    fun `FastestLap maps invalid time to no time`() {
        initSUT()

        val input = FSeasonOverviewRaceRaceFastestLap.model(time = "invalid")
        assertEquals(noTime, sut.mapFastestLap(input).lapTime)
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