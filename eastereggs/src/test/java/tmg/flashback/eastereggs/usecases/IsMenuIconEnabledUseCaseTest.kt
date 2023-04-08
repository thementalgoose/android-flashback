package tmg.flashback.eastereggs.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Year
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.eastereggs.model.MenuIcons

internal class IsMenuIconEnabledUseCaseTest {

    private val mockTimeManager: TimeManager = mockk(relaxed = true)

    private lateinit var underTest: IsMenuIconEnabledUseCase

    private fun initUnderTest() {
        underTest = IsMenuIconEnabledUseCase(
            timeManager = mockTimeManager
        )
    }

    @ParameterizedTest
    @CsvSource(
        "2,2",
        "3,3",
        "5,5",
        "6,6",
        "7,7",
        "8,8",
        "9,9",
        "11,11",
        "12,12"
    )
    fun `random dates return null menu icon`(dayOfMonth: Int, month: Int) {
        val date = LocalDateTime.of(Year.now().value, month, dayOfMonth, 12, 0)
        every { mockTimeManager.now } returns date

        initUnderTest()
        assertNull(underTest.invoke())
    }

    @ParameterizedTest
    @ValueSource(ints = [11, 12, 13, 14])
    fun `valentines is returned when now is within range`(dayOfMonth: Int) {
        every { mockTimeManager.now } returns LocalDateTime.of(Year.now().value, 2, dayOfMonth, 12, 0)

        initUnderTest()
        assertEquals(MenuIcons.VALENTINES_DAY, underTest.invoke())
    }

    @Test
    fun `new years is returned when now is within range`() {
        every { mockTimeManager.now } returns LocalDateTime.of(Year.now().value, 1, 1, 12, 0)

        initUnderTest()
        assertEquals(MenuIcons.NEW_YEARS, underTest.invoke())
    }

    @Test
    fun `new years eve is returned when now is within range`() {
        every { mockTimeManager.now } returns LocalDateTime.of(Year.now().value, 12, 31, 12, 0)

        initUnderTest()
        assertEquals(MenuIcons.NEW_YEARS_EVE, underTest.invoke())
    }

    @ParameterizedTest
    @ValueSource(ints = [9, 10])
    fun `chinese new year is returned when now is within range`(dayOfMonth: Int) {
        val year = MenuIcons.CHINESE_NEW_YEAR.start.year
        every { mockTimeManager.now } returns LocalDateTime.of(year, 2, dayOfMonth, 12, 0)

        initUnderTest()
        assertEquals(MenuIcons.CHINESE_NEW_YEAR, underTest.invoke())
    }

    @ParameterizedTest
    @CsvSource("28,3", "29,3", "30,3", "31,3", "1,4", "2,4")
    fun `easter is returned when now is within range`(dayOfMonth: Int, month: Int) {
        val year = MenuIcons.EASTER.start.year
        every { mockTimeManager.now } returns LocalDateTime.of(year, month, dayOfMonth, 12, 0)

        initUnderTest()
        assertEquals(MenuIcons.EASTER, underTest.invoke())
    }

    @ParameterizedTest
    @ValueSource(ints = [17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31])
    fun `halloween is returned when now is within range`(dayOfMonth: Int) {
        every { mockTimeManager.now } returns LocalDateTime.of(Year.now().value, 10, dayOfMonth, 12, 0)

        initUnderTest()
        assertEquals(MenuIcons.HALLOWEEN, underTest.invoke())
    }

    @ParameterizedTest
    @ValueSource(ints = [4, 5])
    fun `bonfire is returned when now is within range`(dayOfMonth: Int) {
        every { mockTimeManager.now } returns LocalDateTime.of(Year.now().value, 11, dayOfMonth, 12, 0)

        initUnderTest()
        assertEquals(MenuIcons.BONFIRE, underTest.invoke())
    }

    @ParameterizedTest
    @ValueSource(ints = [19, 20, 21, 22, 23, 24, 25])
    fun `christmas is returned when now is within range`(dayOfMonth: Int) {
        every { mockTimeManager.now } returns LocalDateTime.of(Year.now().value, 12, dayOfMonth, 12, 0)

        initUnderTest()
        assertEquals(MenuIcons.CHRISTMAS, underTest.invoke())
    }
}