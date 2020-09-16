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

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when round data is null and date supplied is more than showComingSoonMessageForNextDays in the future, show race in future unavailable message`() {

        val showComingSoonMessageForNextDays = 10

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when round data is null and date supplied is in the past, show coming soon race data unavailable message`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when round data is null and date supplied is null, show missing race data unavailable message`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when round data is null and date supplied is between now and the next 10 days, show the missing race unavailable message`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when view type is (happy) constructor standings show constructor standings items`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when view type is race (error) and round date is in the future, show race in future unavailable message`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when view type is race (error) and round date is in the past, show race data coming soon unavailable message`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when view type is race (happy) and roundData race is not empty, show podium + race results in list`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when view type is qualifying (happy) Q3, items are ordered properly`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when view type is qualifying (happy) Q2, items are ordered properly`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when view type is qualifying (happy) Q1, items are ordered properly`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when order by is changed list content updates`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when show qualifying delta is enabled, qualifying delta is supplied`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when show grid penalties is enabled, grid penalties is supplies to models`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when only q1 data is supplied, ordering for multiple qualifying types always does the same order`() {

        TODO("Unit test needs to implemented")
    }

    @Test
    fun `RaceViewModel when only q1 and q2 data is supplied, changing the order adheres to q1, q2 and then true qualifying order`() {

        TODO("Unit test needs to implemented")
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