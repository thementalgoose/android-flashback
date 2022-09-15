package tmg.flashback.ui.settings.notifications

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.repository.PermissionRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class SettingsNotificationsUpcomingViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: SettingsNotificationsUpcomingViewModel

    private fun initUnderTest() {
        underTest = SettingsNotificationsUpcomingViewModel(
            notificationRepository = mockNotificationRepository,
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
        every { mockNotificationRepository.notificationFreePractice } returns true

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `free practice enabled is false when free practice pref are disabled`() {
        every { mockNotificationRepository.notificationFreePractice } returns false

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `qualifying enabled is true when qualifying pref are enabled`() {
        every { mockNotificationRepository.notificationFreePractice } returns true

        initUnderTest()
        underTest.outputs.freePracticeEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `qualifying enabled is false when qualifying pref are disabled`() {
        every { mockNotificationRepository.notificationQualifying } returns false

        initUnderTest()
        underTest.outputs.qualifyingEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `race enabled is true when race pref are enabled`() {
        every { mockNotificationRepository.notificationRace } returns true

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `race enabled is false when race pref are disabled`() {
        every { mockNotificationRepository.notificationRace } returns false

        initUnderTest()
        underTest.outputs.raceEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `other enabled is true when other pref are enabled`() {
        every { mockNotificationRepository.notificationOther } returns true

        initUnderTest()
        underTest.outputs.otherEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `other enabled is false when other pref are disabled`() {
        every { mockNotificationRepository.notificationOther } returns false

        initUnderTest()
        underTest.outputs.otherEnabled.test {
            assertValue(false)
        }
    }

    
}