package tmg.flashback.upnext.ui.onboarding

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.model.NotificationChannel
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class OnboardingNotificationViewModelTest: BaseTest() {

    private val mockUpNextController: UpNextController = mockk(relaxed = true)

    private lateinit var sut: OnboardingNotificationViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockUpNextController.seenOnboarding() } returns Unit
        every { mockUpNextController.notificationRace } returns true
        every { mockUpNextController.notificationQualifying } returns true
        every { mockUpNextController.notificationFreePractice } returns true
        every { mockUpNextController.notificationSeasonInfo } returns true
    }

    private fun initSUT() {
        sut = OnboardingNotificationViewModel(mockUpNextController)
    }

    @Test
    fun `init marks that we now seen onboarding prompt`() {
        initSUT()
        verify {
            mockUpNextController.seenOnboarding()
        }
    }

    @Test
    fun `init updates list with initial values`() {
        initSUT()
        sut.notificationPreferences.test {
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
        initSUT()
        sut.inputs.selectNotificationChannel(NotificationChannel.RACE)
        verify {
            mockUpNextController.notificationRace = false
        }
    }

    @Test
    fun `selecting notification channel qualifying updates up next controller`() {
        initSUT()
        sut.inputs.selectNotificationChannel(NotificationChannel.QUALIFYING)
        verify {
            mockUpNextController.notificationQualifying = false
        }
    }

    @Test
    fun `selecting notification channel free practice updates up next controller`() {
        initSUT()
        sut.inputs.selectNotificationChannel(NotificationChannel.FREE_PRACTICE)
        verify {
            mockUpNextController.notificationFreePractice = false
        }
    }

    @Test
    fun `selecting notification channel other updates up next controller`() {
        initSUT()
        sut.inputs.selectNotificationChannel(NotificationChannel.SEASON_INFO)
        verify {
            mockUpNextController.notificationSeasonInfo = false
        }
    }

    @Test
    fun `selecting notification channel updates list with new values`() {
        initSUT()
        sut.notificationPreferences.test {
            assertValue(NotificationChannel.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    true
                )
            })
        }

        // Assumes selecting channel works
        every { mockUpNextController.notificationRace } returns false
        sut.inputs.selectNotificationChannel(NotificationChannel.RACE)

        sut.notificationPreferences.test {
            assertValue(NotificationChannel.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    it != NotificationChannel.RACE
                )
            })
        }
    }
}
