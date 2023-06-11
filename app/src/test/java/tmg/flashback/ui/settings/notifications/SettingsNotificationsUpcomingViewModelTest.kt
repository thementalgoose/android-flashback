package tmg.flashback.ui.settings.notifications

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.settings.Settings
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsNotificationsUpcomingViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)
    private val mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockResultsNavigationComponent: ResultsNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: SettingsNotificationsUpcomingViewModel

    private fun initUnderTest() {
        underTest = SettingsNotificationsUpcomingViewModel(
            notificationRepository = mockNotificationRepository,
            scheduleNotificationsUseCase = mockScheduleNotificationsUseCase,
            permissionRepository = mockPermissionRepository,
            permissionManager = mockPermissionManager,
            resultsNavigationComponent = mockResultsNavigationComponent,
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
    fun `free practice enabled is true when free practice pref are enabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns true

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `free practice enabled is false when free practice pref are disabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns false

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `qualifying enabled is true when qualifying pref are enabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns true

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `qualifying enabled is false when qualifying pref are disabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingQualifying } returns false

        initUnderTest()
        underTest.outputs.qualifyingEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `race enabled is true when race pref are enabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingRace } returns true

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `race enabled is false when race pref are disabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingRace } returns false

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `sprint enabled is true when sprint pref are enabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingSprint } returns true

        initUnderTest()
        underTest.outputs.sprintEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `sprint enabled is false when sprint pref are disabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingSprint } returns false

        initUnderTest()
        underTest.outputs.sprintEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `other enabled is true when other pref are enabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingOther } returns true

        initUnderTest()
        underTest.outputs.otherEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `other enabled is false when other pref are disabled`() = runTest {
        every { mockNotificationRepository.notificationUpcomingOther } returns false

        initUnderTest()
        underTest.outputs.otherEnabled.test {
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
    fun `clicking free practice updates pref and updates value`() = runTest {
        every { mockNotificationRepository.notificationUpcomingFreePractice } returns false

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcomingFreePractice(true))

        verify {
            mockNotificationRepository.notificationUpcomingFreePractice = true
            mockScheduleNotificationsUseCase.schedule()
        }
        underTest.outputs.freePracticeEnabled.test { awaitItem() }
    }

    @Test
    fun `clicking qualifying updates pref and updates value`() = runTest {
        every { mockNotificationRepository.notificationUpcomingQualifying } returns false

        initUnderTest()
        underTest.outputs.qualifyingEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcomingQualifying(true))

        verify {
            mockNotificationRepository.notificationUpcomingQualifying = true
            mockScheduleNotificationsUseCase.schedule()
        }
        underTest.outputs.qualifyingEnabled.test { awaitItem() }
    }

    @Test
    fun `clicking race updates pref and updates value`() = runTest {
        every { mockNotificationRepository.notificationUpcomingRace } returns false

        initUnderTest()
        underTest.outputs.raceEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcomingRace(true))

        verify {
            mockNotificationRepository.notificationUpcomingRace = true
            mockScheduleNotificationsUseCase.schedule()
        }
        underTest.outputs.raceEnabled.test { awaitItem() }
    }

    @Test
    fun `clicking other updates pref and updates value`() = runTest {
        every { mockNotificationRepository.notificationUpcomingOther } returns false

        initUnderTest()
        underTest.outputs.otherEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Notifications.notificationUpcomingOther(true))

        verify {
            mockNotificationRepository.notificationUpcomingOther = true
            mockScheduleNotificationsUseCase.schedule()
        }
        underTest.outputs.otherEnabled.test { awaitItem() }
    }

    @Test
    fun `clicking minutes before opens up next notice period`() = runTest {
        initUnderTest()
        underTest.inputs.prefClicked(Settings.Notifications.notificationNoticePeriod())

        verify {
            mockResultsNavigationComponent.upNext()
        }
    }
}