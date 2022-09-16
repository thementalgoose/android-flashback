package tmg.flashback.ui.settings.notifications

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import org.junit.jupiter.api.Test
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.ResubscribeNotificationsUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.settings.Settings
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsNotificationsUpcomingViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockResubscribeNotificationsUseCase: ResubscribeNotificationsUseCase = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: SettingsNotificationsUpcomingViewModel

    private fun initUnderTest() {
        underTest = SettingsNotificationsUpcomingViewModel(
            notificationRepository = mockNotificationRepository,
            resubscribeNotificationsUseCase = mockResubscribeNotificationsUseCase,
            permissionRepository = mockPermissionRepository,
            permissionManager = mockPermissionManager,
            statsNavigationComponent = mockStatsNavigationComponent,
        )
    }

    @Test
    fun `permission enabled is true when runtime permissions are enabled`() {
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true

        initUnderTest()
        underTest.outputs.permissionEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `permission enabled is false when runtime permissions are disabled`() {
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

        initUnderTest()
        underTest.outputs.permissionEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `free practice enabled is true when free practice pref are enabled`() {
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns true

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `free practice enabled is false when free practice pref are disabled`() {
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns false

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `qualifying enabled is true when qualifying pref are enabled`() {
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns true

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `qualifying enabled is false when qualifying pref are disabled`() {
        every { mockNotificationRepository.notificationUpcomingQualifying } returns false

        initUnderTest()
        underTest.outputs.qualifyingEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `race enabled is true when race pref are enabled`() {
        every { mockNotificationRepository.notificationUpcomingRace } returns true

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `race enabled is false when race pref are disabled`() {
        every { mockNotificationRepository.notificationUpcomingRace } returns false

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `other enabled is true when other pref are enabled`() {
        every { mockNotificationRepository.notificationUpcomingOther } returns true

        initUnderTest()
        underTest.outputs.otherEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `other enabled is false when other pref are disabled`() {
        every { mockNotificationRepository.notificationUpcomingOther } returns false

        initUnderTest()
        underTest.outputs.otherEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `clicking enable permission calls permission flow`() {
        val deferred = CompletableDeferred<Boolean>(parent = null)
        every { mockPermissionManager.requestPermission(any()) } returns deferred
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

        initUnderTest()
        val observer = underTest.outputs.permissionEnabled.testObserve()
        observer.assertEmittedCount(1)

        underTest.inputs.prefClicked(Settings.Notifications.notificationPermissionEnable)
        verify {
            mockPermissionManager.requestPermission(any())
        }
        deferred.complete(true)
        observer.assertEmittedCount(2)
    }

    @Test
    fun `clicking enable permission calls refresh if already enabled`() {
        val deferred = CompletableDeferred<Boolean>(parent = null)
        every { mockPermissionManager.requestPermission(any()) } returns deferred
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true

        initUnderTest()
        val observer = underTest.outputs.permissionEnabled.testObserve()
        observer.assertEmittedCount(1)

        underTest.inputs.prefClicked(Settings.Notifications.notificationPermissionEnable)
        verify(exactly = 0) {
            mockPermissionManager.requestPermission(any())
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `clicking free practice updates pref and updates value`() {
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns false

        initUnderTest()
        val observer = underTest.outputs.freePracticeEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcomingFreePractice(true))

        verify {
            mockNotificationRepository.notificationUpcomingFreePractice = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `clicking qualifying updates pref and updates value`() {
        every { mockNotificationRepository.notificationUpcomingQualifying } returns false

        initUnderTest()
        val observer = underTest.outputs.qualifyingEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcomingQualifying(true))

        verify {
            mockNotificationRepository.notificationUpcomingQualifying = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `clicking race updates pref and updates value`() {
        every { mockNotificationRepository.notificationUpcomingRace } returns false

        initUnderTest()
        val observer = underTest.outputs.raceEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcomingRace(true))

        verify {
            mockNotificationRepository.notificationUpcomingRace = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `clicking other updates pref and updates value`() {
        every { mockNotificationRepository.notificationUpcomingOther } returns false

        initUnderTest()
        val observer = underTest.outputs.otherEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcomingOther(true))

        verify {
            mockNotificationRepository.notificationUpcomingOther = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `clicking minutes before opens up next notice period`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Notifications.notificationNoticePeriod())

        verify {
            mockStatsNavigationComponent.upNext()
        }
    }
}