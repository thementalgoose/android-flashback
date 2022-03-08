package tmg.flashback.ui.dashboard

import io.mockk.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.common.controllers.ForceUpgradeController
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.controllers.SearchController
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.testutils.BaseTest

internal class HomeViewModelTest: BaseTest() {

    private var mockRssController: RSSController = mockk(relaxed = true)
    private var mockConfigRepository: ConfigRepository = mockk(relaxed = true)
    private var mockApplyConfigUseCase: ApplyConfigUseCase = mockk(relaxed = true)
    private var mockCrashController: CrashController = mockk(relaxed = true)
    private var mockForceUpgradeController: ForceUpgradeController = mockk(relaxed = true)
    private var mockCacheRepository: CacheRepository = mockk(relaxed = true)
    private var mockScheduleController: ScheduleController = mockk(relaxed = true)
    private var mockSearchController: SearchController = mockk(relaxed = true)

    private lateinit var sut: HomeViewModel

    @BeforeEach
    internal fun setUp() {
        coEvery { mockApplyConfigUseCase.apply() } returns true
        every { mockConfigRepository.requireSynchronisation } returns false
        every { mockForceUpgradeController.shouldForceUpgrade } returns false
        every { mockCacheRepository.initialSync } returns true
        every { mockRssController.enabled } returns false
        every { mockSearchController.enabled } returns false
    }

    private fun initSUT() {
        sut = HomeViewModel(
            mockConfigRepository,
            mockApplyConfigUseCase,
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
        every { mockConfigRepository.requireSynchronisation } returns true

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

        coVerify { mockApplyConfigUseCase.apply() }
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

        coVerify { mockApplyConfigUseCase.apply() }
        verify { mockRssController.addAppShortcut() }
        verify { mockSearchController.addAppShortcut() }
        coVerify { mockScheduleController.scheduleNotifications() }

        assertFalse(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertFalse(sut.appliedChanges)
    }
}