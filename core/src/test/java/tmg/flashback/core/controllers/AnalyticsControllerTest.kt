package tmg.flashback.core.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.core.enums.UserProperty
import tmg.flashback.core.managers.AnalyticsManager
import tmg.flashback.core.repositories.CoreRepository

internal class AnalyticsControllerTest {

    private val mockCoreRepository: CoreRepository = mockk(relaxed = true)
    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)

    private lateinit var sut: AnalyticsController

    private fun initSUT() {
        sut = AnalyticsController(mockCoreRepository, mockAnalyticsManager)
    }

    @Test
    fun `enabled reads value from core repository`() {

        every { mockCoreRepository.analytics } returns true
        initSUT()

        assertTrue(sut.enabled)

        verify {
            mockCoreRepository.analytics
        }
    }

    @Test
    fun `enabled updates value in core repository`() {

        initSUT()

        sut.enabled = false

        verify {
            mockCoreRepository.analytics = false
        }
    }

    @Test
    fun `set user property called if enabled is true`() {

        every { mockCoreRepository.analytics } returns true
        initSUT()

        sut.setUserProperty(UserProperty.APP_VERSION, "test-value")

        verify {
            mockAnalyticsManager.setProperty(any(), any())
        }
    }

    @Test
    fun `set user property not called if enabled is false`() {

        every { mockCoreRepository.analytics } returns false
        initSUT()

        sut.setUserProperty(UserProperty.APP_VERSION, "test-value")

        verify(exactly = 0) {
            mockAnalyticsManager.setProperty(any(), any())
        }
    }

    @Test
    fun `view screen called if enabled is true`() {

        every { mockCoreRepository.analytics } returns true
        initSUT()

        sut.viewScreen("screen", this.javaClass, emptyMap())

        verify {
            mockAnalyticsManager.logViewScreen(any(), any(), any())
        }
    }

    @Test
    fun `view screen not called if enabled is false`() {

        every { mockCoreRepository.analytics } returns false
        initSUT()

        sut.viewScreen("screen", this.javaClass, emptyMap())

        verify(exactly = 0) {
            mockAnalyticsManager.logViewScreen(any(), any(), any())
        }
    }
}