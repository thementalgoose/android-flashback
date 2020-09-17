package tmg.flashback.home

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.di.device.BuildConfigProvider
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.DataDB
import tmg.flashback.repo.db.stats.HistoryDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.assertValue

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModelTest: BaseTest() {

    lateinit var sut: HomeViewModel

    private val mockSeasonOverviewDB: SeasonOverviewDB = mock()
    private val mockHistoryDB: HistoryDB = mock()
    private val mockDataDB: DataDB = mock()
    private val mockPrefsDB: PrefsDB = mock()
    private val mockConnectivityManager: ConnectivityManager = mock()
    private val mockBuildConfigProvider: BuildConfigProvider = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockPrefsDB.shouldShowReleaseNotes).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonOverview(any())).thenReturn(flow { emit(Pair(2019, emptyList())) })
        whenever(mockHistoryDB.allHistory()).thenReturn(flow { emit(emptyList()) })
        whenever(mockDataDB.appBanner()).thenReturn(flow { emit(null) })
        whenever(mockDataDB.appLockout()).thenReturn(flow { emit(null) })
    }

    private fun initSUT() {

        sut = HomeViewModel(mockSeasonOverviewDB, mockHistoryDB, mockDataDB, mockPrefsDB, mockConnectivityManager, mockBuildConfigProvider, testScopeProvider)
    }

    @Test
    fun `HomeViewModel open release notes fires if the prefs signal we should show release notes`() = coroutineTest {

        whenever(mockPrefsDB.shouldShowReleaseNotes).thenReturn(true)

        initSUT()

        assertEventFired(sut.outputs.openReleaseNotes)
    }

    @Test
    fun `HomeViewModel show loading is set to true initially`() = coroutineTest {

        initSUT()

        assertValue(true, sut.outputs.showLoading)
    }

    @AfterEach
    internal fun tearDown() {
        reset(mockSeasonOverviewDB, mockHistoryDB, mockDataDB, mockPrefsDB, mockConnectivityManager, mockBuildConfigProvider)
    }
}