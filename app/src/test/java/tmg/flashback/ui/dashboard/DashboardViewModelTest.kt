package tmg.flashback.ui.dashboard

import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.common.controllers.ReleaseNotesController
import tmg.configuration.controllers.ConfigController
import tmg.core.device.managers.BuildConfigManager
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.models.AppLockout
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.assertEventNotFired
import tmg.testutils.livedata.test

internal class DashboardViewModelTest: BaseTest() {

    lateinit var sut: DashboardViewModel

    private val mockContext: Context = mockk(relaxed = true)
    private val mockDataRepository: DataRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockConfigurationController: ConfigController = mockk(relaxed = true)
    private val mockReleaseNotesCOntroller: ReleaseNotesController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        coEvery { mockConfigurationController.applyPending() } returns false
        every { mockReleaseNotesCOntroller.pendingReleaseNotes } returns false
    }

    private fun initSUT() {
        sut = DashboardViewModel(
            mockContext,
            mockDataRepository,
            mockBuildConfigManager,
            mockConfigurationController,
            mockReleaseNotesCOntroller
        )
    }

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
    }

    //endregion

    //region Release notes

    @Test
    fun `init if release notes are pending then open release notes is fired`() {
        every { mockReleaseNotesCOntroller.pendingReleaseNotes } returns true
        initSUT()
        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `init if release notes are not pending then open release notes not fired`() {
        every { mockReleaseNotesCOntroller.pendingReleaseNotes } returns false
        initSUT()
        sut.outputs.openReleaseNotes.test {
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