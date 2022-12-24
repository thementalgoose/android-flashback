package tmg.flashback.eastereggs.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.TimeManager

internal class IsValentinesDayEnabledUseCaseTest {

    private val mockTimeManager: TimeManager = mockk(relaxed = true)

    private lateinit var underTest: IsValentinesDayEnabledUseCase

    private fun initUnderTest() {
        underTest = IsValentinesDayEnabledUseCase(
            timeManager = mockTimeManager
        )
    }

    @ParameterizedTest(name = "on {0}/{1}, is valentines day easter egg enabled = {2}")
    @CsvSource(
        "11,2,false",
        "12,2,true",
        "13,2,true",
        "14,2,true",
        "15,2,false",
        "1,1,false",
        "30,12,false"
    )
    fun `is valentines day enabled for expected range`(dayOfMonth: Int, month: Int, expected: Boolean) {
        val date = LocalDate.of(2022, month, dayOfMonth)
        every { mockTimeManager.now } returns date.atTime(12, 0 ,0)

        initUnderTest()
        assertEquals(expected, underTest.invoke())
    }
}