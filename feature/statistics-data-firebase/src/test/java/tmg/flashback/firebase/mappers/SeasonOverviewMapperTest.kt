package tmg.flashback.firebase.mappers

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeParseException
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.CircuitSummary
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.ConstructorStandings
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.data.models.stats.DriverStandings
import tmg.flashback.data.models.stats.FastestLap
import tmg.flashback.data.models.stats.SeasonStanding
import tmg.flashback.data.models.stats.noTime
import tmg.flashback.data.utils.toLapTime
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

    private val mockCrashController: CrashController = mockk(relaxed = true)

    private lateinit var sut: SeasonOverviewMapper

    private fun initSUT() {
        sut = SeasonOverviewMapper(mockCrashController)
    }

    @Test
    fun `DriverStandings maps fields correctly`() {
        initSUT()

        val input = FSeasonStatistics.model()
        val inputDrivers = listOfNotNull(
            sut.mapDriver(FSeason.model(), "driverId")
        )
        val expected: DriverStandings = listOf(
            SeasonStanding(
                item = Driver(
                    id = "driverId",
                    firstName = "firstName",
                    lastName = "lastName",
                    code = "ALB",
                    number = 23,
                    wikiUrl = "wikiUrl",
                    photoUrl = "photoUrl",
                    dateOfBirth = LocalDate.of(1995, 10, 12),
                    nationality = "nationality",
                    nationalityISO = "nationalityISO",
                    constructorAtEndOfSeason = Constructor(
                        id = "constructorId",
                        name = "constructorName",
                        wikiUrl = "wikiUrl",
                        nationality = "nationality",
                        nationalityISO = "nationalityISO",
                        color = 0,
                    ),
                ),
                points = 1.0,
                position = 2,
            )
        )

        assertEquals(expected, sut.mapDriverStandings(input, inputDrivers))
    }

    @Test
    fun `DriverStandings points default to 0 if not specified`() {
        initSUT()

        val input = FSeasonStatistics.model(drivers = mapOf(
            "driverId" to FSeasonStatisticsPoints.model(p = null)
        ))
        val inputDrivers = listOfNotNull(
            sut.mapDriver(FSeason.model(), "driverId")
        )
        assertEquals(0.0, sut.mapDriverStandings(input, inputDrivers)[0].points)
    }

    @Test
    fun `DriverStandings if driver not found then not included in standings and logs to crashlytics`() {
        initSUT()

        val input = FSeasonStatistics.model(drivers = mapOf(
            "driverId1" to FSeasonStatisticsPoints(
                p = 1.0,
                pos = null
            ),
            "driverId2" to FSeasonStatisticsPoints(
                p = 2.0,
                pos = null
            )
        ))
        val inputDrivers = listOfNotNull(
            sut.mapDriver(FSeason.model(
                drivers = mapOf(
                    "driverId1" to FSeasonOverviewDriver.model(id = "driverId1")
                ),
                race = mapOf(
                    "r1" to FRound.model(driverCon = mapOf("driverId1" to "constructorId"))
                )
            ), "driverId1")
        )

        val result = sut.mapDriverStandings(input, inputDrivers)
        assertEquals(1, result.size)
        assertEquals("driverId1", result[0].item.id)
        assertEquals(2, result[0].position)

        verify {
            mockCrashController.logException(any())
        }
    }

    @Test
    fun `DriverStandings if position is not specified then order determined by points`() {
        initSUT()

        val input = FSeasonStatistics.model(drivers = mapOf(
            "driverId1" to FSeasonStatisticsPoints(
                p = 1.0,
                pos = null
            ),
            "driverId2" to FSeasonStatisticsPoints(
                p = 2.0,
                pos = null
            )
        ))

        val inputDrivers = FSeason.model(
            drivers = mapOf(
                "driverId1" to FSeasonOverviewDriver.model(id = "driverId1"),
                "driverId2" to FSeasonOverviewDriver.model(id = "driverId2")
            ),
            race = mapOf("r1" to FRound.model(
                driverCon = mapOf(
                    "driverId1" to "constructorId",
                    "driverId2" to "constructorId"
                )
            ))
        ).let {
            listOfNotNull(
                sut.mapDriver(it, "driverId1"),
                sut.mapDriver(it, "driverId2")
            )
        }

        val result = sut.mapDriverStandings(input, inputDrivers)
        assertEquals(2, result.size)
        assertEquals("driverId2", result[0].item.id)
        assertEquals(1, result[0].position)
        assertEquals("driverId1", result[1].item.id)
        assertEquals(2, result[1].position)
    }

    @Test
    fun `ConstructorStandings maps fields correctly`() {
        initSUT()

        val input = FSeasonStatistics.model()
        val expected: ConstructorStandings = listOf(
            SeasonStanding(
                item = Constructor(
                    id = "constructorId",
                    name = "constructorName",
                    wikiUrl = "wikiUrl",
                    nationality = "nationality",
                    nationalityISO = "nationalityISO",
                    color = 0,
                ),
                points = 1.0,
                position = 2,
            )
        )

        assertEquals(expected, sut.mapConstructorStandings(input, listOf(FSeasonOverviewConstructor.model())))
    }

    @Test
    fun `ConstructorStandings points default to 0 if not specified`() {
        initSUT()

        val input = FSeasonStatistics.model(constructors = mapOf(
            "constructorId" to FSeasonStatisticsPoints.model(p = null)
        ))
        val inputConstructors = listOf(
            FSeasonOverviewConstructor.model()
        )
        assertEquals(0.0, sut.mapConstructorStandings(input, inputConstructors)[0].points)
    }

    @Test
    fun `ConstructorStandings if constructor not found then not included in standings and logs to crashlytics`() {
        initSUT()

        val input = FSeasonStatistics.model(constructors = mapOf(
            "constructorId1" to FSeasonStatisticsPoints(
                p = 1.0,
                pos = null
            ),
            "constructorId2" to FSeasonStatisticsPoints(
                p = 2.0,
                pos = null
            )
        ))
        val inputConstructors = listOf(
            FSeasonOverviewConstructor.model(id = "constructorId1")
        )

        val result = sut.mapConstructorStandings(input, inputConstructors)
        assertEquals(1, result.size)
        assertEquals("constructorId1", result[0].item.id)
        assertEquals(2, result[0].position)

        verify {
            mockCrashController.logException(any())
        }
    }

    @Test
    fun `ConstructorStandings if position is not specified then order determined by points`() {
        initSUT()

        val input = FSeasonStatistics.model(constructors = mapOf(
                "constructorId1" to FSeasonStatisticsPoints(
                    p = 1.0,
                    pos = null
                ),
                "constructorId2" to FSeasonStatisticsPoints(
                    p = 2.0,
                    pos = null
                )
        ))
        val inputConstructors = listOf(
            FSeasonOverviewConstructor.model(id = "constructorId1"),
            FSeasonOverviewConstructor.model(id = "constructorId2")
        )

        val result = sut.mapConstructorStandings(input, inputConstructors)
        assertEquals(2, result.size)
        assertEquals("constructorId2", result[0].item.id)
        assertEquals(1, result[0].position)
        assertEquals("constructorId1", result[1].item.id)
        assertEquals(2, result[1].position)
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
            locationLat = 51.101,
            locationLng = -1.101
        )

        assertEquals(expected, sut.mapCircuit(input))
    }

    @Test
    fun `CircuitSummary defaults location to 0 if null`() {
        initSUT()

        val input = FSeasonOverviewRaceCircuit.model(
            location = FSeasonOverviewRaceCircuitLocation(
                lat = null,
                lng = null
            )
        )
        assertEquals(0.0, sut.mapCircuit(input).locationLat)
        assertEquals(0.0, sut.mapCircuit(input).locationLng)
    }

    @Test
    fun `CircuitSummary defaults location to 0 if invalid`() {
        initSUT()

        val input = FSeasonOverviewRaceCircuit.model(
            location = FSeasonOverviewRaceCircuitLocation(
                lat = "hello",
                lng = "world"
            )
        )
        assertEquals(0.0, sut.mapCircuit(input).locationLat)
        assertEquals(0.0, sut.mapCircuit(input).locationLng)
    }

    @Test
    fun `Driver maps fields correctly`() {
        initSUT()

        val input = FSeason.model()
        val expected = Driver(
            id = "driverId",
            firstName = "firstName",
            lastName = "lastName",
            code = "ALB",
            number = 23,
            wikiUrl = "wikiUrl",
            photoUrl = "photoUrl",
            dateOfBirth = LocalDate.of(1995, 10, 12),
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            constructorAtEndOfSeason = Constructor(
                id = "constructorId",
                name = "constructorName",
                wikiUrl = "wikiUrl",
                nationality = "nationality",
                nationalityISO = "nationalityISO",
                color = 0
            )
        )

        assertEquals(expected, sut.mapDriver(input, "driverId"))
    }

    @Test
    fun `Driver returns null if constructor is not available in top list`() {
        initSUT()

        val input = FSeason.model(constructors = null)
        assertNull(sut.mapDriver(input, "driverId"))
    }

    @Test
    fun `Driver returns null if constructor not assigned to driver is not available in top list`() {
        initSUT()

        val input = FSeason.model(constructors = mapOf(
            "constructorId2" to FSeasonOverviewConstructor.model(id = "constructorId2")
        ))
        assertNull(sut.mapDriver(input, "driverId"))
    }

    @Test
    fun `Driver returns null if no race with driver in`() {
        initSUT()

        val input = FSeason.model(race = mapOf(
            "r1" to FRound.model(
                driverCon = mapOf(
                    "driverId" to "constructorId2"
                )
            )
        ))
        assertNull(sut.mapDriver(input, "driverId"))
    }

    @Test
    fun `Driver number returns 0 if number is null`() {
        initSUT()

        val input = FSeason.model(drivers = mapOf(
            "driverId" to FSeasonOverviewDriver.model(
                number = null
            )
        ))
        assertEquals(0, sut.mapDriver(input, "driverId")!!.number)
    }

    @Test
    fun `Driver invalid date of birth crashes mapper`() {
        initSUT()

        val input = FSeason.model(drivers = mapOf(
            "driverId" to FSeasonOverviewDriver.model(
                dob = "invalid"
            )
        ))
        assertThrows(DateTimeParseException::class.java) {
            sut.mapDriver(input, "driverId")
        }
    }

    @Test
    fun `Constructor maps fields correctly`() {
        initSUT()

        val input = FSeasonOverviewConstructor.model()
        val expected = Constructor(
            id = "constructorId",
            name = "constructorName",
            wikiUrl = "wikiUrl",
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            color = 0
        )

        assertEquals(expected, sut.mapConstructor(input))
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