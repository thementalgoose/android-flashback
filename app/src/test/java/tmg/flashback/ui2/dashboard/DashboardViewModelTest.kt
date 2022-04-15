package tmg.flashback.ui2.dashboard

import android.content.Context
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.common.constants.ReleaseNotes
import tmg.flashback.common.usecases.NewReleaseNotesUseCase
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.workmanager.WorkerProvider
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.assertEventNotFired
import tmg.testutils.livedata.test

internal class DashboardViewModelTest: BaseTest() {

    lateinit var sut: DashboardViewModel

    private val mockContext: Context = mockk(relaxed = true)
    private val mockWorkerProvider: WorkerProvider = mockk(relaxed = true)
    private val mockApplyConfigUseCase: ApplyConfigUseCase = mockk(relaxed = true)
    private val mockFetchConfigUseCase: FetchConfigUseCase = mockk(relaxed = true)
    private val mockHomeController: HomeController = mockk(relaxed = true)
    private val mockNewReleaseNotesUseCase: NewReleaseNotesUseCase = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        coEvery { mockApplyConfigUseCase.apply() } returns false
        every { mockNewReleaseNotesUseCase.getNotes() } returns emptyList()
    }

    private fun initSUT() {
        sut = DashboardViewModel(
            mockContext,
            mockWorkerProvider,
            mockFetchConfigUseCase,
            mockApplyConfigUseCase,
            mockHomeController,
            mockNewReleaseNotesUseCase
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

    @Test
    fun `default to schedule returns true value from home controller`() {
        every { mockHomeController.dashboardDefaultToSchedule } returns true
        initSUT()
        assertTrue(sut.defaultToSchedule)
    }

    @Test
    fun `default to schedule returns false value from home controller`() {
        every { mockHomeController.dashboardDefaultToSchedule } returns false
        initSUT()
        assertFalse(sut.defaultToSchedule)
    }

    //region Remote config fetch and sync

    @Test
    fun `init if update returns changes and activate fails nothing happens`() = coroutineTest {

        coEvery { mockApplyConfigUseCase.apply() } returns false

        initSUT()
        advanceUntilIdle()

        sut.outputs.appConfigSynced.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `init if update returns changes and activate successfully then notify app config synced event`() = coroutineTest {

        coEvery { mockApplyConfigUseCase.apply() } returns true

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
        every { mockNewReleaseNotesUseCase.getNotes() } returns listOf(ReleaseNotes.VERSION_110)
        initSUT()
        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    @Test
    fun `init if release notes are not pending then open release notes not fired`() {
        every { mockNewReleaseNotesUseCase.getNotes() } returns emptyList()
        initSUT()
        sut.outputs.openReleaseNotes.test {
            assertEventNotFired()
        }
    }

    //endregion
}