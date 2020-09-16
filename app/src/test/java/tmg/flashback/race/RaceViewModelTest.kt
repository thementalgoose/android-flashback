package tmg.flashback.race

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertValue
import tmg.flashback.utils.SeasonRound

@FlowPreview
@ExperimentalCoroutinesApi
class RaceViewModelTest: BaseTest() {

    lateinit var sut: RaceViewModel

    private val mockSeasonOverviewDB: SeasonOverviewDB = mock()
    private val mockPrefsDB: PrefsDB = mock()
    private val mockConnectivityManager: ConnectivityManager = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
    }

    private fun initSUT() {
        sut = RaceViewModel(mockSeasonOverviewDB, mockPrefsDB, mockConnectivityManager, testScopeProvider)
        sut.inputs.initialise(2019, 1, null)
    }

    @Test
    fun `RaceViewModel init no network error shown when network isnt available`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonRound(any(), any())).thenReturn(flow { emit(null) })

        initSUT()


    }

    @Test
    fun `RaceViewModel initialise emits season round data`() = coroutineTest {

        initSUT()

        sut.inputs.initialise(2020, 1, LocalDate.now())
        advanceUntilIdle()

        assertValue(SeasonRound(2020, 1), sut.outputs.seasonRoundData)
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockSeasonOverviewDB, mockPrefsDB, mockConnectivityManager)
    }
}