package tmg.flashback.ui2.dashboard

import io.mockk.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.common.repository.ForceUpgradeRepository
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.statistics.usecases.SearchAppShortcutUseCase
import tmg.flashback.ui.HomeViewModel
import tmg.testutils.BaseTest

internal class HomeViewModelTest: BaseTest() {

    private var mockRssController: RSSController = mockk(relaxed = true)
    private var mockConfigRepository: ConfigRepository = mockk(relaxed = true)
    private var mockApplyConfigUseCase: ApplyConfigUseCase = mockk(relaxed = true)
    private var mockCrashController: CrashController = mockk(relaxed = true)
    private var mockForceUpgradeRepository: ForceUpgradeRepository = mockk(relaxed = true)
    private var mockCacheRepository: CacheRepository = mockk(relaxed = true)
    private var mockScheduleController: ScheduleController = mockk(relaxed = true)
    private var mockAppShortcutUseCase: SearchAppShortcutUseCase = mockk(relaxed = true)

    private lateinit var sut: HomeViewModel

    @BeforeEach
    internal fun setUp() {
        coEvery { mockApplyConfigUseCase.apply() } returns true
        every { mockConfigRepository.requireSynchronisation } returns false
        every { mockForceUpgradeRepository.shouldForceUpgrade } returns false
        every { mockCacheRepository.initialSync } returns true
        every { mockRssController.enabled } returns false
    }

    private fun initSUT() {
        sut = HomeViewModel(
            mockConfigRepository,
            mockApplyConfigUseCase,
            mockRssController,
            mockCrashController,
            mockForceUpgradeRepository,
            mockCacheRepository,
            mockAppShortcutUseCase,
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
        every { mockForceUpgradeRepository.shouldForceUpgrade } returns true

        initSUT()
        sut.initialise()

        assertFalse(sut.requiresSync)
        assertTrue(sut.forceUpgrade)
        assertTrue(sut.appliedChanges)
    }

    @Test
    fun `initialise with no force upgrade or sync with rss disabled and search disabled notifies applied changes`() = coroutineTest {
        every { mockRssController.enabled } returns false

        initSUT()
        sut.initialise()

        coVerify { mockApplyConfigUseCase.apply() }
        verify { mockRssController.removeAppShortcut() }
        verify { mockAppShortcutUseCase.setup() }
        coVerify { mockScheduleController.scheduleNotifications() }

        assertFalse(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertFalse(sut.appliedChanges)
    }

    @Test
    fun `initialise with no force upgrade or sync with rss enabled and search enabled notifies applied changes`() = coroutineTest {
        every { mockRssController.enabled } returns true

        initSUT()
        sut.initialise()

        coVerify { mockApplyConfigUseCase.apply() }
        verify { mockRssController.addAppShortcut() }
        verify { mockAppShortcutUseCase.setup() }
        coVerify { mockScheduleController.scheduleNotifications() }

        assertFalse(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertFalse(sut.appliedChanges)
    }
}