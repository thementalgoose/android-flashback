package tmg.flashback.drivers.presentation.overview

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.DriverRepository
import tmg.flashback.drivers.R
import tmg.flashback.drivers.contract.DriverNavigationComponent
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.drivers.contract.model.ScreenDriverData
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest

internal class DriverOverviewViewModelTest: BaseTest() {

    private val mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockDriverNavigationComponent: DriverNavigationComponent = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)

    private lateinit var underTest: DriverOverviewViewModel

    private fun initUnderTest() {
        val savedStateHandle = SavedStateHandle(mapOf("data" to ScreenDriverData("driverId", "driverName")))
        underTest = DriverOverviewViewModel(
            driverRepository = mockDriverRepository,
            driverNavigationComponent = mockDriverNavigationComponent,
            openWebpageUseCase = mockOpenWebpageUseCase,
            networkConnectivityManager = mockNetworkConnectivityManager,
            savedStateHandle = savedStateHandle,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    private val driver: Driver = Driver.model()
    private val driverHistorySeason: DriverHistorySeason = DriverHistorySeason.model()
    private val driverHistory: DriverHistory = DriverHistory.model(
        driver = driver,
        standings = listOf(driverHistorySeason)
    )

    @BeforeEach
    internal fun setUp() {
        every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(driverHistory) }
        every { mockNetworkConnectivityManager.isConnected } returns true
        coEvery { mockDriverRepository.fetchDriver(any()) } returns true
    }


    @Test
    fun `setup adds driver id and name to state, calls refresh`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item = awaitItem()
            assertEquals("driverId", item.driverId)
            assertEquals("driverName", item.driverName)
            assertEquals(driver, item.driver)
            assertEquals(true, item.networkAvailable)
        }
    }

    @Test
    fun `open url fies open url event`() {
        initUnderTest()
        underTest.inputs.openUrl("url")
        verify {
            mockOpenWebpageUseCase.open(url = "url", title = "")
        }
    }

    @Test
    fun `refresh calls populate, fetch driver and populate`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {

            underTest.inputs.refresh()
            coVerify { mockDriverRepository.fetchDriver("driverId") }
            testScheduler.advanceUntilIdle()

            val state = awaitItem()
            assertEquals(driver, state.driver)
            assertEquals("driverId", state.driverId)
            assertEquals("driverName", state.driverName)
            assertEquals(true, state.networkAvailable)
            assertEquals(false, state.isLoading)
            assertStatModels(state.list)
            assertSeasonRacedFor(state.list, 2020)
        }
    }

    @Test
    fun `open stat history calls navigation component`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            val currentState = awaitItem()

            every { mockDriverRepository.getDriverOverview(any()) } returns flow { emit(null) }
            underTest.inputs.refresh()

            coVerify { mockDriverRepository.fetchDriver("driverId") }
            val state = awaitItem()
            assertTrue(state.networkAvailable)
        }
    }

    @Test
    fun `opening driver stat history launches stats navigation component`() = runTest(testDispatcher) {
        initUnderTest()

        underTest.inputs.openStatHistory(DriverStatHistoryType.POLES)

        verify {
            mockDriverNavigationComponent.driverStatHistory("driverId", "firstName lastName", DriverStatHistoryType.POLES)
        }
    }

    @Test
    fun `opening driver season updates selected season, back clears selected season`() = runTest {
        initUnderTest()
        underTest.inputs.openSeason(2020)
        underTest.outputs.uiState.test {
            assertEquals(2020, awaitItem().selectedSeason)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSeason)
        }
    }

    private fun assertStatModels(list: List<DriverOverviewModel>) {
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_driver })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_standings })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_podium })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_starts })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_finishes })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_retirements })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_best_finish })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_race_points })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_front_row })
        assertTrue(list.any { it is DriverOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten })
    }

    private fun assertSeasonRacedFor(list: List<DriverOverviewModel>, vararg season: Int) {
        season.forEach { year ->
            assertTrue(list.any { it is DriverOverviewModel.RacedFor && it.season == year } )
        }
    }
}