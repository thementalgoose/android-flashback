package tmg.flashback.drivers.ui.overview

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.drivers.R
import tmg.flashback.drivers.contract.DriverNavigationComponent
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.domain.repo.DriverRepository
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class DriverOverviewViewModelTest: BaseTest() {

    private val mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockDriverNavigationComponent: DriverNavigationComponent = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)

    private lateinit var sut: DriverOverviewViewModel

    private fun initSUT() {
        sut = DriverOverviewViewModel(
            mockDriverRepository,
            mockNetworkConnectivityManager,
            mockNavigator,
            mockDriverNavigationComponent,
            mockOpenWebpageUseCase,
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

    @Test
    fun `opening driver stat history launches stats navigation component`() = coroutineTest {
        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.inputs.openStatHistory(DriverStatHistoryType.POLES)

        verify {
            mockDriverNavigationComponent.driverStatHistory("driverId", "firstName lastName", DriverStatHistoryType.POLES)
        }
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

            assertListMatchesItem { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_driver }
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

        runBlocking {
            sut.inputs.setup("driverId", "firstName lastName")
        }

        sut.outputs.list.test {
            assertValue(listOf(
                DriverOverviewModel.Loading
            ), atIndex = 0)

            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.RacedFor && it.season == 2020 }

            assertListMatchesItem(atIndex = 1) { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_driver }
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
            mockOpenWebpageUseCase.open(url = "url", title = "")
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
            mockNavigator.navigate(
                Screen.DriverSeason.with(
                driverId = "driverId",
                driverName = "firstName lastName",
                season = 2020
            ))
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls driver repository`() = coroutineTest {
        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        runBlocking {
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