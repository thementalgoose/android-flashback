package tmg.flashback.ui.dashboard

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.common.controllers.ForceUpgradeController
import tmg.flashback.configuration.controllers.ConfigController
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.controllers.SearchController
import tmg.testutils.BaseTest

internal class HomeViewModelTest: BaseTest() {

    private var mockRssController: RSSController = mockk(relaxed = true)
    private var mockConfigurationManager: ConfigController = mockk(relaxed = true)
    private var mockCrashController: CrashController = mockk(relaxed = true)
    private var mockForceUpgradeController: ForceUpgradeController = mockk(relaxed = true)
    private var mockCacheRepository: CacheRepository = mockk(relaxed = true)
    private var mockScheduleController: ScheduleController = mockk(relaxed = true)
    private var mockSearchController: SearchController = mockk(relaxed = true)

    private lateinit var sut: HomeViewModel

    @BeforeEach
    internal fun setUp() {
        coEvery { mockConfigurationManager.applyPending() } returns true
        every { mockConfigurationManager.requireSynchronisation } returns false
        every { mockForceUpgradeController.shouldForceUpgrade } returns false
        every { mockCacheRepository.initialSync } returns true
        every { mockRssController.enabled } returns false
        every { mockSearchController.enabled } returns false
    }

    private fun initSUT() {
        sut = HomeViewModel(
            mockConfigurationManager,
            mockRssController,
            mockCrashController,
            mockForceUpgradeController,
            mockCacheRepository,
            mockSearchController,
            mockScheduleController,
        )
    }

    @Test
    fun `initialise with config requiring sync notifies requires sync`() = coroutineTest {
        every { mockConfigurationManager.requireSynchronisation } returns true

        initSUT()
        sut.initialise()

        assertTrue(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertTrue(sut.appliedChanges)
    }

    @Test
    fun `initialise with config requiring cache repo requires sync`() = coroutineTest {
        every { mockCacheRepository.initialSync } returns false

        initSUT()
        sut.initialise()

        assertTrue(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertTrue(sut.appliedChanges)
    }

    @Test
    fun `initialise with force upgrade true notifies force upgrade`() = coroutineTest {
        every { mockForceUpgradeController.shouldForceUpgrade } returns true

        initSUT()
        sut.initialise()

        assertFalse(sut.requiresSync)
        assertTrue(sut.forceUpgrade)
        assertTrue(sut.appliedChanges)
    }

    @Test
    fun `initialise with no force upgrade or sync with rss disabled and search disabled notifies applied changes`() = coroutineTest {
        every { mockRssController.enabled } returns false
        every { mockSearchController.enabled } returns false

        initSUT()
        sut.initialise()

        coVerify { mockConfigurationManager.applyPending() }
        verify { mockRssController.removeAppShortcut() }
        verify { mockSearchController.removeAppShortcut() }
        coVerify { mockScheduleController.scheduleNotifications() }

        assertFalse(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertFalse(sut.appliedChanges)
    }

    @Test
    fun `initialise with no force upgrade or sync with rss enabled and search enabled notifies applied changes`() = coroutineTest {
        every { mockRssController.enabled } returns true
        every { mockSearchController.enabled } returns true

        initSUT()
        sut.initialise()

        coVerify { mockConfigurationManager.applyPending() }
        verify { mockRssController.addAppShortcut() }
        verify { mockSearchController.removeAppShortcut() }
        coVerify { mockScheduleController.scheduleNotifications() }

        assertFalse(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertFalse(sut.appliedChanges)
    }
}