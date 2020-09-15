package tmg.flashback.race

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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

    }

    private fun initSUT() {
        sut = RaceViewModel(mockSeasonOverviewDB, mockPrefsDB, mockConnectivityManager, testScopeProvider)
    }

    @Test
    fun `RaceViewModel`() {
        TODO("Do more race view model unit tests")
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