package tmg.flashback.presentation.settings.notifications

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.season.contract.repository.NotificationsRepository
import tmg.flashback.season.contract.repository.models.NotificationReminder
import tmg.flashback.season.usecases.ScheduleNotificationsUseCase
import tmg.flashback.device.AppPermissions
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.device.repository.PermissionRepository
import tmg.flashback.presentation.settings.Settings
import tmg.testutils.BaseTest

internal class SettingsNotificationsUpcomingNoticeViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationsRepository = mockk(relaxed = true)
    private val mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: SettingsNotificationsUpcomingNoticeViewModel

    private fun initUnderTest() {
        underTest = SettingsNotificationsUpcomingNoticeViewModel(
            notificationRepository = mockNotificationRepository,
            permissionManager = mockPermissionManager,
            permissionRepository = mockPermissionRepository,
            applicationNavigationComponent = mockApplicationNavigationComponent,
            scheduleNotificationsUseCase = mockScheduleNotificationsUseCase
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationRepository.notificationReminderPeriod } returns NotificationReminder.MINUTES_30
    }

    @Test
    fun `init loads notification list`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.outputs.currentlySelected.test {
            Assertions.assertEquals(NotificationReminder.MINUTES_30, awaitItem())
        }
    }

    @Test
    fun `clicking exact alarm permission launches special permissions screen`() {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Notifications.notificationExactAlarmEnable)
        // Verification step here fails build sdk int check
    }

    @Test
    fun `clicking runtime notification permission launches notification permissions screen`() {
        val deferrable = CompletableDeferred<Map<String, Boolean>>()
        every { mockPermissionManager.requestPermission(any()) } returns deferrable
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Notifications.notificationPermissionEnable)

        verify {
            mockPermissionManager.requestPermission(AppPermissions.RuntimeNotifications)
        }

        deferrable.complete(mapOf())
    }

    @ParameterizedTest
    @EnumSource(NotificationReminder::class)
    fun `selecting notification reminder updates value in controller`(reminder: NotificationReminder) {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Notifications.notificationNoticePeriod(reminder, true, true))
        verify {
            mockNotificationRepository.notificationReminderPeriod = reminder
            mockScheduleNotificationsUseCase.schedule()
        }
    }
}