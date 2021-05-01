package tmg.analytics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.analytics.UserProperty
import tmg.analytics.managers.AnalyticsManager
import tmg.analytics.repository.AnalyticsRepository


internal class AnalyticsControllerTest {

    private val mockAnalyticsRepository: AnalyticsRepository = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)

    private lateinit var sut: AnalyticsController

    private fun initSUT() {
        sut = AnalyticsController(mockAnalyticsRepository, mockAnalyticsManager)
    }

    //region enabled

    @Test
    fun `enabled reads value from core repository`() {

        every { mockAnalyticsRepository.isEnabled } returns true
        initSUT()

        assertTrue(sut.enabled)

        verify {
            mockAnalyticsRepository.isEnabled
        }
    }

    @Test
    fun `enabled updates value in core repository`() {

        initSUT()

        sut.enabled = false

        verify {
            mockAnalyticsRepository.isEnabled = false
        }
    }

    //endregion

    //region logEvent

    @Test
    fun `set event called if enabled is true`() {

        every { mockAnalyticsRepository.isEnabled } returns true
        initSUT()

        sut.logEvent("testKey")

        verify {
            mockAnalyticsManager.logEvent("testKey")
        }
    }

    @Test
    fun `set event called with params if enabled is true`() {
        every { mockAnalyticsRepository.isEnabled } returns true
        initSUT()

        sut.logEvent("testKey", mapOf("test" to "hello"))

        verify {
            mockAnalyticsManager.logEvent("testKey", any())
        }
    }

    @Test
    fun `set event not called if enabled is false`() {

        every { mockAnalyticsRepository.isEnabled } returns false
        initSUT()

        sut.logEvent("testKey")

        verify(exactly = 0) {
            mockAnalyticsManager.logEvent("testKey")
        }
    }

    //endregion

    //region userProperty

    @Test
    fun `set user property called if enabled is true`() {

        every { mockAnalyticsRepository.isEnabled } returns true
        initSUT()

        sut.setUserProperty(UserProperty.APP_VERSION, "test-value")

        verify {
            mockAnalyticsManager.setProperty(any(), any())
        }
    }

    @Test
    fun `set user property not called if enabled is false`() {

        every { mockAnalyticsRepository.isEnabled } returns false
        initSUT()

        sut.setUserProperty(UserProperty.APP_VERSION, "test-value")

        verify(exactly = 0) {
            mockAnalyticsManager.setProperty(any(), any())
        }
    }

    //endregion

    //region viewScreen

    @Test
    fun `view screen called if enabled is true`() {

        every { mockAnalyticsRepository.isEnabled } returns true
        initSUT()

        sut.viewScreen("screen", this.javaClass, emptyMap())

        verify {
            mockAnalyticsManager.logViewScreen(any(), any(), any())
        }
    }

    @Test
    fun `view screen not called if enabled is false`() {

        every { mockAnalyticsRepository.isEnabled } returns false
        initSUT()

        sut.viewScreen("screen", this.javaClass, emptyMap())

        verify(exactly = 0) {
            mockAnalyticsManager.logViewScreen(any(), any(), any())
        }
    }

    //endregion
}