package tmg.flashback.stats.ui.settings.notifications

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.stats.R
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.testutils.assertExpectedOrder
import tmg.flashback.stats.testutils.findPref
import tmg.flashback.stats.testutils.findSwitch
import tmg.flashback.stats.usecases.ResubscribeNotificationsUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.permissions.RationaleType
import tmg.flashback.ui.repository.PermissionRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class UpNextSettingsViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockResubscribeNotificationUseCase: ResubscribeNotificationsUseCase = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)

    private lateinit var sut: SettingsNotificationViewModel

    private fun initSUT() {
        sut = SettingsNotificationViewModel(
            notificationRepository = mockNotificationRepository,
            resubscribeNotificationsUseCase = mockResubscribeNotificationUseCase,
            permissionRepository = mockPermissionRepository,
            permissionManager = mockPermissionManager,
            statsNavigationComponent = mockStatsNavigationComponent
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true
        every { mockNotificationRepository.notificationRace } returns true
        every { mockNotificationRepository.notificationQualifying } returns true
        every { mockNotificationRepository.notificationFreePractice } returns true
        every { mockNotificationRepository.notificationOther } returns true
    }

    @Test
    fun `initial model list with runtime permissions disabled is expected`() {
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

        initSUT()

        val expected = listOf(
            Pair(R.string.settings_notifications_runtime_enabled, null),
            Pair(R.string.settings_notifications_runtime_title, R.string.settings_notifications_runtime_description),
            Pair(R.string.settings_up_next_category_title, null),
            Pair(R.string.settings_up_next_category_race_title, R.string.settings_up_next_category_race_descrition),
            Pair(R.string.settings_up_next_category_qualifying_title, R.string.settings_up_next_category_qualifying_descrition),
            Pair(R.string.settings_up_next_category_free_practice_title, R.string.settings_up_next_category_free_practice_descrition),
            Pair(R.string.settings_up_next_category_other_title, R.string.settings_up_next_category_other_descrition),
            Pair(R.string.settings_up_next_title, null),
            Pair(R.string.settings_up_next_time_before_title, R.string.settings_up_next_time_before_description),
            Pair(R.string.settings_up_next_results_available_title, null),
            Pair(R.string.settings_up_next_results_race_title, R.string.settings_up_next_results_race_descrition),
            Pair(R.string.settings_up_next_results_sprint_title, R.string.settings_up_next_results_sprint_descrition),
            Pair(R.string.settings_up_next_results_qualifying_title, R.string.settings_up_next_results_qualifying_descrition)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `initial model list is expected`() {
        initSUT()

        val expected = listOf(
            Pair(R.string.settings_up_next_category_title, null),
            Pair(R.string.settings_up_next_category_race_title, R.string.settings_up_next_category_race_descrition),
            Pair(R.string.settings_up_next_category_qualifying_title, R.string.settings_up_next_category_qualifying_descrition),
            Pair(R.string.settings_up_next_category_free_practice_title, R.string.settings_up_next_category_free_practice_descrition),
            Pair(R.string.settings_up_next_category_other_title, R.string.settings_up_next_category_other_descrition),
            Pair(R.string.settings_up_next_title, null),
            Pair(R.string.settings_up_next_time_before_title, R.string.settings_up_next_time_before_description),
            Pair(R.string.settings_up_next_results_available_title, null),
            Pair(R.string.settings_up_next_results_race_title, R.string.settings_up_next_results_race_descrition),
            Pair(R.string.settings_up_next_results_sprint_title, R.string.settings_up_next_results_sprint_descrition),
            Pair(R.string.settings_up_next_results_qualifying_title, R.string.settings_up_next_results_qualifying_descrition)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking enable notifications launches that are enabled dont launch notification flow`() {
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

        initSUT()

        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true
        sut.clickPreference(sut.models.findPref(R.string.settings_notifications_runtime_title))

        verify(exactly = 0) {
            mockPermissionManager.requestPermission(RationaleType.RuntimeNotifications)
        }
    }

    @Test
    fun `clicking enable notifications launches that are disabled launches notification flow`() {
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false
        val completableDeferred = CompletableDeferred<Boolean>()
        every { mockPermissionManager.requestPermission(RationaleType.RuntimeNotifications) } returns completableDeferred
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_notifications_runtime_title))

        verify {
            mockPermissionManager.requestPermission(RationaleType.RuntimeNotifications)
        }
    }

    @Test
    fun `clicking toggle for race updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_race_title), true)
        verify {
            mockNotificationRepository.notificationRace = true
        }
    }

    @Test
    fun `clicking toggle for qualifying updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_qualifying_title), true)
        verify {
            mockNotificationRepository.notificationQualifying = true
        }
    }

    @Test
    fun `clicking toggle for free practice updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_free_practice_title), true)
        verify {
            mockNotificationRepository.notificationFreePractice = true
        }
    }

    @Test
    fun `clicking toggle for other updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_other_title), true)
        verify {
            mockNotificationRepository.notificationOther = true
        }
    }

    @Test
    fun `clicking toggle for race notify updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_results_qualifying_title), true)
        verify {
            mockNotificationRepository.notificationNotifyQualifying = true
        }
        coVerify {
            mockResubscribeNotificationUseCase.resubscribe()
        }
    }

    @Test
    fun `clicking toggle for sprint notify updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_results_sprint_title), true)
        verify {
            mockNotificationRepository.notificationNotifySprint = true
        }
        coVerify {
            mockResubscribeNotificationUseCase.resubscribe()
        }
    }

    @Test
    fun `clicking toggle for qualifying notify updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_results_race_title), true)
        verify {
            mockNotificationRepository.notificationNotifyRace = true
        }
        coVerify {
            mockResubscribeNotificationUseCase.resubscribe()
        }
    }

    @Test
    fun `clicking toggle for reminders opens event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_up_next_time_before_title))
        verify {
            mockStatsNavigationComponent.upNext()
        }
    }
}