package tmg.flashback.ui.settings.notifications

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.navigation.IntentNavigationComponent
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.settings.Settings
import tmg.testutils.BaseTest

internal class SettingsNotificationsUpcomingViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)
    private val mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockResultsNavigationComponent: ResultsNavigationComponent = mockk(relaxed = true)
    private val mockIntentNavigationComponent: IntentNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: SettingsNotificationsUpcomingViewModel

    private fun initUnderTest() {
        underTest = SettingsNotificationsUpcomingViewModel(
            notificationRepository = mockNotificationRepository,
            scheduleNotificationsUseCase = mockScheduleNotificationsUseCase,
            permissionRepository = mockPermissionRepository,
            permissionManager = mockPermissionManager,
            resultsNavigationComponent = mockResultsNavigationComponent,
            intentNavigationComponent = mockIntentNavigationComponent
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
    fun `enabled notification permission updates value in repository`() = runTest {
        every { mockNotificationRepository.isUpcomingEnabled(any()) } returns true

        val expected = NotificationUpcoming.values().map { it to true }
        initUnderTest()
        underTest.outputs.notifications.test {
            assertEquals(expected, awaitItem())
        }

        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcoming(
            NotificationUpcoming.RACE, false))
        verify {
            mockNotificationRepository.setUpcomingEnabled(NotificationUpcoming.RACE, false)
        }
        underTest.outputs.notifications.test {
            // Expected will be the same because mocked refresh call
            assertEquals(expected, awaitItem())
        }
    }


    @Test
    fun `clicking enable permission calls permission flow`() = runTest {
        val deferred = CompletableDeferred<Map<String, Boolean>>(parent = null)
        every { mockPermissionManager.requestPermission(any()) } returns deferred
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

        initUnderTest()
        underTest.outputs.permissionEnabled.test { awaitItem() }

        underTest.inputs.prefClicked(Settings.Notifications.notificationPermissionEnable)
        verify {
            mockPermissionManager.requestPermission(any())
        }
        deferred.complete(mapOf("" to true))
        underTest.outputs.permissionEnabled.test { awaitItem() }
    }

    @Test
    fun `clicking enable exact alarm calls permission flow`() = runTest {
        every { mockPermissionRepository.isExactAlarmEnabled } returns false

        initUnderTest()
        underTest.outputs.exactAlarmEnabled.test { awaitItem() }

        underTest.inputs.prefClicked(Settings.Notifications.notificationExactAlarmEnable)
        verify {
            mockIntentNavigationComponent.openIntent(any())
        }
    }

    @Test
    fun `clicking enable permission calls refresh if already enabled`() = runTest {
        val deferred = CompletableDeferred<Map<String, Boolean>>(parent = null)
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
    fun `clicking minutes before opens up next notice period`() = runTest {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcomingNotice())

        verify {
            mockResultsNavigationComponent.upNext()
        }
    }
}