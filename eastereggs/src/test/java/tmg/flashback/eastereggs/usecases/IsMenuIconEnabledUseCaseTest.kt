package tmg.flashback.eastereggs.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import org.junit.runners.Parameterized.Parameter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Year
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.eastereggs.model.MenuKeys
import tmg.flashback.eastereggs.repository.EasterEggsRepository

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
        "1,1",
        "2,2",
        "3,3",
        "5,5",
        "6,6",
        "7,7",
        "8,8",
        "9,9",
        "11,11",
        "31,12",
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
        assertEquals(MenuKeys.VALENTINES_DAY, underTest.invoke())
    }

    @ParameterizedTest
    @ValueSource(ints = [4, 5, 6, 7, 8, 9])
    fun `easter is returned when now is within range`(dayOfMonth: Int) {
        val year = MenuKeys.EASTER.start.year
        every { mockTimeManager.now } returns LocalDateTime.of(year, 4, dayOfMonth, 12, 0)

        initUnderTest()
        assertEquals(MenuKeys.EASTER, underTest.invoke())
    }

    @ParameterizedTest
    @ValueSource(ints = [17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31])
    fun `halloween is returned when now is within range`(dayOfMonth: Int) {
        every { mockTimeManager.now } returns LocalDateTime.of(Year.now().value, 10, dayOfMonth, 12, 0)

        initUnderTest()
        assertEquals(MenuKeys.HALLOWEEN, underTest.invoke())
    }

    @ParameterizedTest
    @ValueSource(ints = [19, 20, 21, 22, 23, 24, 25])
    fun `christmas is returned when now is within range`(dayOfMonth: Int) {
        every { mockTimeManager.now } returns LocalDateTime.of(Year.now().value, 12, dayOfMonth, 12, 0)

        initUnderTest()
        assertEquals(MenuKeys.CHRISTMAS, underTest.invoke())
    }
}