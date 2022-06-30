package tmg.flashback.statistics.ui.dashboard.onboarding

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class OnboardingNotificationViewModelTest: BaseTest() {

    private val mockScheduleController: ScheduleController = mockk(relaxed = true)

    private lateinit var sut: OnboardingNotificationViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockScheduleController.seenOnboarding() } returns Unit
        every { mockScheduleController.notificationRace } returns true
        every { mockScheduleController.notificationQualifying } returns true
        every { mockScheduleController.notificationFreePractice } returns true
        every { mockScheduleController.notificationSeasonInfo } returns true
    }

    private fun initSUT() {
        sut = OnboardingNotificationViewModel(mockScheduleController)
    }

    @Test
    fun `init marks that we now seen onboarding prompt`() {
        initSUT()
        verify {
            mockScheduleController.seenOnboarding()
        }
    }

    @Test
    fun `init updates list with initial values`() {
        initSUT()
        sut.notificationPreferences.test {
            assertValue(tmg.flashback.statistics.repository.models.NotificationChannel.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    true
                )
            })
        }
    }

    @Test
    fun `selecting notification channel race updates up next controller`() {
        initSUT()
        sut.inputs.selectNotificationChannel(tmg.flashback.statistics.repository.models.NotificationChannel.RACE)
        verify {
            mockScheduleController.notificationRace = false
        }
    }

    @Test
    fun `selecting notification channel qualifying updates up next controller`() {
        initSUT()
        sut.inputs.selectNotificationChannel(tmg.flashback.statistics.repository.models.NotificationChannel.QUALIFYING)
        verify {
            mockScheduleController.notificationQualifying = false
        }
    }

    @Test
    fun `selecting notification channel free practice updates up next controller`() {
        initSUT()
        sut.inputs.selectNotificationChannel(tmg.flashback.statistics.repository.models.NotificationChannel.FREE_PRACTICE)
        verify {
            mockScheduleController.notificationFreePractice = false
        }
    }

    @Test
    fun `selecting notification channel other updates up next controller`() {
        initSUT()
        sut.inputs.selectNotificationChannel(tmg.flashback.statistics.repository.models.NotificationChannel.SEASON_INFO)
        verify {
            mockScheduleController.notificationSeasonInfo = false
        }
    }

    @Test
    fun `selecting notification channel updates list with new values`() {
        initSUT()
        sut.notificationPreferences.test {
            assertValue(tmg.flashback.statistics.repository.models.NotificationChannel.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    true
                )
            })
        }

        // Assumes selecting channel works
        every { mockScheduleController.notificationRace } returns false
        sut.inputs.selectNotificationChannel(tmg.flashback.statistics.repository.models.NotificationChannel.RACE)

        sut.notificationPreferences.test {
            assertValue(tmg.flashback.statistics.repository.models.NotificationChannel.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    it != tmg.flashback.statistics.repository.models.NotificationChannel.RACE
                )
            })
        }
    }
}
