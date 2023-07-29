package tmg.flashback.googleanalytics.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.googleanalytics.UserProperty
import tmg.flashback.device.repository.PrivacyRepository
import tmg.flashback.googleanalytics.services.FireabseAnalyticsService


internal class FirebaseAnalyticsManagerTest {

    private val mockPrivacyRepository: PrivacyRepository = mockk(relaxed = true)
    private val mockFireabseAnalyticsService: FireabseAnalyticsService = mockk(relaxed = true)

    private lateinit var sut: FirebaseAnalyticsManager

    private fun initSUT() {
        sut = FirebaseAnalyticsManager(mockPrivacyRepository, mockFireabseAnalyticsService)
    }

    //region enabled

    @Test
    fun `enabled reads value from core repository`() {

        every { mockPrivacyRepository.analytics } returns true
        initSUT()

        assertTrue(sut.enabled)

        verify {
            mockPrivacyRepository.analytics
        }
    }

    @Test
    fun `enabled updates value in core repository`() {

        initSUT()

        sut.enabled = false

        verify {
            mockPrivacyRepository.analytics = false
        }
    }

    //endregion

    //region Initialising

    @Test
    fun `initialise passes user id and collection enabled to analytics service`() {
        every { mockPrivacyRepository.analytics } returns true
        initSUT()
        sut.initialise("my-user-id")
        verify {
            mockFireabseAnalyticsService.setUserId("my-user-id")
            mockFireabseAnalyticsService.setAnalyticsCollectionEnabled(true)
        }
    }

    //endregion

    //region logEvent

    @Test
    fun `set event called if enabled is true`() {

        every { mockPrivacyRepository.analytics } returns true
        initSUT()

        sut.logEvent("testKey")

        verify {
            mockFireabseAnalyticsService.logEvent("testKey")
        }
    }

    @Test
    fun `set event called with params if enabled is true`() {
        every { mockPrivacyRepository.analytics } returns true
        initSUT()

        sut.logEvent("testKey", mapOf("test" to "hello"))

        verify {
            mockFireabseAnalyticsService.logEvent("testKey", any())
        }
    }

    @Test
    fun `set event not called if enabled is false`() {

        every { mockPrivacyRepository.analytics } returns false
        initSUT()

        sut.logEvent("testKey")

        verify(exactly = 0) {
            mockFireabseAnalyticsService.logEvent("testKey")
        }
    }

    //endregion

    //region userProperty

    @Test
    fun `set user property called if enabled is true`() {

        every { mockPrivacyRepository.analytics } returns true
        initSUT()

        sut.setUserProperty(UserProperty.APP_VERSION, "test-value")

        verify {
            mockFireabseAnalyticsService.setProperty(any(), any())
        }
    }

    @Test
    fun `set user property not called if enabled is false`() {

        every { mockPrivacyRepository.analytics } returns false
        initSUT()

        sut.setUserProperty(UserProperty.APP_VERSION, "test-value")

        verify(exactly = 0) {
            mockFireabseAnalyticsService.setProperty(any(), any())
        }
    }

    //endregion

    //region viewScreen

    @Test
    fun `view screen called if enabled is true`() {

        every { mockPrivacyRepository.analytics } returns true
        initSUT()

        sut.viewScreen("screen", emptyMap(), this.javaClass)

        verify {
            mockFireabseAnalyticsService.logViewScreen(any(), any(), any())
        }
    }

    @Test
    fun `view screen not called if enabled is false`() {

        every { mockPrivacyRepository.analytics } returns false
        initSUT()

        sut.viewScreen("screen", emptyMap(), this.javaClass)

        verify(exactly = 0) {
            mockFireabseAnalyticsService.logViewScreen(any(), any(), any())
        }
    }

    //endregion
}