package tmg.flashback.firebase.mappers

import io.mockk.mockk
import io.mockk.verify
import java.lang.NullPointerException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.ConstructorStandings
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.data.models.stats.DriverStandings
import tmg.flashback.data.models.stats.FastestLap
import tmg.flashback.data.models.stats.SeasonStanding
import tmg.flashback.data.models.stats.noTime
import tmg.flashback.data.utils.toLapTime
import tmg.flashback.firebase.models.FConstructorOverviewStandings
import tmg.flashback.firebase.models.FConstructorOverviewStandingsDriver
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.FSeasonOverviewDriver
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
    fun `Driver maps fields correctly`() {
        initSUT()

        val input = FSeasonStatistics.model()
        val inputDrivers = listOf(
            FSeasonOverviewDriver.model() to FSeasonOverviewConstructor.model()
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
    fun `Driver points default to 0 if not specified`() {
        initSUT()

        val input = FSeasonStatistics.model(drivers = mapOf(
            "driverId" to FSeasonStatisticsPoints.model(p = null)
        ))
        val inputDrivers = listOf(
            FSeasonOverviewDriver.model() to FSeasonOverviewConstructor.model()
        )
        assertEquals(0.0, sut.mapDriverStandings(input, inputDrivers)[0].points)
    }

    @Test
    fun `Driver if constructor not found then not included in standings and logs to crashlytics`() {
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
        val inputDrivers = listOf(
            FSeasonOverviewDriver.model(id = "driverId1") to FSeasonOverviewConstructor.model()
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
    fun `Driver if position is not specified then order determined by points`() {
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
        val inputDrivers = listOf(
            FSeasonOverviewDriver.model(id = "driverId1") to FSeasonOverviewConstructor.model(),
            FSeasonOverviewDriver.model(id = "driverId2") to FSeasonOverviewConstructor.model()
        )

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