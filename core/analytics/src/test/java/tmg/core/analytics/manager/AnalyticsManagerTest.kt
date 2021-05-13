package tmg.core.analytics.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.core.analytics.UserProperty
import tmg.core.analytics.services.AnalyticsService
import tmg.core.analytics.repository.AnalyticsRepository


internal class AnalyticsManagerTest {

    private val mockAnalyticsRepository: AnalyticsRepository = mockk(relaxed = true)
    private val mockAnalyticsService: AnalyticsService = mockk(relaxed = true)

    private lateinit var sut: AnalyticsManager

    private fun initSUT() {
        sut = AnalyticsManager(mockAnalyticsRepository, mockAnalyticsService)
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
            mockAnalyticsService.logEvent("testKey")
        }
    }

    @Test
    fun `set event called with params if enabled is true`() {
        every { mockAnalyticsRepository.isEnabled } returns true
        initSUT()

        sut.logEvent("testKey", mapOf("test" to "hello"))

        verify {
            mockAnalyticsService.logEvent("testKey", any())
        }
    }

    @Test
    fun `set event not called if enabled is false`() {

        every { mockAnalyticsRepository.isEnabled } returns false
        initSUT()

        sut.logEvent("testKey")

        verify(exactly = 0) {
            mockAnalyticsService.logEvent("testKey")
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
            mockAnalyticsService.setProperty(any(), any())
        }
    }

    @Test
    fun `set user property not called if enabled is false`() {

        every { mockAnalyticsRepository.isEnabled } returns false
        initSUT()

        sut.setUserProperty(UserProperty.APP_VERSION, "test-value")

        verify(exactly = 0) {
            mockAnalyticsService.setProperty(any(), any())
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
            mockAnalyticsService.logViewScreen(any(), any(), any())
        }
    }

    @Test
    fun `view screen not called if enabled is false`() {

        every { mockAnalyticsRepository.isEnabled } returns false
        initSUT()

        sut.viewScreen("screen", this.javaClass, emptyMap())

        verify(exactly = 0) {
            mockAnalyticsService.logViewScreen(any(), any(), any())
        }
    }

    //endregion
}