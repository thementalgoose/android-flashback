package tmg.flashback.firebase.mappers

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.History
import tmg.flashback.data.models.stats.HistoryRound
import tmg.flashback.data.models.stats.WinnerSeason
import tmg.flashback.data.models.stats.WinnerSeasonConstructor
import tmg.flashback.data.models.stats.WinnerSeasonDriver
import tmg.flashback.firebase.models.FHistorySeason
import tmg.flashback.firebase.models.FHistorySeasonRound
import tmg.flashback.firebase.models.FHistorySeasonWin
import tmg.flashback.firebase.models.FHistorySeasonWinConstructor
import tmg.flashback.firebase.models.FHistorySeasonWinDriver
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class HistoryMapperTest: BaseTest() {

    private val mockCrashController: CrashController = mockk(relaxed = true)

    private lateinit var sut: HistoryMapper

    private fun initSUT() {
        sut = HistoryMapper(
            2020,
            mockCrashController
        )
    }

    @Test
    fun `History maps fields correctly`() {
        initSUT()

        val input = FHistorySeason.model()
        val expected = listOf(History(
            season = 2020,
            winner = WinnerSeason(
                season = 2020,
                driver = listOf(WinnerSeasonDriver(
                    id = "driverId",
                    name = "driverName",
                    image = "driverImg",
                    points = 2
                )),
                constructor = listOf(WinnerSeasonConstructor(
                    id = "constructorId",
                    name = "constructorName",
                    color = "constructorColor",
                    points = 1
                ))
            ),
            rounds = listOf(HistoryRound(
                date = LocalDate.of(2020, 1, 1),
                round = 1,
                season = 2020,
                raceName = "name",
                circuitId = "circuitId",
                circuitName = "circuit",
                country = "country",
                countryISO = "countryISO",
                hasQualifying = true,
                hasResults = true
            ))
        ))

        assertEquals(expected, sut.mapHistory(input))
    }

    @Test
    fun `History with invalid season is ignored from list`() {
        initSUT()

        val input = FHistorySeason.model(all = mapOf(
            "s-1" to mapOf(
                "r1" to FHistorySeasonRound.model(s = -1)
            )
        ))

        assertEquals(0, sut.mapHistory(input).size)
    }

    @Test
    fun `History rounds are ignored if they are null`() {
        initSUT()

        val input = FHistorySeason.model(all = mapOf(
            "s-1" to mapOf(
                "r1" to FHistorySeasonRound.model(),
                "r2" to null
            )
        ))

        assertEquals(1, sut.mapHistory(input)[0].rounds.size)

        verify {
            mockCrashController.logException(any())
        }
    }

    @Test
    fun `HistoryRound maps fields correctly`() {
        initSUT()

        val input = FHistorySeasonRound.model()
        val expected = HistoryRound(
            date = LocalDate.of(2020, 1, 1),
            round = 1,
            season = 2020,
            raceName = "name",
            circuitId = "circuitId",
            circuitName = "circuit",
            country = "country",
            countryISO = "countryISO",
            hasQualifying = true,
            hasResults = true
        )

        assertEquals(expected, sut.mapHistoryRound(input))
    }

    @Test
    fun `HistoryRound has qualifying goes to true if data is true`() {
        initSUT()

        val input = FHistorySeasonRound.model(data = true, hasQ = null, hasR = null)
        assertTrue(sut.mapHistoryRound(input).hasQualifying)
    }

    @Test
    fun `HistoryRound has qualifying goes to true if data null and season higher than flag`() {
        initSUT()

        val input = FHistorySeasonRound.model(s = 2019, data = null, hasQ = null, hasR = null)
        assertTrue(sut.mapHistoryRound(input).hasQualifying)
    }

    @Test
    fun `HistoryRound has results goes to true if data is true`() {
        initSUT()

        val input = FHistorySeasonRound.model(data = true, hasQ = null, hasR = null)
        assertTrue(sut.mapHistoryRound(input).hasResults)
    }

    @Test
    fun `HistoryRound has results goes to true if data null and season higher than flag`() {
        initSUT()

        val input = FHistorySeasonRound.model(s = 2019, data = null, hasQ = null, hasR = null)
        assertTrue(sut.mapHistoryRound(input).hasResults)
    }

    @Test
    fun `WinnerSeason maps fields correctly`() {
        initSUT()

        val input = FHistorySeasonWin.model()
        val expected = WinnerSeason(
            season = 2020,
            driver = listOf(WinnerSeasonDriver(
                id = "driverId",
                name = "driverName",
                image = "driverImg",
                points = 2
            )),
            constructor = listOf(WinnerSeasonConstructor(
                id = "constructorId",
                name = "constructorName",
                color = "constructorColor",
                points = 1
            ))
        )

        assertEquals(expected, sut.mapWinnerSeason(input))
    }

    @Test
    fun `WinnerSeason null constructor returns empty list`() {
        initSUT()

        val input = FHistorySeasonWin.model(constr = null)
        assertEquals(0, sut.mapWinnerSeason(input).constructor.size)
    }

    @Test
    fun `WinnerSeason null drivers returns empty list`() {
        initSUT()

        val input = FHistorySeasonWin.model(driver = null)
        assertEquals(0, sut.mapWinnerSeason(input).driver.size)
    }

    @Test
    fun `WinnerSeasonDriver maps fields correctly`() {
        initSUT()

        val input = FHistorySeasonWinDriver.model()
        val expected = WinnerSeasonDriver(
            id = "driverId",
            name = "driverName",
            image = "driverImg",
            points = 2
        )

        assertEquals(expected, sut.mapWinnerSeasonDriver(input))
    }

    @Test
    fun `WinnerSeasonConstructor maps fields correctly`() {
        initSUT()

        val input = FHistorySeasonWinConstructor.model()
        val expected = WinnerSeasonConstructor(
            id = "constructorId",
            name = "constructorName",
            color = "constructorColor",
            points = 1
        )

        assertEquals(expected, sut.mapWinnerSeasonConstructor(input))
    }

}