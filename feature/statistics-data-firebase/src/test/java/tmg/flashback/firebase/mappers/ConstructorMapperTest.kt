package tmg.flashback.firebase.mappers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.ConstructorDriver
import tmg.flashback.data.models.stats.ConstructorOverview
import tmg.flashback.data.models.stats.ConstructorOverviewDriverStanding
import tmg.flashback.data.models.stats.ConstructorOverviewStanding
import tmg.flashback.firebase.currentYear
import tmg.flashback.firebase.models.FConstructorOverview
import tmg.flashback.firebase.models.FConstructorOverviewData
import tmg.flashback.firebase.models.FConstructorOverviewDrivers
import tmg.flashback.firebase.models.FConstructorOverviewStandings
import tmg.flashback.firebase.models.FConstructorOverviewStandingsDriver
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class ConstructorMapperTest: BaseTest() {

    private lateinit var sut: ConstructorMapper

    private fun initSUT() {
        sut = ConstructorMapper()
    }

    @Test
    fun `ConstructorOverview maps fields correctly`() {
        initSUT()

        val input = FConstructorOverview.model()
        val expected = ConstructorOverview(
            id = "constructorId",
            name = "constructorName",
            wikiUrl = "wikiUrl",
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            color = 0,
            standings = listOf(
                ConstructorOverviewStanding(
                    drivers = mapOf(
                        "driverId" to ConstructorOverviewDriverStanding(
                            driver = ConstructorDriver(
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
                                constructor = Constructor(
                                    id = "constructorId",
                                    name = "constructorName",
                                    wikiUrl = "wikiUrl",
                                    nationality = "nationality",
                                    nationalityISO = "nationalityISO",
                                    color = 0
                                )
                            ),
                            bestFinish = 1,
                            bestQualifying = 1,
                            points = 25.0,
                            finishesInP1 = 1,
                            finishesInP2 = 2,
                            finishesInP3 = 3,
                            finishesInPoints = 10,
                            qualifyingP1 = 1,
                            qualifyingP2 = 2,
                            qualifyingP3 = 3,
                            qualifyingTop10 = 10,
                            races = 1,
                            championshipStanding = 2
                        )
                    ),
                    isInProgress = false,
                    championshipStanding = 1,
                    points = 10.0,
                    season = 2020,
                    races = 1
                )
            )
        )

        assertEquals(expected, sut.mapConstructorOverview(input))
    }

    @Test
    fun `ConstructorOverview standings is empty if drivers is null`() {
        initSUT()

        val input = FConstructorOverview.model(drivers = null)
        assertEquals(0, sut.mapConstructorOverview(input).standings.size)
    }

    @Test
    fun `ConstructorOverview standings is empty if drivers is empty`() {
        initSUT()

        val input = FConstructorOverview.model(drivers = emptyMap())
        assertEquals(0, sut.mapConstructorOverview(input).standings.size)
    }

    @Test
    fun `ConstructorOverview standings is empty if standings is null`() {
        initSUT()

        val input = FConstructorOverview.model(standings = null)
        assertEquals(0, sut.mapConstructorOverview(input).standings.size)
    }

    @Test
    fun `ConstructorOverview standings is empty if standings is empty`() {
        initSUT()

        val input = FConstructorOverview.model(standings = emptyMap())
        assertEquals(0, sut.mapConstructorOverview(input).standings.size)
    }

    @Test
    fun `ConstructorOverviewStanding maps fields correctly`() {
        initSUT()

        val input = FConstructorOverviewStandings.model()
        val inputDrivers = mapOf(
            "driverId" to FConstructorOverviewDrivers.model()
        )
        val expected = ConstructorOverviewStanding(
            drivers = mapOf(
                "driverId" to ConstructorOverviewDriverStanding(
                    driver = ConstructorDriver(
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
                        constructor = Constructor(
                            id = "constructorId",
                            name = "constructorName",
                            wikiUrl = "wikiUrl",
                            nationality = "nationality",
                            nationalityISO = "nationalityISO",
                            color = 0
                        )
                    ),
                    bestFinish = 1,
                    bestQualifying = 1,
                    points = 25.0,
                    finishesInP1 = 1,
                    finishesInP2 = 2,
                    finishesInP3 = 3,
                    finishesInPoints = 10,
                    qualifyingP1 = 1,
                    qualifyingP2 = 2,
                    qualifyingP3 = 3,
                    qualifyingTop10 = 10,
                    races = 1,
                    championshipStanding = 2
                )
            ),
            isInProgress = false,
            championshipStanding = 1,
            points = 10.0,
            season = 2020,
            races = 1
        )

        assertEquals(expected, sut.mapConstructorOverviewStandings(input, FConstructorOverviewData.model(), inputDrivers))
    }

    @Test
    fun `ConstructorOverviewStanding drivers is empty if input is null`() {
        initSUT()

        val input = FConstructorOverviewStandings.model(drivers = null)
        assertEquals(0, sut.mapConstructorOverviewStandings(input, FConstructorOverviewData.model(), emptyMap()).drivers.size)
    }

    @Test
    fun `ConstructorOverviewStanding driver is not included if id in map is not in complete list of drivers`() {
        initSUT()

        val input = FConstructorOverviewStandings.model(drivers = mapOf(
            "invalidDriverId" to FConstructorOverviewStandingsDriver.model()
        ))
        val inputDrivers = mapOf(
            "driverId" to FConstructorOverviewDrivers.model()
        )
        assertEquals(0, sut.mapConstructorOverviewStandings(input, FConstructorOverviewData.model(), inputDrivers).drivers.size)
    }

    @Test
    fun `ConstructorOverviewStanding championship standing defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandings.model(championshipStanding = null)
        assertEquals(0, sut.mapConstructorOverviewStandings(input, FConstructorOverviewData.model(), emptyMap()).championshipStanding)
    }

    @Test
    fun `ConstructorOverviewStanding points defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandings.model(p = null)
        assertEquals(0.0, sut.mapConstructorOverviewStandings(input, FConstructorOverviewData.model(), emptyMap()).points)
    }

    @Test
    fun `ConstructorOverviewStanding races defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandings.model(races = null)
        assertEquals(0, sut.mapConstructorOverviewStandings(input, FConstructorOverviewData.model(), emptyMap()).races)
    }

    @Test
    fun `ConstructorOverviewStanding is in progress is false if season is in before current year`() {
        initSUT()

        val input = FConstructorOverviewStandings.model(s = currentYear - 1, inProgress = true)
        assertFalse(sut.mapConstructorOverviewStandings(input, FConstructorOverviewData.model(), emptyMap()).isInProgress)
    }

    @Test
    fun `ConstructorOverviewStanding is in progress is input value if current year equal current season`() {
        initSUT()

        val input = FConstructorOverviewStandings.model(s = currentYear, inProgress = true)
        assertTrue(sut.mapConstructorOverviewStandings(input, FConstructorOverviewData.model(), emptyMap()).isInProgress)
    }

    @Test
    fun `ConstructorOverviewStanding is in progress is input value if current year greater than current season`() {
        initSUT()

        val input = FConstructorOverviewStandings.model(s = currentYear + 1, inProgress = true)
        assertTrue(sut.mapConstructorOverviewStandings(input, FConstructorOverviewData.model(), emptyMap()).isInProgress)
    }

    @Test
    fun `ConstructorOverviewDriverStanding maps fields correctly`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model()
        val expectedDriver = ConstructorDriver(
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
            constructor = Constructor(
                id = "constructorId",
                name = "constructorName",
                wikiUrl = "wikiUrl",
                nationality = "nationality",
                nationalityISO = "nationalityISO",
                color = 0
            )
        )
        val expected = ConstructorOverviewDriverStanding(
            driver = expectedDriver,
            bestFinish = 1,
            bestQualifying = 1,
            points = 25.0,
            finishesInP1 = 1,
            finishesInP2 = 2,
            finishesInP3 = 3,
            finishesInPoints = 10,
            qualifyingP1 = 1,
            qualifyingP2 = 2,
            qualifyingP3 = 3,
            qualifyingTop10 = 10,
            races = 1,
            championshipStanding = 2
        )

        assertEquals(expected, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()))
    }

    @Test
    fun `ConstructorOverviewDriverStanding best finish defaults to -1 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(bF = null)
        assertEquals(-1, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).bestFinish)
    }

    @Test
    fun `ConstructorOverviewDriverStanding best qualifying defaults to -1 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(bQ = null)
        assertEquals(-1, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).bestQualifying)
    }

    @Test
    fun `ConstructorOverviewDriverStanding points defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(p = null)
        assertEquals(0.0, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).points)
    }

    @Test
    fun `ConstructorOverviewDriverStanding finishes in p1 defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(p1 = null)
        assertEquals(0, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).finishesInP1)
    }

    @Test
    fun `ConstructorOverviewDriverStanding finishes in p2 defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(p2 = null)
        assertEquals(0, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).finishesInP2)
    }

    @Test
    fun `ConstructorOverviewDriverStanding finishes in p3 defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(p3 = null)
        assertEquals(0, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).finishesInP3)
    }

    @Test
    fun `ConstructorOverviewDriverStanding finishes in points defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(pF = null)
        assertEquals(0, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).finishesInPoints)
    }

    @Test
    fun `ConstructorOverviewDriverStanding qualifying p1 defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(q1 = null)
        assertEquals(0, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).qualifyingP1)
    }

    @Test
    fun `ConstructorOverviewDriverStanding qualifying p2 defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(q2 = null)
        assertEquals(0, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).qualifyingP2)
    }

    @Test
    fun `ConstructorOverviewDriverStanding qualifying p3 defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(q3 = null)
        assertEquals(0, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).qualifyingP3)
    }

    @Test
    fun `ConstructorOverviewDriverStanding races defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(races = null)
        assertEquals(0, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).races)
    }

    @Test
    fun `ConstructorOverviewDriverStanding championship standing defaults to -1 if null`() {
        initSUT()

        val input = FConstructorOverviewStandingsDriver.model(pos = null)
        assertEquals(-1, sut.mapConstructorOverviewDriverStanding(input, FConstructorOverviewDrivers.model(), FConstructorOverviewData.model()).championshipStanding)
    }

    @Test
    fun `ConstructorDriver maps fields correctly`() {
        initSUT()

        val input = FConstructorOverviewDrivers.model()
        val expected = ConstructorDriver(
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
            constructor = Constructor(
                id = "constructorId",
                name = "constructorName",
                nationality = "nationality",
                nationalityISO = "nationalityISO",
                wikiUrl = "wikiUrl",
                color = 0
            )
        )

        assertEquals(expected, sut.mapConstructorDriver(input, FConstructorOverviewData.model()))
    }

    @Test
    fun `ConstructorDriver driver number defaults to 0 if null`() {
        initSUT()

        val input = FConstructorOverviewDrivers.model(driverNumber = null)
        assertEquals(0, sut.mapConstructorDriver(input, FConstructorOverviewData.model()).number)
    }

    @Test
    fun `ConstructorDriver driver number defaults to 0 if invalid string`() {
        initSUT()

        val input = FConstructorOverviewDrivers.model(driverNumber = "invalid")
        assertEquals(0, sut.mapConstructorDriver(input, FConstructorOverviewData.model()).number)
    }

    @Test
    fun `ConstructorDriver date of birth throws exception if dob is invalid`() {
        initSUT()

        val input = FConstructorOverviewDrivers.model(dob = "invalid")
        assertThrows(DateTimeParseException::class.java) {
            sut.mapConstructorDriver(input, FConstructorOverviewData.model())
        }
    }

    @Test
    fun `Constructor maps fields correctly`() {
        initSUT()

        val input = FConstructorOverviewData.model()
        val expected = Constructor(
            id = "constructorId",
            name = "constructorName",
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            wikiUrl = "wikiUrl",
            color = 0
        )

        assertEquals(expected, sut.mapConstructor(input))
    }
}