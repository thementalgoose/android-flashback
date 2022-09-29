package tmg.flashback.ui

import io.mockk.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.forceupgrade.repository.ForceUpgradeRepository
import tmg.flashback.rss.usecases.RssShortcutUseCase
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.stats.usecases.ScheduleNotificationsUseCase
import tmg.flashback.stats.usecases.SearchAppShortcutUseCase
import tmg.testutils.BaseTest

internal class HomeViewModelTest: BaseTest() {

    private var mockRssShortcutUseCase: RssShortcutUseCase = mockk(relaxed = true)
    private var mockConfigRepository: ConfigRepository = mockk(relaxed = true)
    private var mockApplyConfigUseCase: ApplyConfigUseCase = mockk(relaxed = true)
    private var mockCrashController: CrashController = mockk(relaxed = true)
    private var mockForceUpgradeRepository: ForceUpgradeRepository = mockk(relaxed = true)
    private var mockCacheRepository: CacheRepository = mockk(relaxed = true)
    private var mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)
    private var mockAppShortcutUseCase: SearchAppShortcutUseCase = mockk(relaxed = true)

    private lateinit var sut: HomeViewModel

    @BeforeEach
    internal fun setUp() {
        coEvery { mockApplyConfigUseCase.apply() } returns true
        every { mockConfigRepository.requireSynchronisation } returns false
        every { mockForceUpgradeRepository.shouldForceUpgrade } returns false
        every { mockCacheRepository.initialSync } returns true
    }

    private fun initSUT() {
        sut = HomeViewModel(
            mockConfigRepository,
            mockApplyConfigUseCase,
            mockRssShortcutUseCase,
            mockCrashController,
            mockForceUpgradeRepository,
            mockCacheRepository,
            mockAppShortcutUseCase,
            mockScheduleNotificationsUseCase,
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
    fun `initialise with no force upgrade or sync and search disabled notifies applied changes`() = coroutineTest {

        initSUT()
        sut.initialise()

        coVerify { mockApplyConfigUseCase.apply() }
        verify { mockRssShortcutUseCase.setup() }
        verify { mockAppShortcutUseCase.setup() }
        coVerify { mockScheduleNotificationsUseCase.schedule() }

        assertFalse(sut.requiresSync)
        assertFalse(sut.forceUpgrade)
        assertFalse(sut.appliedChanges)
    }
}