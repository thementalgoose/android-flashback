package tmg.flashback.ui.dashboard

import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.common.controllers.ReleaseNotesController
import tmg.configuration.controllers.ConfigController
import tmg.core.device.managers.BuildConfigManager
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.models.AppLockout
import tmg.flashback.upnext.controllers.UpNextController
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.assertEventNotFired
import tmg.testutils.livedata.test

internal class DashboardViewModelTest: BaseTest() {

    lateinit var sut: DashboardViewModel

    private val mockContext: Context = mockk(relaxed = true)
    private val mockDataRepository: DataRepository = mockk(relaxed = true)
    private val mockUpNextController: UpNextController = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockConfigurationController: ConfigController = mockk(relaxed = true)
    private val mockReleaseNotesController: ReleaseNotesController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        coEvery { mockConfigurationController.applyPending() } returns false
        every { mockReleaseNotesController.pendingReleaseNotes } returns false
        every { mockUpNextController.getNextEvent() } returns mockk()
        every { mockUpNextController.shouldShowNotificationOnboarding } returns true
    }

    private fun initSUT() {
        sut = DashboardViewModel(
            mockContext,
            mockDataRepository,
            mockUpNextController,
            mockBuildConfigManager,
            mockConfigurationController,
            mockReleaseNotesController
        )
    }

    //region Showing up next

    @Test
    fun `showing up next returns true if next event is not null`() {

        every { mockUpNextController.getNextEvent() } returns mockk()

        initSUT()

        sut.outputs.showUpNext.test {
            assertValue(true)
        }
    }

    @Test
    fun `showing up next returns false if next event is null`() {

        every { mockUpNextController.getNextEvent() } returns null

        initSUT()

        sut.outputs.showUpNext.test {
            assertValue(false)
        }
    }

    //endregion

    //region App Lockout

    @Test
    fun `app lockout event is fired if show is true and build config provider says version is should lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout) }
        every { mockBuildConfigManager.versionCode } returns expectedAppLockout.version!! - 1

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventFired()
        }
    }

    @Test
    fun `app lockout event is not fired if show is false and build config provider says version is should lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout.copy(show = false)) }
        every { mockBuildConfigManager.versionCode } returns expectedAppLockout.version!! - 1

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `app lockout event is not fired if show is true and build config provider says version is should not lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout.copy()) }
        every { mockBuildConfigManager.versionCode } returns expectedAppLockout.version!! + 1

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `app lockout event is not fired if app lockout value is null`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(null) }
        every { mockBuildConfigManager.versionCode } returns expectedAppLockout.version!! - 1

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    //endregion

    //region Remote config fetch and sync

    @Test
    fun `init if update returns changes and activate fails nothing happens`() = coroutineTest {

        coEvery { mockConfigurationController.applyPending() } returns false

        initSUT()
        advanceUntilIdle()

        sut.outputs.appConfigSynced.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `init if update returns changes and activate successfully then notify app config synced event`() = coroutineTest {

        coEvery { mockConfigurationController.applyPending() } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.appConfigSynced.test {
            assertEventFired()
        }
        verify {
            mockUpNextController.scheduleNotifications()
        }
    }

    //endregion

    //region Release notes

    @Test
    fun `init if release notes are pending then open release notes is fired`() {
        // Because notification onboarding takes priority over release notes
        every { mockUpNextController.shouldShowNotificationOnboarding } returns false
        every { mockReleaseNotesController.pendingReleaseNotes } returns true
        initSUT()
        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `init if release notes are not pending then open release notes not fired`() {
        every { mockUpNextController.shouldShowNotificationOnboarding } returns false
        every { mockReleaseNotesController.pendingReleaseNotes } returns false
        initSUT()
        sut.outputs.openReleaseNotes.test {
            assertEventNotFired()
        }
    }

    //endregion

    //region Release notes

    @Test
    fun `init if up next onboarding are pending then open up next onboarding is fired`() {
        every { mockUpNextController.shouldShowNotificationOnboarding } returns true
        initSUT()
        sut.outputs.openUpNextNotificationOnboarding.test {
            assertEventFired()
        }
    }

    @Test
    fun `init if up next onboarding are not pending then open up next onboarding not fired`() {
        every { mockUpNextController.shouldShowNotificationOnboarding } returns false
        initSUT()
        sut.outputs.openUpNextNotificationOnboarding.test {
            assertEventNotFired()
        }
    }

    //endregion

    //region Mock Data - App lockout

    private val expectedAppLockout = AppLockout(
        show = true,
        title = "msg",
        message = "Another msg",
        linkText = null,
        link = null,
        version = 10
    )

    //endregion
}