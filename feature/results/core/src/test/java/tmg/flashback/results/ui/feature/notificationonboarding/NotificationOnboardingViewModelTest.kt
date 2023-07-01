package tmg.flashback.results.ui.feature.notificationonboarding

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.results.contract.repository.models.NotificationUpcoming.RACE
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.icon
import tmg.testutils.BaseTest
import tmg.utilities.models.Selected

internal class NotificationOnboardingViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)

    private lateinit var underTest: NotificationOnboardingViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationRepository.seenNotificationOnboarding } returns false
        every { mockNotificationRepository.isUpcomingEnabled(any()) } returns true
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
            assertEquals(NotificationUpcoming.values().map {
                Selected(
                    NotificationOnboardingModel(it.name, it, it.channelLabel, it.icon),
                    true
                )
            }, awaitItem())
        }
    }

    @Test
    fun `selecting notification channel race updates up next controller`() {
        initUnderTest()
        underTest.inputs.selectNotificationChannel(RACE)
        verify {
            mockNotificationRepository.setUpcomingEnabled(RACE, false)
        }
    }

    @Test
    fun `selecting notification channel updates list with new values`() = runTest {
        initUnderTest()
        underTest.notificationPreferences.test {
            assertEquals(NotificationUpcoming.values().map {
                Selected(
                    NotificationOnboardingModel(it.name, it, it.channelLabel, it.icon),
                    true
                )
            }, awaitItem())
        }

        // Assumes selecting channel works
        every { mockNotificationRepository.isUpcomingEnabled(RACE) } returns false
        underTest.inputs.selectNotificationChannel(RACE)

        underTest.notificationPreferences.test {
            assertEquals(NotificationUpcoming.values().map {
                Selected(
                    NotificationOnboardingModel(it.name, it, it.channelLabel, it.icon),
                    it != RACE
                )
            }, awaitItem())
        }
    }
}