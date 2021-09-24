package tmg.flashback.upnext.ui.onboarding

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.core.ui.bottomsheet.BottomSheetItem
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.model.NotificationChannel
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class OnboardingNotificationViewModelTest {

    private val mockUpNextController: UpNextController = mockk(relaxed = true)

    private lateinit var sut: OnboardingNotificationViewModel

    private fun initSUT() {
        sut = OnboardingNotificationViewModel(mockUpNextController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockUpNextController.notificationRace } returns true
        every { mockUpNextController.notificationQualifying } returns true
        every { mockUpNextController.notificationFreePractice } returns true
        every { mockUpNextController.notificationSeasonInfo } returns true
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
        sut.inputs.selectNotificationChannel(NotificationChannel.RACE)

        // Assumes selecting channel works
        every { mockUpNextController.notificationRace } returns false
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
