package tmg.flashback.drivers.ui.season

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.domain.repo.DriverRepository
import tmg.flashback.drivers.R
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
    fun `driver data with empty results and no network shows pull to refresh`() = runTest {
        val input = DriverHistory.model(standings = emptyList())
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("driverId", 2020)

        sut.outputs.list.test {
            assertEquals(listOf(
                DriverSeasonModel.headerModel(),
                DriverSeasonModel.NetworkError
            ), awaitItem())
        }
    }

    @Test
    fun `driver data with null item and no network shows pull to refresh`() = runTest {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("driverId", 2020)

        sut.outputs.list.test {
            assertEquals(listOf(
                DriverSeasonModel.NetworkError
            ), awaitItem())
        }
    }

    @Test
    fun `driver data with empty results and network shows data unavailable`() = runTest {
        val input = DriverHistory.model(standings = emptyList())
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.setup("driverId", 2020)

        sut.outputs.list.test {
            assertEquals(mutableListOf(
                DriverSeasonModel.headerModel(),
                DriverSeasonModel.InternalError
            ), awaitItem())
        }
    }

    @Test
    fun `driver data with results and network shows list of results in descending order`() = runTest {
        val input = DriverHistory.model(standings = listOf(
            DriverHistorySeason.model(season = 2020)
        ))
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(input) }

        initSUT()
        sut.inputs.setup("driverId", 2020)

        sut.outputs.list.test {
            val item = awaitItem()
            assertTrue(item.any { it is DriverSeasonModel.Result && it.round == 1 })

            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_standings })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_podium })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_starts })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_finishes })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_retirements })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_best_finish })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_finishes_in_points })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_points })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_pole })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_front_row })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten })
        }
    }

    //endregion

    //region Request

    @Test
    fun `driver request is not made when season count is found`() = runTest {
        coEvery { mockDriverRepository.getDriverSeasonCount(any()) } returns 1

        initSUT()
        sut.inputs.setup("driverId", 2020)

        coVerify(exactly = 0) {
            mockDriverRepository.fetchDriver(any())
        }
    }

    @Test
    fun `driver request is made when season count is 0`() = runTest {
        coEvery { mockDriverRepository.getDriverSeasonCount(any()) } returns 0

        initSUT()

        runBlocking {
            sut.inputs.setup("driverId", 2020)
        }

        sut.outputs.list.test {
            val item = awaitItem()
            assertTrue(item.any { it is DriverSeasonModel.Result && it.round == 1 })

            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_standings })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_podium })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_starts })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_finishes })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_retirements })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_best_finish })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_finishes_in_points })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_race_points })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_pole })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_front_row })
            assertTrue(item.any { it is DriverSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten })
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls driver repository`() = runTest {
        initSUT()
        sut.inputs.setup("driverId", 2020)

        runBlocking {
            sut.inputs.refresh()
        }

        coVerify {
            mockDriverRepository.fetchDriver(any())
        }
        sut.outputs.isLoading.test {
            assertEquals(false, awaitItem())
        }
    }

    //endregion
}