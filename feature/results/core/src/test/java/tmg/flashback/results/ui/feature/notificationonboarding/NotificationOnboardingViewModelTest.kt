package tmg.flashback.results.ui.feature.notificationonboarding

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.NotificationChannel
import tmg.testutils.BaseTest
import tmg.utilities.models.Selected

internal class NotificationOnboardingViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)

    private lateinit var underTest: NotificationOnboardingViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationRepository.seenNotificationOnboarding } returns false
        every { mockNotificationRepository.notificationUpcomingRace } returns true
        every { mockNotificationRepository.notificationUpcomingSprint } returns true
        every { mockNotificationRepository.notificationUpcomingQualifying } returns true
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns true
        every { mockNotificationRepository.notificationUpcomingOther } returns true
    }

    private fun initUnderTest() {
        underTest = NotificationOnboardingViewModel(mockNotificationRepository)
    }

    @Test
    fun `init marks that we now seen onboarding prompt`() {
        initUnderTest()
        verify {
            mockNotificationRepository.seenNotificationOnboarding = true
        }
    }

    @Test
    fun `init updates list with initial values`() = runTest {
        initUnderTest()
        underTest.notificationPreferences.test {
            assertEquals(NotificationChannel.values().map {
                Selected(
                    NotificationOnboardingModel(it.name, it, it.label, it.icon),
                    true
                )
            }, awaitItem())
        }
    }

    @Test
    fun `selecting notification channel race updates up next controller`() {
        initUnderTest()
        underTest.inputs.selectNotificationChannel(NotificationChannel.RACE)
        verify {
            mockNotificationRepository.notificationUpcomingRace = false
        }
    }

    @Test
    fun `selecting notification channel qualifying updates up next controller`() {
        initUnderTest()
        underTest.inputs.selectNotificationChannel(NotificationChannel.QUALIFYING)
        verify {
            mockNotificationRepository.notificationUpcomingQualifying = false
        }
    }

    @Test
    fun `selecting notification channel free practice updates up next controller`() {
        initUnderTest()
        underTest.inputs.selectNotificationChannel(NotificationChannel.FREE_PRACTICE)
        verify {
            mockNotificationRepository.notificationUpcomingFreePractice = false
        }
    }

    @Test
    fun `selecting notification channel other updates up next controller`() {
        initUnderTest()
        underTest.inputs.selectNotificationChannel(NotificationChannel.SEASON_INFO)
        verify {
            mockNotificationRepository.notificationUpcomingOther = false
        }
    }

    @Test
    fun `selecting notification channel updates list with new values`() = runTest {
        initUnderTest()
        underTest.notificationPreferences.test {
            assertEquals(NotificationChannel.values().map {
                Selected(
                    NotificationOnboardingModel(it.name, it, it.label, it.icon),
                    true
                )
            }, awaitItem())
        }

        // Assumes selecting channel works
        every { mockNotificationRepository.notificationUpcomingRace } returns false
        underTest.inputs.selectNotificationChannel(NotificationChannel.RACE)

        underTest.notificationPreferences.test {
            assertEquals(NotificationChannel.values().map {
                Selected(
                    NotificationOnboardingModel(it.name, it, it.label, it.icon),
                    it != NotificationChannel.RACE
                )
            }, awaitItem())
        }
    }
}