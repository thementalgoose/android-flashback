package tmg.flashback.ui

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.common.controllers.ForceUpgradeController
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.statistics.usecases.SearchAppShortcutUseCase
import tmg.flashback.ui.sync.SyncNavTarget.DASHBOARD
import tmg.flashback.ui.sync.SyncNavTarget.FORCE_UPGRADE
import tmg.flashback.ui.sync.SyncState
import tmg.flashback.ui.sync.SyncViewModel
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertEventNotFired
import tmg.testutils.livedata.test

internal class SyncViewModelTest: BaseTest() {

    private var mockRssController: RSSController = mockk(relaxed = true)
    private var mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private var mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private var mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private var mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private var mockConfigRepository: ConfigRepository = mockk(relaxed = true)
    private var mockResetConfigUseCase: ResetConfigUseCase = mockk(relaxed = true)
    private var mockFetchConfigUseCase: FetchConfigUseCase = mockk(relaxed = true)
    private var mockCacheRepository: CacheRepository = mockk(relaxed = true)
    private var mockForceUpgradeController: ForceUpgradeController = mockk(relaxed = true)
    private var mockScheduleController: ScheduleController = mockk(relaxed = true)
    private var mockSearchAppShortcutUseCase: SearchAppShortcutUseCase = mockk(relaxed = true)

    private lateinit var sut: SyncViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockForceUpgradeController.shouldForceUpgrade } returns false
        every { mockRssController.enabled } returns false
        coEvery { mockFetchConfigUseCase.fetchAndApply() } returns true

        coEvery { mockCircuitRepository.fetchCircuits() } returns true
        coEvery { mockConstructorRepository.fetchConstructors() } returns true
        coEvery { mockDriverRepository.fetchDrivers() } returns true
        coEvery { mockOverviewRepository.fetchOverview() } returns true
    }

    private fun initSUT() {
        sut = SyncViewModel(
            mockRssController,
            mockCircuitRepository,
            mockConstructorRepository,
            mockDriverRepository,
            mockOverviewRepository,
            mockConfigRepository,
            mockResetConfigUseCase,
            mockFetchConfigUseCase,
            mockForceUpgradeController,
            mockCacheRepository,
            mockScheduleController,
            mockSearchAppShortcutUseCase,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @Test
    fun `start loading with config not synced sends go to home event`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns true

        initSUT()
        sut.inputs.startLoading()

        coVerify {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.loadingState.test {
            assertValue(SyncState.DONE)
        }
        sut.outputs.showRetry.test {
            assertValue(false)
        }
        sut.outputs.navigate.test {
            assertDataEventValue(DASHBOARD)
        }
        verify {
            mockCacheRepository.initialSync = true
            mockSearchAppShortcutUseCase.setup()
        }
    }

    @Test
    fun `start loading with config not synced sends go to force upgrade event`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns true
        every { mockForceUpgradeController.shouldForceUpgrade } returns true

        initSUT()
        sut.inputs.startLoading()

        coVerify {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.loadingState.test {
            assertValue(SyncState.DONE)
        }
        sut.outputs.showRetry.test {
            assertValue(false)
        }
        sut.outputs.navigate.test {
            assertDataEventValue(FORCE_UPGRADE)
        }
        verify {
            mockCacheRepository.initialSync = true
            mockSearchAppShortcutUseCase.setup()
        }
    }

    @Test
    fun `start loading with config previously synced sends go to home event`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns false

        initSUT()
        sut.inputs.startLoading()

        coVerify(exactly = 0) {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.loadingState.test {
            assertValue(SyncState.DONE)
        }
        sut.outputs.showRetry.test {
            assertValue(false)
        }
        sut.outputs.navigate.test {
            assertDataEventValue(DASHBOARD)
        }
        verify {
            mockCacheRepository.initialSync = true
            mockSearchAppShortcutUseCase.setup()
        }
    }









    @Test
    fun `start loading with config failed changes state to failed`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns true
        coEvery { mockFetchConfigUseCase.fetchAndApply() } returns false

        initSUT()
        sut.inputs.startLoading()

        coVerify {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.loadingState.test {
            assertValue(SyncState.FAILED)
        }
        sut.outputs.showRetry.test {
            assertValue(true)
        }
        sut.outputs.navigate.test {
            assertEventNotFired()
        }
        verify(exactly = 0) {
            mockCacheRepository.initialSync = true
            mockSearchAppShortcutUseCase.setup()
        }
    }

    @Test
    fun `start loading with circuits failing changes state to failed`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns false
        coEvery { mockCircuitRepository.fetchCircuits() } returns false

        initSUT()
        sut.inputs.startLoading()

        coVerify(exactly = 0) {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.loadingState.test {
            assertValue(SyncState.FAILED)
        }
        sut.outputs.showRetry.test {
            assertValue(true)
        }
        sut.outputs.navigate.test {
            assertEventNotFired()
        }
        verify(exactly = 0) {
            mockCacheRepository.initialSync = true
        }
    }

    @Test
    fun `start loading with races failing changes state to failed`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns false
        coEvery { mockOverviewRepository.fetchOverview() } returns false

        initSUT()
        sut.inputs.startLoading()

        coVerify(exactly = 0) {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.loadingState.test {
            assertValue(SyncState.FAILED)
        }
        sut.outputs.showRetry.test {
            assertValue(true)
        }
        sut.outputs.navigate.test {
            assertEventNotFired()
        }
        verify(exactly = 0) {
            mockCacheRepository.initialSync = true
        }
    }

    @Test
    fun `start loading with constructors failing changes state to failed`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns false
        coEvery { mockConstructorRepository.fetchConstructors() } returns false

        initSUT()
        sut.inputs.startLoading()

        coVerify(exactly = 0) {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.loadingState.test {
            assertValue(SyncState.FAILED)
        }
        sut.outputs.showRetry.test {
            assertValue(true)
        }
        sut.outputs.navigate.test {
            assertEventNotFired()
        }
        verify(exactly = 0) {
            mockCacheRepository.initialSync = true
        }
    }

    @Test
    fun `start loading with drivers failing changes state to failed`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns false
        coEvery { mockDriverRepository.fetchDrivers() } returns false

        initSUT()
        sut.inputs.startLoading()

        coVerify(exactly = 0) {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.loadingState.test {
            assertValue(SyncState.FAILED)
        }
        sut.outputs.showRetry.test {
            assertValue(true)
        }
        sut.outputs.navigate.test {
            assertEventNotFired()
        }
        verify(exactly = 0) {
            mockCacheRepository.initialSync = true
        }
    }
}