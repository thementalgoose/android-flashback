package tmg.flashback.drivers.ui.overview

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
    fun `opening driver stat history launches stats navigation component`() = runTest {
        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.inputs.openStatHistory(DriverStatHistoryType.POLES)

        verify {
            mockDriverNavigationComponent.driverStatHistory("driverId", "firstName lastName", DriverStatHistoryType.POLES)
        }
    }

    //region List

    @Test
    fun `driver data with empty results and no network shows pull to refresh`() = runTest {
        val input = DriverHistory.model(standings = emptyList())
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.outputs.list.test {
            assertEquals(listOf(
                DriverOverviewModel.headerModel(),
                DriverOverviewModel.NetworkError
            ), awaitItem())
        }
    }

    @Test
    fun `driver data with null item and no network shows pull to refresh`() = runTest {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.outputs.list.test {
            assertEquals(listOf(
                DriverOverviewModel.NetworkError
            ), awaitItem())
        }
    }

    @Test
    fun `driver data with empty results and network shows data unavailable`() = runTest {
        val input = DriverHistory.model(standings = emptyList())
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.outputs.list.test {
            assertEquals(mutableListOf(
                DriverOverviewModel.headerModel(),
                DriverOverviewModel.InternalError
            ), awaitItem())
        }
    }

    @Test
    fun `driver data with results and network shows list of results in descending order`() = runTest {
        val input = DriverHistory.model(standings = listOf(
            DriverHistorySeason.model(season = 2019),
            DriverHistorySeason.model(season = 2020)
        ))
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }

        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        sut.outputs.list.test {
            val item = awaitItem()
            assertTrue(item.any { it is DriverOverviewModel.RacedFor && it.season == 2019 })
            assertTrue(item.any { it is DriverOverviewModel.RacedFor && it.season == 2020 })

            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_driver })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_standings })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_podium })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_starts })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_finishes })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_retirements })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_best_finish })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_points })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_front_row })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten })
        }
    }

    //endregion

    //region Request

    @Test
    fun `driver request is not made when season count is found`() = runTest {
        coEvery { mockDriverRepository.getDriverSeasonCount(any()) } returns 1

        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        coVerify(exactly = 0) {
            mockDriverRepository.fetchDriver(any())
        }
    }

    @Test
    fun `driver request is made when season count is 0`() = runTest {
        coEvery { mockDriverRepository.getDriverSeasonCount(any()) } returns 0

        initSUT()

        runBlocking {
            sut.inputs.setup("driverId", "firstName lastName")
        }

        sut.outputs.list.test {
            val item = awaitItem()
            assertTrue(item.any { it is DriverOverviewModel.RacedFor && it.season == 2020 })

            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_driver })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_standings })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_podium })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_starts })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_finishes })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_retirements })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_best_finish })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_points })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_front_row })
            assertTrue(item.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten })
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
    fun `refresh calls driver repository`() = runTest {
        initSUT()
        sut.inputs.setup("driverId", "firstName lastName")

        runBlocking {
            sut.inputs.refresh()
        }

        coVerify {
            mockDriverRepository.fetchDriver(any())
        }
        sut.outputs.showLoading.test {
            assertEquals(false, awaitItem())
        }
    }

    //endregion
}