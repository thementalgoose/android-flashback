package tmg.flashback.stats.ui.drivers.overview

import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.stats.R
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.web.WebNavigationComponent
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class DriverOverviewViewModelTest: BaseTest() {

    private val mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)
    private val mockWebNavigationComponent: WebNavigationComponent = mockk(relaxed = true)

    private lateinit var sut: DriverOverviewViewModel

    private fun initSUT() {
        sut = DriverOverviewViewModel(
            mockDriverRepository,
            mockNetworkConnectivityManager,
            mockStatsNavigationComponent,
            mockWebNavigationComponent,
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
        sut.inputs.setup("driverId", "firstName lastName")

        sut.outputs.list.test {
            assertValue(listOf(
                DriverOverviewModel.headerModel(),
                DriverOverviewModel.NetworkError
            ))
        }
    }

    @Test
    fun `driver data with null item and no network shows pull to refresh`() = coroutineTest {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.outputs.list.test {
            assertValue(listOf(
                DriverOverviewModel.NetworkError
            ))
        }
    }

    @Test
    fun `driver data with empty results and network shows data unavailable`() = coroutineTest {
        val input = DriverHistory.model(standings = emptyList())
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.outputs.list.test {
            assertValue(mutableListOf(
                DriverOverviewModel.headerModel(),
                DriverOverviewModel.InternalError
            ))
        }
    }

    @Test
    fun `driver data with results and network shows list of results in descending order`() = coroutineTest {
        val input = DriverHistory.model(standings = listOf(
            DriverHistorySeason.model(season = 2019),
            DriverHistorySeason.model(season = 2020)
        ))
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }

        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.outputs.list.test {
            assertListMatchesItem { it is DriverOverviewModel.RacedFor && it.season == 2019 }
            assertListMatchesItem { it is DriverOverviewModel.RacedFor && it.season == 2020 }

            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_menu_drivers }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_standings }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_podium }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_starts }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_finishes }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_retirements }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_best_finish }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_points }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_front_row }
            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten }
        }
    }

    //endregion

    //region Request

    @Test
    fun `driver request is not made when season count is found`() = coroutineTest {
        coEvery { mockDriverRepository.getDriverSeasonCount(any()) } returns 1

        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        coVerify(exactly = 0) {
            mockDriverRepository.fetchDriver(any())
        }
    }

    @Test
    fun `driver request is made when season count is 0`() = coroutineTest {
        coEvery { mockDriverRepository.getDriverSeasonCount(any()) } returns 0

        initSUT()

        runBlockingTest {
            sut.inputs.setup("driverId", "firstName lastName")
        }

        sut.outputs.list.test {
            assertValueAt(listOf(
                DriverOverviewModel.Loading
            ), 0)

            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.RacedFor && it.season == 2020 }

            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_menu_drivers }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_standings }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_podium }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_starts }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_finishes }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_retirements }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_best_finish }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_points }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_front_row }
            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten }
        }
    }

    //endregion

    //region Url

    @Test
    fun `open url fies open url event`() {

        initSUT()
        sut.inputs.openUrl("url")
        verify {
            mockWebNavigationComponent.web("url")
        }
    }

    //endregion

    //region Open season

    @Test
    fun `open season opens season event`() {
        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.inputs.openSeason(2020)
        verify {
            mockStatsNavigationComponent.driverSeason("driverId", "firstName lastName", 2020)
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls driver repository`() = coroutineTest {
        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        runBlockingTest {
            sut.inputs.refresh()
        }

        coVerify {
            mockDriverRepository.fetchDriver(any())
        }
        sut.outputs.showLoading.test {
            assertValue(false)
        }
    }

    //endregion
}