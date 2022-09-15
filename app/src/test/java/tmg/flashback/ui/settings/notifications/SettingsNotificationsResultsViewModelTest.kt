package tmg.flashback.ui.settings.notifications

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import org.junit.jupiter.api.Test
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.ResubscribeNotificationsUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.settings.Settings
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsNotificationsResultsViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockResubscribeNotificationsUseCase: ResubscribeNotificationsUseCase = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)

    private lateinit var underTest: SettingsNotificationsResultsViewModel

    private fun initUnderTest() {
        underTest = SettingsNotificationsResultsViewModel(
            notificationRepository = mockNotificationRepository,
            resubscribeNotificationsUseCase = mockResubscribeNotificationsUseCase,
            permissionRepository = mockPermissionRepository,
            permissionManager = mockPermissionManager
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
    fun `qualifying enabled is true when qualifying pref are enabled`() {
        every { mockNotificationRepository.notificationNotifyQualifying } returns true

        initUnderTest()
        underTest.outputs.qualifyingEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `qualifying enabled is false when qualifying pref are disabled`() {
        every { mockNotificationRepository.notificationNotifyQualifying } returns false

        initUnderTest()
        underTest.outputs.qualifyingEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `race enabled is true when race pref are enabled`() {
        every { mockNotificationRepository.notificationNotifyRace } returns true

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `race enabled is false when race pref are disabled`() {
        every { mockNotificationRepository.notificationNotifyRace } returns false

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `other enabled is true when other pref are enabled`() {
        every { mockNotificationRepository.notificationNotifySprint } returns true

        initUnderTest()
        underTest.outputs.sprintEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `other enabled is false when other pref are disabled`() {
        every { mockNotificationRepository.notificationNotifySprint } returns false

        initUnderTest()
        underTest.outputs.sprintEnabled.test {
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
    fun `clicking sprint updates pref and updates value`() {
        every { mockNotificationRepository.notificationNotifySprint } returns false

        initUnderTest()
        val observer = underTest.outputs.sprintEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Notifications.notificationResultsSprint(true))

        verify {
            mockNotificationRepository.notificationNotifySprint = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `clicking qualifying updates pref and updates value`() {
        every { mockNotificationRepository.notificationNotifyQualifying } returns false

        initUnderTest()
        val observer = underTest.outputs.qualifyingEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Notifications.notificationResultsQualifying(true))

        verify {
            mockNotificationRepository.notificationNotifyQualifying = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `clicking race updates pref and updates value`() {
        every { mockNotificationRepository.notificationNotifyRace } returns false

        initUnderTest()
        val observer = underTest.outputs.raceEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Notifications.notificationResultsRace(true))

        verify {
            mockNotificationRepository.notificationNotifyRace = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        observer.assertEmittedCount(2)
    }
}