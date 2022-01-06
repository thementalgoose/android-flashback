package tmg.flashback.ui.dashboard

import android.content.Context
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.common.controllers.ReleaseNotesController
import tmg.flashback.configuration.controllers.ConfigController
import tmg.flashback.statistics.workmanager.WorkerProvider
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.assertEventNotFired
import tmg.testutils.livedata.test

internal class DashboardViewModelTest: BaseTest() {

    lateinit var sut: DashboardViewModel

    private val mockContext: Context = mockk(relaxed = true)
    private val mockWorkerProvider: WorkerProvider = mockk(relaxed = true)
    private val mockConfigurationController: ConfigController = mockk(relaxed = true)
    private val mockReleaseNotesController: ReleaseNotesController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        coEvery { mockConfigurationController.applyPending() } returns false
        every { mockReleaseNotesController.pendingReleaseNotes } returns false
    }

    private fun initSUT() {
        sut = DashboardViewModel(
            mockContext,
            mockWorkerProvider,
            mockConfigurationController,
            mockReleaseNotesController
        )
    }

    @Test
    fun `clicking search opens search screen`() {
        initSUT()

        sut.inputs.clickSearch()
        sut.outputs.openSearch.test {
            assertEventFired()
        }
    }

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
        coVerify {
            mockWorkerProvider.schedule()
        }
    }

    //endregion

    //region Release notes

    @Test
    fun `init if release notes are pending then open release notes is fired`() {
        // Because notification onboarding takes priority over release notes
        every { mockReleaseNotesController.pendingReleaseNotes } returns true
        initSUT()
        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `init if release notes are not pending then open release notes not fired`() {
        every { mockReleaseNotesController.pendingReleaseNotes } returns false
        initSUT()
        sut.outputs.openReleaseNotes.test {
            assertEventNotFired()
        }
    }

    //endregion
}