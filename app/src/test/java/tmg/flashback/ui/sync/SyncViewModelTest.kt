package tmg.flashback.ui.sync

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.flashback.domain.repo.CircuitRepository
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.domain.repo.DriverRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.domain.repo.repository.CacheRepository
import tmg.flashback.maintenance.contract.usecases.ShouldForceUpgradeUseCase
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.ui.sync.SyncNavTarget.DASHBOARD
import tmg.flashback.ui.sync.SyncNavTarget.FORCE_UPGRADE
import tmg.flashback.usecases.SetupAppShortcutUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertEventNotFired
import tmg.testutils.livedata.test

internal class SyncViewModelTest: BaseTest() {

    private var mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private var mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private var mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private var mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private var mockConfigRepository: ConfigRepository = mockk(relaxed = true)
    private var mockResetConfigUseCase: ResetConfigUseCase = mockk(relaxed = true)
    private var mockFetchConfigUseCase: FetchConfigUseCase = mockk(relaxed = true)
    private var mockCacheRepository: CacheRepository = mockk(relaxed = true)
    private var mockShouldForceUpgradeUseCase: ShouldForceUpgradeUseCase = mockk(relaxed = true)
    private var mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)
    private var mockSetupAppShortcutUseCase: SetupAppShortcutUseCase = mockk(relaxed = true)

    private lateinit var sut: SyncViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockShouldForceUpgradeUseCase.shouldForceUpgrade() } returns false
        coEvery { mockFetchConfigUseCase.fetchAndApply() } returns true

        coEvery { mockCircuitRepository.fetchCircuits() } returns true
        coEvery { mockConstructorRepository.fetchConstructors() } returns true
        coEvery { mockDriverRepository.fetchDrivers() } returns true
        coEvery { mockOverviewRepository.fetchOverview() } returns true
    }

    private fun initSUT() {
        sut = SyncViewModel(
            mockCircuitRepository,
            mockConstructorRepository,
            mockDriverRepository,
            mockOverviewRepository,
            mockConfigRepository,
            mockResetConfigUseCase,
            mockFetchConfigUseCase,
            mockShouldForceUpgradeUseCase,
            mockCacheRepository,
            mockScheduleNotificationsUseCase,
            mockSetupAppShortcutUseCase,
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
        sut.outputs.circuitsState.test { assertValue(SyncState.DONE) }
        sut.outputs.constructorsState.test { assertValue(SyncState.DONE) }
        sut.outputs.driversState.test { assertValue(SyncState.DONE) }
        sut.outputs.racesState.test { assertValue(SyncState.DONE) }
        sut.outputs.configState.test { assertValue(SyncState.DONE) }
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
            mockSetupAppShortcutUseCase.setup()
            mockScheduleNotificationsUseCase.schedule()
        }
    }

    @Test
    fun `start loading with config not synced sends go to force upgrade event`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns true
        every { mockShouldForceUpgradeUseCase.shouldForceUpgrade() } returns true

        initSUT()
        sut.inputs.startLoading()

        coVerify {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.circuitsState.test { assertValue(SyncState.DONE) }
        sut.outputs.constructorsState.test { assertValue(SyncState.DONE) }
        sut.outputs.driversState.test { assertValue(SyncState.DONE) }
        sut.outputs.racesState.test { assertValue(SyncState.DONE) }
        sut.outputs.configState.test { assertValue(SyncState.DONE) }
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
            mockSetupAppShortcutUseCase.setup()
            mockScheduleNotificationsUseCase.schedule()
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
        sut.outputs.circuitsState.test { assertValue(SyncState.DONE) }
        sut.outputs.constructorsState.test { assertValue(SyncState.DONE) }
        sut.outputs.driversState.test { assertValue(SyncState.DONE) }
        sut.outputs.racesState.test { assertValue(SyncState.DONE) }
        sut.outputs.configState.test { assertValue(SyncState.DONE) }
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
        verify {
            mockSetupAppShortcutUseCase.setup()
        }
        sut.outputs.circuitsState.test { assertValue(SyncState.DONE) }
        sut.outputs.constructorsState.test { assertValue(SyncState.DONE) }
        sut.outputs.driversState.test { assertValue(SyncState.DONE) }
        sut.outputs.racesState.test { assertValue(SyncState.DONE) }
        sut.outputs.configState.test { assertValue(SyncState.FAILED) }
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
    fun `start loading with circuits failing changes state to failed`() = coroutineTest {
        every { mockConfigRepository.requireSynchronisation } returns false
        coEvery { mockCircuitRepository.fetchCircuits() } returns false

        initSUT()
        sut.inputs.startLoading()

        coVerify(exactly = 0) {
            mockResetConfigUseCase.ensureReset()
            mockFetchConfigUseCase.fetchAndApply()
        }
        sut.outputs.circuitsState.test { assertValue(SyncState.FAILED) }
        sut.outputs.constructorsState.test { assertValue(SyncState.DONE) }
        sut.outputs.driversState.test { assertValue(SyncState.DONE) }
        sut.outputs.racesState.test { assertValue(SyncState.DONE) }
        sut.outputs.configState.test { assertValue(SyncState.DONE) }
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
        sut.outputs.circuitsState.test { assertValue(SyncState.DONE) }
        sut.outputs.constructorsState.test { assertValue(SyncState.DONE) }
        sut.outputs.driversState.test { assertValue(SyncState.DONE) }
        sut.outputs.racesState.test { assertValue(SyncState.FAILED) }
        sut.outputs.configState.test { assertValue(SyncState.DONE) }
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
        sut.outputs.circuitsState.test { assertValue(SyncState.DONE) }
        sut.outputs.constructorsState.test { assertValue(SyncState.FAILED) }
        sut.outputs.driversState.test { assertValue(SyncState.DONE) }
        sut.outputs.racesState.test { assertValue(SyncState.DONE) }
        sut.outputs.configState.test { assertValue(SyncState.DONE) }
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
        sut.outputs.circuitsState.test { assertValue(SyncState.DONE) }
        sut.outputs.constructorsState.test { assertValue(SyncState.DONE) }
        sut.outputs.driversState.test { assertValue(SyncState.FAILED) }
        sut.outputs.racesState.test { assertValue(SyncState.DONE) }
        sut.outputs.configState.test { assertValue(SyncState.DONE) }
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