package tmg.flashback.widgets.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class WidgetRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: WidgetRepository

    private fun initUnderTest() {
        underTest = WidgetRepository(
            preferenceManager = mockPreferenceManager
        )
    }

    @Test
    fun `up next - saving show background to app widget id`() {
        initUnderTest()
        underTest.setShowBackground(1, true)

        verify {
            mockPreferenceManager.save("${WIDGET_UP_NEXT_SHOW_BACKGROUND}_1", true)
        }
    }

    @Test
    fun `up next - get show background to app widget id`() {
        every { mockPreferenceManager.getBoolean("${WIDGET_UP_NEXT_SHOW_BACKGROUND}_1", false) } returns true

        initUnderTest()
        assertTrue(underTest.getShowBackground(1))

        verify {
            mockPreferenceManager.getBoolean("${WIDGET_UP_NEXT_SHOW_BACKGROUND}_1", false)
        }
    }



    @Test
    fun `up next - saving show weather to app widget id`() {
        initUnderTest()
        underTest.setShowWeather(1, true)

        verify {
            mockPreferenceManager.save("${WIDGET_UP_NEXT_SHOW_WEATHER}_1", true)
        }
    }

    @Test
    fun `up next - get show weather to app widget id`() {
        every { mockPreferenceManager.getBoolean("${WIDGET_UP_NEXT_SHOW_WEATHER}_1", false) } returns true

        initUnderTest()
        assertTrue(underTest.getShowWeather(1))

        verify {
            mockPreferenceManager.getBoolean("${WIDGET_UP_NEXT_SHOW_WEATHER}_1", false)
        }
    }

    companion object {
        private const val WIDGET_UP_NEXT_SHOW_BACKGROUND = "widget_upnext_show_background"
        private const val WIDGET_UP_NEXT_SHOW_WEATHER = "widget_upnext_show_weather"
    }
}