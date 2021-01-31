package tmg.flashback.ui.dashboard

import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import tmg.flashback.controllers.ReleaseNotesController
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.core.managers.BuildConfigManager
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.models.AppLockout
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.assertEventNotFired
import tmg.flashback.testutils.test

internal class DashboardViewModelTest: BaseTest() {

    lateinit var sut: DashboardViewModel

    private val mockContext: Context = mockk(relaxed = true)
    private val mockDataRepository: DataRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockReleaseNotesController: ReleaseNotesController = mockk(relaxed = true)
    private val mockConfigurationController: ConfigurationController = mockk(relaxed = true)

    private fun initSUT() {
        sut = DashboardViewModel(
            mockContext,
            mockDataRepository,
            mockBuildConfigManager,
            mockConfigurationController,
            mockReleaseNotesController
        )
    }

    //region Release Notes

    @Test
    fun `DashboardViewModel open release notes fires when release notes are different`() {

        every { mockReleaseNotesController.pendingReleaseNotes } returns true

        initSUT()

        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `DashboardViewModel open release notes doesnt fire when no release notes difference`() {

        every { mockReleaseNotesController.pendingReleaseNotes } returns false

        initSUT()

        sut.outputs.openReleaseNotes.test {
            assertEventNotFired()
        }
    }

    //endregion

    //region App Lockout

    @Test
    fun `DashboardViewModel app lockout event is fired if show is true and build config provider says version is should lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventFired()
        }
    }

    @Test
    fun `DashboardViewModel app lockout event is not fired if show is false and build config provider says version is should lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout.copy(show = false)) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `DashboardViewModel app lockout event is not fired if show is true and build config provider says version is should not lockout`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(expectedAppLockout.copy()) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns false

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `DashboardViewModel app lockout event is not fired if app lockout value is null`() = coroutineTest {

        every { mockDataRepository.appLockout() } returns flow { emit(null) }
        every { mockBuildConfigManager.shouldLockoutBasedOnVersion(any()) } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    //endregion

    //region Remote config fetch and sync

    @Test
    fun `DashboardViewModel init if update returns changes and activate fails nothing happens`() = coroutineTest {

        coEvery { mockConfigurationController.applyPending() } returns false

        initSUT()
        advanceUntilIdle()

        sut.outputs.appConfigSynced.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `DashboardViewModel init if update returns changes and activate successfully then notify app config synced event`() = coroutineTest {

        coEvery { mockConfigurationController.applyPending() } returns true

        initSUT()
        advanceUntilIdle()

        sut.outputs.appConfigSynced.test {
            assertEventFired()
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