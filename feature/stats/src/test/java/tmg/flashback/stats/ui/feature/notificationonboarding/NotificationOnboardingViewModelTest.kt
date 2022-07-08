package tmg.flashback.stats.ui.feature.notificationonboarding

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationChannel
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class NotificationOnboardingViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)

    private lateinit var underTest: NotificationOnboardingViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationRepository.seenNotificationOnboarding } returns false
        every { mockNotificationRepository.notificationRace } returns true
        every { mockNotificationRepository.notificationQualifying } returns true
        every { mockNotificationRepository.notificationFreePractice } returns true
        every { mockNotificationRepository.notificationOther } returns true
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
    fun `init updates list with initial values`() {
        initUnderTest()
        underTest.notificationPreferences.test {
            assertValue(NotificationChannel.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    true
                )
            })
        }
    }

    @Test
    fun `selecting notification channel race updates up next controller`() {
        initUnderTest()
        underTest.inputs.selectNotificationChannel(NotificationChannel.RACE)
        verify {
            mockNotificationRepository.notificationRace = false
        }
    }

    @Test
    fun `selecting notification channel qualifying updates up next controller`() {
        initUnderTest()
        underTest.inputs.selectNotificationChannel(NotificationChannel.QUALIFYING)
        verify {
            mockNotificationRepository.notificationQualifying = false
        }
    }

    @Test
    fun `selecting notification channel free practice updates up next controller`() {
        initUnderTest()
        underTest.inputs.selectNotificationChannel(NotificationChannel.FREE_PRACTICE)
        verify {
            mockNotificationRepository.notificationFreePractice = false
        }
    }

    @Test
    fun `selecting notification channel other updates up next controller`() {
        initUnderTest()
        underTest.inputs.selectNotificationChannel(NotificationChannel.SEASON_INFO)
        verify {
            mockNotificationRepository.notificationOther = false
        }
    }

    @Test
    fun `selecting notification channel updates list with new values`() {
        initUnderTest()
        underTest.notificationPreferences.test {
            assertValue(NotificationChannel.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    true
                )
            })
        }

        // Assumes selecting channel works
        every { mockNotificationRepository.notificationRace } returns false
        underTest.inputs.selectNotificationChannel(NotificationChannel.RACE)

        underTest.notificationPreferences.test {
            assertValue(NotificationChannel.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    it != NotificationChannel.RACE
                )
            })
        }
    }
}