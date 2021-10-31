package tmg.flashback.firebase.mappers.seasonoverview

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.ConstructorStandings
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverStandings
import tmg.flashback.formula1.model.SeasonStanding
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.FSeasonStatistics
import tmg.flashback.firebase.models.FSeasonStatisticsPoints
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class SeasonOverviewStandingsMapperTest: BaseTest() {

    private val mockDriverMapper: SeasonOverviewDriverMapper = mockk(relaxed = true)
    private val mockConstructorMapper: SeasonOverviewConstructorMapper = mockk(relaxed = true)
    private val mockCrashController: CrashController = mockk(relaxed = true)

    private lateinit var sut: SeasonOverviewStandingsMapper

    private fun initSUT() {
        sut = SeasonOverviewStandingsMapper(mockDriverMapper, mockConstructorMapper, mockCrashController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockDriverMapper.mapDriver(any(), any()) } returns mockDriver()
        every { mockConstructorMapper.mapConstructor(any()) } returns mockConstructor()
        every { mockConstructorMapper.mapConstructor(FSeasonOverviewConstructor.model("constructorId1")) } returns mockConstructor("constructorId1")
        every { mockConstructorMapper.mapConstructor(FSeasonOverviewConstructor.model("constructorId2")) } returns mockConstructor("constructorId2")
    }

    //region DriverStandings

    @Test
    fun `DriverStandings maps fields correctly`() {
        initSUT()

        val input = FSeasonStatistics.model()
        val expected: tmg.flashback.formula1.model.DriverStandings = listOf(
            tmg.flashback.formula1.model.SeasonStanding(
                item = mockDriver(),
                points = 1.0,
                position = 2,
            )
        )

        assertEquals(expected, sut.mapDriverStandings(input, listOf(mockDriver())))
    }

    @Test
    fun `DriverStandings points default to 0 if not specified`() {
        initSUT()

        val input = FSeasonStatistics.model(drivers = mapOf(
            "driverId" to FSeasonStatisticsPoints.model(p = null)
        ))
        assertEquals(0.0, sut.mapDriverStandings(input, listOf(mockDriver()))[0].points)
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

        val result = sut.mapDriverStandings(input, listOf(mockDriver("driverId1")))
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

        val result = sut.mapDriverStandings(input, listOf(
            mockDriver("driverId1"),
            mockDriver("driverId2")
        ))
        assertEquals(2, result.size)
        assertEquals("driverId2", result[0].item.id)
        assertEquals(1, result[0].position)
        assertEquals("driverId1", result[1].item.id)
        assertEquals(2, result[1].position)
    }

    //endregion

    //region ConstructorStandings

    @Test
    fun `ConstructorStandings maps fields correctly`() {

        initSUT()

        val input = FSeasonStatistics.model()
        val expected: tmg.flashback.formula1.model.ConstructorStandings = listOf(
            tmg.flashback.formula1.model.SeasonStanding(
                item = tmg.flashback.formula1.model.Constructor(
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

    //endregion

}

private fun mockDriver(driverId: String = "driverId", constructorsMap: Map<Int, String> = mapOf(1 to "constructorId")): tmg.flashback.formula1.model.Driver {
    return tmg.flashback.formula1.model.Driver(
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
                key to tmg.flashback.formula1.model.Constructor(
                    id = value,
                    name = "constructorName",
                    wikiUrl = "wikiUrl",
                    nationality = "nationality",
                    nationalityISO = "nationalityISO",
                    color = 0
                )
            }
            .toMap(),
        startingConstructor = tmg.flashback.formula1.model.Constructor(
            id = "constructorId",
            name = "constructorName",
            wikiUrl = "wikiUrl",
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            color = 0,
        ),
    )
}
private fun mockConstructor(id: String = "constructorId"): tmg.flashback.formula1.model.Constructor {
    return tmg.flashback.formula1.model.Constructor(
        id = id,
        name = "constructorName",
        wikiUrl = "wikiUrl",
        nationality = "nationality",
        nationalityISO = "nationalityISO",
        color = 0
    )
}