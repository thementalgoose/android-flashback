package tmg.flashback.stats.ui.drivers.season

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.stats.R
import tmg.flashback.ui.repository.ThemeRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class DriverSeasonViewModelTest: BaseTest() {

    private val mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)

    private lateinit var sut: DriverSeasonViewModel

    private fun initSUT() {
        sut = DriverSeasonViewModel(
            mockDriverRepository,
            mockNetworkConnectivityManager,
            mockThemeRepository,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(
            DriverHistory.model()) }
        every { mockNetworkConnectivityManager.isConnected } returns true
        coEvery { mockDriverRepository.getDriverSeasonCount(any()) } returns 1
        coEvery { mockDriverRepository.fetchDriver(any()) } returns true
    }

    //region List

    @Test
    fun `driver data with empty results and no network shows pull to refresh`() = coroutineTest {
        val input = DriverHistory.model(standings = emptyList())
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("driverId", 2020)

        sut.outputs.list.test {
            assertValue(listOf(
                DriverSeasonModel.headerModel(),
                DriverSeasonModel.NetworkError
            ))
        }
    }

    @Test
    fun `driver data with null item and no network shows pull to refresh`() = coroutineTest {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("driverId", 2020)

        sut.outputs.list.test {
            assertValue(listOf(
                DriverSeasonModel.NetworkError
            ))
        }
    }

    @Test
    fun `driver data with empty results and network shows data unavailable`() = coroutineTest {
        val input = DriverHistory.model(standings = emptyList())
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.setup("driverId", 2020)

        sut.outputs.list.test {
            assertValue(mutableListOf(
                DriverSeasonModel.headerModel(),
                DriverSeasonModel.InternalError
            ))
        }
    }

    @Test
    fun `driver data with results and network shows list of results in descending order`() = coroutineTest {
        val input = DriverHistory.model(standings = listOf(
            DriverHistorySeason.model(season = 2020)
        ))
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }

        initSUT()
        sut.inputs.setup("driverId", 2020)

        sut.outputs.list.test {
            assertListMatchesItem { it is DriverSeasonModel.Result && it.round == 1 }

            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_standings }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_podium }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_starts }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_finishes }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_retirements }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_best_finish }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_finishes_in_points }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_points }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_pole }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_front_row }
            assertListMatchesItem { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten }
        }
    }

    //endregion

    //region Request

    @Test
    fun `driver request is not made when season count is found`() = coroutineTest {
        coEvery { mockDriverRepository.getDriverSeasonCount(any()) } returns 1

        initSUT()
        sut.inputs.setup("driverId", 2020)

        coVerify(exactly = 0) {
            mockDriverRepository.fetchDriver(any())
        }
    }

    @Test
    fun `driver request is made when season count is 0`() = coroutineTest {
        coEvery { mockDriverRepository.getDriverSeasonCount(any()) } returns 0

        initSUT()

        runBlocking {
            sut.inputs.setup("driverId", 2020)
        }

        sut.outputs.list.test {
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Result && it.round == 1 }

            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_standings }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_podium }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_starts }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_finishes }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_retirements }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_best_finish }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_finishes_in_points }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_points }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_pole }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_front_row }
            assertListMatchesItem(atIndex = 0) { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten }
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls driver repository`() = coroutineTest {
        initSUT()
        sut.inputs.setup("driverId", 2020)

        runBlocking {
            sut.inputs.refresh()
        }

        coVerify {
            mockDriverRepository.fetchDriver(any())
        }
        sut.outputs.isLoading.test {
            assertValue(false)
        }
    }

    //endregion
}