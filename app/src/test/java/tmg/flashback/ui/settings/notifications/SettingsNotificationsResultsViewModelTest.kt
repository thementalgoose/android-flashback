package tmg.flashback.ui.settings.notifications

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.usecases.ResubscribeNotificationsUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.settings.Settings
import tmg.testutils.BaseTest

internal class SettingsNotificationsResultsViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)
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
    fun `permission enabled is true when runtime permissions are enabled`() = runTest {
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true

        initUnderTest()
        underTest.outputs.permissionEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `permission enabled is false when runtime permissions are disabled`() = runTest {
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

        initUnderTest()
        underTest.outputs.permissionEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `qualifying enabled is true when qualifying pref are enabled`() = runTest {
        every { mockNotificationRepository.notificationNotifyQualifying } returns true

        initUnderTest()
        underTest.outputs.qualifyingEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `qualifying enabled is false when qualifying pref are disabled`() = runTest {
        every { mockNotificationRepository.notificationNotifyQualifying } returns false

        initUnderTest()
        underTest.outputs.qualifyingEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `race enabled is true when race pref are enabled`() = runTest {
        every { mockNotificationRepository.notificationNotifyRace } returns true

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `race enabled is false when race pref are disabled`() = runTest {
        every { mockNotificationRepository.notificationNotifyRace } returns false

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `other enabled is true when other pref are enabled`() = runTest {
        every { mockNotificationRepository.notificationNotifySprint } returns true

        initUnderTest()
        underTest.outputs.sprintEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `other enabled is false when other pref are disabled`() = runTest {
        every { mockNotificationRepository.notificationNotifySprint } returns false

        initUnderTest()
        underTest.outputs.sprintEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `clicking enable permission calls permission flow`() = runTest {
        val deferred = CompletableDeferred<Boolean>(parent = null)
        every { mockPermissionManager.requestPermission(any()) } returns deferred
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

        initUnderTest()
        underTest.outputs.permissionEnabled.test { awaitItem() }

        underTest.inputs.prefClicked(Settings.Notifications.notificationPermissionEnable)
        verify {
            mockPermissionManager.requestPermission(any())
        }
        deferred.complete(true)
        underTest.outputs.permissionEnabled.test { awaitItem() }
    }

    @Test
    fun `clicking enable permission calls refresh if already enabled`() = runTest {
        val deferred = CompletableDeferred<Boolean>(parent = null)
        every { mockPermissionManager.requestPermission(any()) } returns deferred
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true

        initUnderTest()
        underTest.outputs.permissionEnabled.test { awaitItem() }

        underTest.inputs.prefClicked(Settings.Notifications.notificationPermissionEnable)
        verify(exactly = 0) {
            mockPermissionManager.requestPermission(any())
        }
        underTest.outputs.permissionEnabled.test { awaitItem() }
    }

    @Test
    fun `clicking sprint updates pref and updates value`() = runTest {
        every { mockNotificationRepository.notificationNotifySprint } returns false

        initUnderTest()
        underTest.outputs.sprintEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Notifications.notificationResultsSprint(true))

        verify {
            mockNotificationRepository.notificationNotifySprint = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        underTest.outputs.sprintEnabled.test { awaitItem() }
    }

    @Test
    fun `clicking qualifying updates pref and updates value`() = runTest {
        every { mockNotificationRepository.notificationNotifyQualifying } returns false

        initUnderTest()
        underTest.outputs.qualifyingEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Notifications.notificationResultsQualifying(true))

        verify {
            mockNotificationRepository.notificationNotifyQualifying = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        underTest.outputs.qualifyingEnabled.test { awaitItem() }
    }

    @Test
    fun `clicking race updates pref and updates value`() = runTest {
        every { mockNotificationRepository.notificationNotifyRace } returns false

        initUnderTest()
        underTest.outputs.raceEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Notifications.notificationResultsRace(true))

        verify {
            mockNotificationRepository.notificationNotifyRace = true
        }
        coVerify {
            mockResubscribeNotificationsUseCase.resubscribe()
        }
        underTest.outputs.raceEnabled.test { awaitItem() }
    }
}