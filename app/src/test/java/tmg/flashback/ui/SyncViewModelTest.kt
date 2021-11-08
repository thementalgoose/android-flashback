package tmg.flashback.ui

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.common.controllers.ForceUpgradeController
import tmg.configuration.controllers.ConfigController
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.upnext.controllers.UpNextController
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SyncViewModelTest: BaseTest() {

    private var mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)
    private var mockRssController: RSSController = mockk(relaxed = true)
    private var mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private var mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private var mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private var mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private var mockConfigurationManager: ConfigController = mockk(relaxed = true)
    private var mockForceUpgradeController: ForceUpgradeController = mockk(relaxed = true)
    private var mockUpNextController: UpNextController = mockk(relaxed = true)

    private lateinit var sut: SyncViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockAppShortcutManager.enable() } returns true
        every { mockAppShortcutManager.disable() } returns true
        every { mockForceUpgradeController.shouldForceUpgrade } returns false
        every { mockRssController.enabled } returns false
        coEvery { mockConfigurationManager.fetchAndApply() } returns true

        coEvery { mockCircuitRepository.fetchCircuits() } returns true
        coEvery { mockConstructorRepository.fetchConstructors() } returns true
        coEvery { mockDriverRepository.fetchDrivers() } returns true
        coEvery { mockOverviewRepository.fetchOverview() } returns true
    }

    private fun initSUT() {
        sut = SyncViewModel(
            mockAppShortcutManager,
            mockRssController,
            mockCircuitRepository,
            mockConstructorRepository,
            mockDriverRepository,
            mockOverviewRepository,
            mockConfigurationManager,
            mockForceUpgradeController,
            mockUpNextController,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    //region Races

    @Test
    fun `sync races sets value to done when fetch succeeds`() = coroutineTest {
        coEvery { mockOverviewRepository.fetchOverview() } returns true

        initSUT()

        val observe = sut.outputs.racesState.testObserve()
        runBlockingTest { sut.startSyncRaces() }

        observe.assertValueAt(SyncState.LOADING, 0)
        observe.assertValueAt(SyncState.LOADING, 1)
        observe.assertValueAt(SyncState.DONE, 2)
        coVerify {
            mockOverviewRepository.fetchOverview()
        }
    }

    @Test
    fun `sync races sets value to failed when fetch fails`() = coroutineTest {
        coEvery { mockOverviewRepository.fetchOverview() } returns false

        initSUT()

        val observe = sut.outputs.racesState.testObserve()
        runBlockingTest { sut.startSyncRaces() }

        observe.assertValueAt(SyncState.LOADING, 0)
        observe.assertValueAt(SyncState.LOADING, 1)
        observe.assertValueAt(SyncState.FAILED, 2)
        coVerify {
            mockOverviewRepository.fetchOverview()
        }
    }

    //endregion
}