package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.configuration.controllers.ConfigController

internal class FeatureControllerTest {

    private var mockConfigurationController: ConfigController = mockk(relaxed = true)

    private lateinit var sut: tmg.flashback.controllers.FeatureController

    private fun initSUT() {
        sut = tmg.flashback.controllers.FeatureController(mockConfigurationController)
    }

    //region RSS

    @Test
    fun `rss enabled`() {
        every { mockConfigurationController.rss } returns true
        initSUT()
        assertTrue(sut.rssEnabled)
        verify { mockConfigurationController.rss }
    }

    @Test
    fun `rss disabled`() {
        every { mockConfigurationController.rss } returns false
        initSUT()
        assertFalse(sut.rssEnabled)
        verify { mockConfigurationController.rss }
    }

    //endregion

    //region Dashboard Calendar

    @Test
    fun `dashboard calendar enabled`() {
        every { mockConfigurationController.dashboardCalendar } returns true
        initSUT()
        assertTrue(sut.calendarDashboardEnabled)
        verify { mockConfigurationController.dashboardCalendar }
    }

    @Test
    fun `dashboard calendar disabled`() {
        every { mockConfigurationController.dashboardCalendar } returns false
        initSUT()
        assertFalse(sut.calendarDashboardEnabled)
        verify { mockConfigurationController.dashboardCalendar }
    }

    //endregion
}