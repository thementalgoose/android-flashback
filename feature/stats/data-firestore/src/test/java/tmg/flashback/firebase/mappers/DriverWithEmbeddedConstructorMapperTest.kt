package tmg.flashback.firebase.mappers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.firebase.currentYear
import tmg.flashback.firebase.models.FDriverOverview
import tmg.flashback.firebase.models.FDriverOverviewDriver
import tmg.flashback.firebase.models.FDriverOverviewStanding
import tmg.flashback.firebase.models.FDriverOverviewStandingConstructor
import tmg.flashback.firebase.models.FDriverOverviewStandingHistory
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class DriverWithEmbeddedConstructorMapperTest: BaseTest() {

    private lateinit var sut: DriverMapper

    private fun initSUT() {
        sut = DriverMapper()
    }

    @Test
    fun `DriverOverview maps fields correctly`() {
        initSUT()

        val input = FDriverOverview.model()
        val expected = tmg.flashback.formula1.model.DriverHistory(
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
            standings = listOf(
                tmg.flashback.formula1.model.DriverHistorySeason(
                    bestFinish = 1,
                    bestFinishQuantity = 4,
                    bestQualifying = 2,
                    bestQualifyingQuantity = 5,
                    championshipStanding = 1,
                    isInProgress = false,
                    points = 25.0,
                    podiums = 1,
                    races = 1,
                    season = 2020,
                    wins = 5,
                    constructors = listOf(
                        tmg.flashback.formula1.model.SlimConstructor(
                            id = "constructorId",
                            name = "constructorName",
                            color = 0
                        )
                    ),
                    raceOverview = listOf(
                        tmg.flashback.formula1.model.DriverHistorySeasonRace(
                            status = "1",
                            finished = 1,
                            points = 25.0,
                            qualified = 1,
                            round = 1,
                            season = 2020,
                            raceName = "raceName",
                            date = LocalDate.of(2020, 1, 1),
                            constructor = tmg.flashback.formula1.model.SlimConstructor(
                                id = "constructorId",
                                name = "constructorName",
                                color = 0
                            ),
                            circuitName = "circuitName",
                            circuitId = "circuitId",
                            circuitNationality = "circuitCountry",
                            circuitNationalityISO = "circuitCountryISO",
                        )
                    )
                )
            )
        )

        assertEquals(expected, sut.mapDriverOverview(input))
    }

    @Test
    fun `DriverOverview code takes first 3 of surname if driver code not supplied`() {
        initSUT()

        val input = FDriverOverview.model(driver = FDriverOverviewDriver.model(
            driverCode = null
        ))
        assertEquals("LAS", sut.mapDriverOverview(input).code)
    }

    @Test
    fun `DriverOverview driver number is 0 if number is invalid`() {
        initSUT()

        val input = FDriverOverview.model(driver = FDriverOverviewDriver.model(
            driverNumber = "invalid"
        ))
        assertEquals(0, sut.mapDriverOverview(input).number)
    }

    @Test
    fun `DriverOverview invalid dob throws exception`() {
        initSUT()

        val input = FDriverOverview.model(driver = FDriverOverviewDriver.model(
            dob = "invalid"
        ))

        assertThrows(DateTimeParseException::class.java) {
            sut.mapDriverOverview(input)
        }
    }

    @Test
    fun `DriverOverview standings are empty is null`() {
        initSUT()

        val input = FDriverOverview.model(standings = null)
        assertEquals(0, sut.mapDriverOverview(input).standings.size)
    }

    @Test
    fun `DriverOverviewStanding maps fields correctly`() {
        initSUT()

        val input = FDriverOverviewStanding.model()
        val expected = tmg.flashback.formula1.model.DriverHistorySeason(
            bestFinish = 1,
            bestFinishQuantity = 4,
            bestQualifying = 2,
            bestQualifyingQuantity = 5,
            championshipStanding = 1,
            isInProgress = false,
            points = 25.0,
            podiums = 1,
            races = 1,
            season = 2020,
            wins = 5,
            constructors = listOf(
                tmg.flashback.formula1.model.SlimConstructor(
                    id = "constructorId",
                    name = "constructorName",
                    color = 0
                )
            ),
            raceOverview = listOf(
                tmg.flashback.formula1.model.DriverHistorySeasonRace(
                    status = "1",
                    finished = 1,
                    points = 25.0,
                    qualified = 1,
                    round = 1,
                    season = 2020,
                    raceName = "raceName",
                    date = LocalDate.of(2020, 1, 1),
                    constructor = tmg.flashback.formula1.model.SlimConstructor(
                        id = "constructorId",
                        name = "constructorName",
                        color = 0
                    ),
                    circuitName = "circuitName",
                    circuitId = "circuitId",
                    circuitNationality = "circuitCountry",
                    circuitNationalityISO = "circuitCountryISO",
                )
            )
        )

        assertEquals(expected, sut.mapDriverOverviewStanding(input))
    }

    @Test
    fun `DriverOverviewStanding best finish defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStanding.model(bestFinish = null)
        assertEquals(0, sut.mapDriverOverviewStanding(input).bestFinish)
    }

    @Test
    fun `DriverOverviewStanding best finish quantity defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStanding.model(bestFinishQuantity = null)
        assertEquals(0, sut.mapDriverOverviewStanding(input).bestFinishQuantity)
    }

    @Test
    fun `DriverOverviewStanding best qualifying defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStanding.model(bestQualifying = null)
        assertEquals(0, sut.mapDriverOverviewStanding(input).bestQualifying)
    }

    @Test
    fun `DriverOverviewStanding best qualifying quantity defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStanding.model(bestQualifyingQuantity = null)
        assertEquals(0, sut.mapDriverOverviewStanding(input).bestQualifyingQuantity)
    }

    @Test
    fun `DriverOverviewStanding championshipStanding defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStanding.model(championshipStanding = null)
        assertEquals(0, sut.mapDriverOverviewStanding(input).championshipStanding)
    }

    @Test
    fun `DriverOverviewStanding is in progress is false if season is in before current year`() {
        initSUT()

        val input = FDriverOverviewStanding.model(s = currentYear - 1, inProgress = true)
        assertFalse(sut.mapDriverOverviewStanding(input).isInProgress)
    }

    @Test
    fun `DriverOverviewStanding is in progress is input value if current year equal current season`() {
        initSUT()

        val input = FDriverOverviewStanding.model(s = currentYear, inProgress = true)
        assertTrue(sut.mapDriverOverviewStanding(input).isInProgress)
    }

    @Test
    fun `DriverOverviewStanding is in progress is input value if current year greater than current season`() {
        initSUT()

        val input = FDriverOverviewStanding.model(s = currentYear + 1, inProgress = true)
        assertTrue(sut.mapDriverOverviewStanding(input).isInProgress)
    }

    @Test
    fun `DriverOverviewStanding points defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStanding.model(p = null)
        assertEquals(0.0, sut.mapDriverOverviewStanding(input).points)
    }

    @Test
    fun `DriverOverviewStanding podiums defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStanding.model(podiums = null)
        assertEquals(0, sut.mapDriverOverviewStanding(input).podiums)
    }

    @Test
    fun `DriverOverviewStanding races defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStanding.model(races = null)
        assertEquals(0, sut.mapDriverOverviewStanding(input).races)
    }

    @Test
    fun `DriverOverviewRace maps fields correctly`() {
        initSUT()

        val input = FDriverOverviewStandingHistory.model(
            con = "constructorId2"
        )
        val inputSeason = 2020
        val inputConstructors = listOf(
            FDriverOverviewStandingConstructor.model(),
            FDriverOverviewStandingConstructor.model(
                id = "constructorId2",
                name = "constructorName2"
            )
        )
        val expected = tmg.flashback.formula1.model.DriverHistorySeasonRace(
            status = "1",
            finished = 1,
            points = 25.0,
            qualified = 1,
            round = 1,
            season = inputSeason,
            raceName = "raceName",
            date = LocalDate.of(2020, 1, 1),
            constructor = tmg.flashback.formula1.model.SlimConstructor(
                id = "constructorId2",
                name = "constructorName2",
                color = 0
            ),
            circuitName = "circuitName",
            circuitId = "circuitId",
            circuitNationality = "circuitCountry",
            circuitNationalityISO = "circuitCountryISO",
        )

        assertEquals(expected, sut.mapDriverOverviewRace(2020, inputConstructors, input))
    }

    @Test
    fun `DriverOverviewRace with empty constructors sets constructor to null`() {
        initSUT()

        val input = FDriverOverviewStandingHistory.model()
        assertNull(sut.mapDriverOverviewRace(2020, emptyList(), input).constructor)
    }

    @Test
    fun `DriverOverviewRace with 1 constructor sets constructor to same instance with diff id`() {
        initSUT()

        val input = FDriverOverviewStandingHistory.model()
        val expected = tmg.flashback.formula1.model.SlimConstructor(
            id = "diff",
            name = "constructorName",
            color = 0
        )
        val constructor = FDriverOverviewStandingConstructor.model(id = "diff")
        assertEquals(expected, sut.mapDriverOverviewRace(2020, listOf(constructor), input).constructor)
    }

    @Test
    fun `DriverOverviewRace finished defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStandingHistory.model(f = null)
        assertEquals(0, sut.mapDriverOverviewRace(2020, emptyList(), input).finished)
    }

    @Test
    fun `DriverOverviewRace points defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStandingHistory.model(p = null)
        assertEquals(0.0, sut.mapDriverOverviewRace(2020, emptyList(), input).points)
    }

    @Test
    fun `DriverOverviewRace qualified defaults to 0 if null`() {
        initSUT()

        val input = FDriverOverviewStandingHistory.model(q = null)
        assertEquals(0, sut.mapDriverOverviewRace(2020, emptyList(), input).qualified)
    }

    @Test
    fun `DriverOverviewRace race name defaults to empty string if null`() {
        initSUT()

        val input = FDriverOverviewStandingHistory.model(rName = null)
        assertEquals("", sut.mapDriverOverviewRace(2020, emptyList(), input).raceName)
    }

    @Test
    fun `DriverOverviewRace status defaults to empty string if null`() {
        initSUT()

        val input = FDriverOverviewStandingHistory.model(fStatus = null)
        assertEquals("Unknown", sut.mapDriverOverviewRace(2020, emptyList(), input).status)
    }
}