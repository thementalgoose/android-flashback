package tmg.flashback.upnext.controllers

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.configuration.controllers.ConfigController
import tmg.configuration.repository.models.UpNextSchedule
import tmg.configuration.repository.models.UpNextScheduleTimestamp
import tmg.flashback.formula1.model.Timestamp
import tmg.testutils.BaseTest

internal class UpNextControllerTest : BaseTest() {

    private var mockRemoteConfigRepository: ConfigController = mockk(relaxed = true)
//    private var mockCoreRepository: CoreRepository = mockk(relaxed = true)

    private lateinit var sut: UpNextController

    private fun initSUT() {
        sut = UpNextController(mockRemoteConfigRepository)
    }

    @Test
    fun `no next race if remote config is empty`() {
        every { mockRemoteConfigRepository.upNext } returns emptyList()
        initSUT()

        assertNull(sut.getNextEvent())
    }

    @Test
    fun `no next up next if all timestamps are in the past`() {
        val list = listOf(
            generateUpNextItem(-1 to "12:00", -1 to "10:00"),
            generateUpNextItem(-2 to "12:00", -1 to "10:00")
        )

        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertNull(sut.getNextEvent())
    }

    @Test
    fun `last event shown when last timestamp is available`() {
        val expected = generateUpNextItem(0 to "12:00", -1 to "12:00")
        val list = listOf(
            expected,
            generateUpNextItem(3 to "10:00")
        )
        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    @Test
    fun `last event shown when first last timestamp is available`() {
        val expected = generateUpNextItem(0 to "01:00", 2 to "12:00")
        val list = listOf(
            expected,
            generateUpNextItem(-1 to "10:00")
        )
        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    @Test
    fun `last event shown when date is between between two last view timestamps`() {
        val expected = generateUpNextItem(1 to "01:00", 2 to "12:00")
        val list = listOf(
            expected,
            generateUpNextItem(-1 to "10:00")
        )
        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    fun `second last event shown when last of second item is shown with last all in future`() {
        val expected = generateUpNextItem(-1 to "01:00", 0 to "12:00")
        val list = listOf(
            generateUpNextItem(-3 to "10:00"),
            expected
        )
        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    @Test
    fun `list item correct item in middle list of all`() {
        val expected = generateUpNextItem(0 to "12:00")
        val list = listOf(
            generateUpNextItem(-4 to "12:00"),
            generateUpNextItem(-2 to "11:00"),
            expected,
            generateUpNextItem(1 to "11:00"),
            generateUpNextItem(2 to "11:00")
        )

        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertEquals(expected, sut.getNextEvent())
    }

    //region Race simulations

    @Test
    fun `list item is correct when simulating race weekend on race day`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
            generateUpNextItem(
                -2 to "11:00",
                -2 to "15:00",
                -1 to "12:00",
                -1 to "15:00",
                0 to "15:00"
            ),
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        every { mockRemoteConfigRepository.upNext } returns list

        assertEquals(list[1], sut.getNextEvent())
    }

    @Test
    fun `list item is correct when simulating race weekend on saturday (qualifying + fp3)`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
            generateUpNextItem(
                -1 to "11:00",
                -1 to "15:00",
                0 to "12:00",
                0 to "15:00",
                1 to "15:00"
            ),
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        every { mockRemoteConfigRepository.upNext } returns list

        assertEquals(list[1], sut.getNextEvent())
    }

    @Test
    fun `list item is correct when simulating race weekend on friday (fp1 + fp2)`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
            generateUpNextItem(
                0 to "11:00",
                0 to "15:00",
                1 to "12:00",
                1 to "15:00",
                2 to "15:00"
            ),
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        every { mockRemoteConfigRepository.upNext } returns list

        assertEquals(list[1], sut.getNextEvent())
    }

    @Test
    fun `list item is correct when simulating race weekend on thursday (coming up)`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
            generateUpNextItem(
                1 to "11:00",
                1 to "15:00",
                2 to "12:00",
                2 to "15:00",
                3 to "15:00"
            ),
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        every { mockRemoteConfigRepository.upNext } returns list

        assertEquals(list[1], sut.getNextEvent())
    }

    @Test
    fun `list item is correct when simulating race weekend on monday (coming up)`() {
        val list = listOf(
            generateUpNextItem(
                -9 to "11:00",
                -9 to "15:00",
                -8 to "12:00",
                -8 to "15:00",
                -7 to "15:00"
            ),
            generateUpNextItem(
                -3 to "11:00",
                -3 to "15:00",
                -2 to "12:00",
                -2 to "15:00",
                -1 to "15:00"
            ),
            generateUpNextItem(
                5 to "11:00",
                5 to "15:00",
                6 to "12:00",
                6 to "15:00",
                7 to "15:00"
            )
        )
        initSUT()
        every { mockRemoteConfigRepository.upNext } returns list

        assertEquals(list[2], sut.getNextEvent())
    }

    //endregion

    //region Up Next Display Type

//    @Test
//    fun `up next display type reads value from core`() {
//        every { mockCoreRepository.displayListTypePref } returns TimeListDisplayType.LOCAL
//        initSUT()
//
//        assertEquals(TimeListDisplayType.LOCAL, sut.upNextDisplayType)
//    }
//
//    @Test
//    fun `up next display type writes value to core when updated`() {
//        every { mockCoreRepository.displayListTypePref } returns TimeListDisplayType.LOCAL
//        initSUT()
//        sut.upNextDisplayType = TimeListDisplayType.RELATIVE
//
//        verify {
//            mockCoreRepository.displayListTypePref = TimeListDisplayType.RELATIVE
//        }
//    }

    //endregion

    private fun generateUpNextItem(vararg delta: Pair<Int, String>): UpNextSchedule {
        return UpNextSchedule(
            season = 1,
            round = 3,
            title = "Up next item $delta",
            subtitle = null,
            values = delta.map {
                UpNextScheduleTimestamp(
                    label = "Example ${it.second} ${it.first}",
                    timestamp = Timestamp(
                        LocalDate.now().plusDays(it.first.toLong()),
                        LocalTime.parse(it.second, DateTimeFormatter.ofPattern("HH:mm"))
                    )
                )
            },
            flag = null,
            circuitId = null
        )
    }

}