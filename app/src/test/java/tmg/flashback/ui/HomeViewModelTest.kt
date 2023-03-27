package tmg.flashback.ui

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.maintenance.repository.MaintenanceRepository
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.rss.contract.usecases.RSSAppShortcutUseCase
import tmg.flashback.search.contract.usecases.SearchAppShortcutUseCase
import tmg.testutils.BaseTest

internal class HomeViewModelTest: BaseTest() {

    private var mockRssShortcutUseCase: RSSAppShortcutUseCase = mockk(relaxed = true)
    private var mockConfigRepository: ConfigRepository = mockk(relaxed = true)
    private var mockApplyConfigUseCase: ApplyConfigUseCase = mockk(relaxed = true)
    private var mockCrashController: CrashManager = mockk(relaxed = true)
    private var mockMaintenanceRepository: MaintenanceRepository = mockk(relaxed = true)
    private var mockCacheRepository: CacheRepository = mockk(relaxed = true)
    private var mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)
    private var mockAppShortcutUseCase: SearchAppShortcutUseCase = mockk(relaxed = true)

    private lateinit var sut: HomeViewModel

    @BeforeEach
    internal fun setUp() {
        coEvery { mockApplyConfigUseCase.apply() } returns true
        every { mockConfigRepository.requireSynchronisation } returns false
        every { mockMaintenanceRepository.shouldForceUpgrade } returns false
        every { mockCacheRepository.initialSync } returns true
    }

    private fun initSUT() {
        sut = HomeViewModel(
            mockConfigRepository,
            mockApplyConfigUseCase,
            mockRssShortcutUseCase,
            mockCrashController,
            mockMaintenanceRepository,
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
        every { mockMaintenanceRepository.shouldForceUpgrade } returns true

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