package tmg.flashback.widgets.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class WidgetRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: UpNextWidgetRepository

    private fun initUnderTest() {
        underTest = UpNextWidgetRepository(
            preferenceManager = mockPreferenceManager
        )
    }

    @Test
    fun `up next - saving show background`() {
        initUnderTest()
        underTest.showBackground = true

        verify {
            mockPreferenceManager.save(keyWidgetShowBackground, true)
        }
    }

    @Test
    fun `up next - get show background`() {
        every { mockPreferenceManager.getBoolean(keyWidgetShowBackground, true) } returns true

        initUnderTest()
        assertTrue(underTest.showBackground)

        verify {
            mockPreferenceManager.getBoolean(keyWidgetShowBackground, true)
        }
    }

    @Test
    fun `up next - saving deeplink to event`() {
        initUnderTest()
        underTest.deeplinkToEvent = true

        verify {
            mockPreferenceManager.save(keyWidgetDeeplinkToEvent, true)
        }
    }

    @Test
    fun `up next - get deeplink to event`() {
        every { mockPreferenceManager.getBoolean(keyWidgetDeeplinkToEvent, false) } returns true

        initUnderTest()
        assertTrue(underTest.deeplinkToEvent)

        verify {
            mockPreferenceManager.getBoolean(keyWidgetDeeplinkToEvent, false)
        }
    }



    @Test
    fun `up next - saving show weather`() {
        initUnderTest()
        underTest.showWeather = true

        verify {
            mockPreferenceManager.save(keyWidgetShowWeather, true)
        }
    }

    @Test
    fun `up next - get show weather`() {
        every { mockPreferenceManager.getBoolean(keyWidgetShowWeather, false) } returns true

        initUnderTest()
        assertTrue(underTest.showWeather)

        verify {
            mockPreferenceManager.getBoolean(keyWidgetShowWeather, false)
        }
    }

    companion object {
        private const val keyWidgetShowBackground: String = "WIDGET_SHOW_BACKGROUND"
        private const val keyWidgetDeeplinkToEvent: String = "WIDGET_DEEPLINK_EVENT"
        private const val keyWidgetShowWeather: String = "WIDGET_SHOW_WEATHER"
    }
}