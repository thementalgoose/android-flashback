package tmg.flashback.constructors.presentation.overview

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
import tmg.flashback.constructors.R
import tmg.flashback.formula1.R.drawable
import tmg.flashback.constructors.contract.model.ScreenConstructorData
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.formula1.model.ConstructorHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest


internal class ConstructorOverviewViewModelTest: BaseTest() {

    private val mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)

    private lateinit var underTest: ConstructorOverviewViewModel

    private fun initUnderTest() {
        val state = SavedStateHandle(mapOf("data" to ScreenConstructorData("constructorId", "constructorName")))
        underTest = ConstructorOverviewViewModel(
            constructorRepository = mockConstructorRepository,
            openWebpageUseCase = mockOpenWebpageUseCase,
            networkConnectivityManager = mockNetworkConnectivityManager,
            savedStateHandle = state,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }


    private val constructor: Constructor = Constructor.model()
    private val constructorHistorySeason: ConstructorHistorySeason = ConstructorHistorySeason.model()
    private val constructorHistory: ConstructorHistory = ConstructorHistory.model(
        constructor = constructor,
        standings = listOf(constructorHistorySeason)
    )

    @BeforeEach
    internal fun setUp() {
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(constructorHistory) }
        every { mockNetworkConnectivityManager.isConnected } returns true
        coEvery { mockConstructorRepository.fetchConstructor(any()) } returns true
    }

    @Test
    fun `setup adds constructor id and name to state, calls refresh`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item = awaitItem()
            assertEquals("constructorId", item.constructorId)
            assertEquals("constructorName", item.constructorName)
            assertEquals(constructor, item.constructor)
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
    fun `refresh calls populate, fetch constructor and populate`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {

            underTest.inputs.refresh()
            coVerify { mockConstructorRepository.fetchConstructor("constructorId") }
            testScheduler.advanceUntilIdle()

            val state = awaitItem()
            println(state)
            assertEquals(constructor, state.constructor)
            assertEquals("constructorId", state.constructorId)
            assertEquals("constructorName", state.constructorName)
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

            every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(null) }
            underTest.inputs.refresh()

            coVerify { mockConstructorRepository.fetchConstructor("constructorId") }
            val state = awaitItem()
            assertTrue(state.networkAvailable)
        }
    }

    @Test
    fun `opening constructor season updates selected season, back clears selected season`() = runTest {
        initUnderTest()
        underTest.inputs.openSeason(2020)
        underTest.outputs.uiState.test {
            assertEquals(2020, awaitItem().selectedSeason)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSeason)
        }
    }

    private fun assertStatModels(list: List<ConstructorOverviewModel>) {
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == tmg.flashback.ui.R.drawable.ic_menu_constructors })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == tmg.flashback.ui.R.drawable.ic_menu_drivers })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == drawable.ic_race_grid })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == drawable.ic_standings })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == drawable.ic_podium })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == drawable.ic_race_points })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == drawable.ic_finishes_in_points })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == drawable.ic_qualifying_pole })
    }

    private fun assertSeasonRacedFor(list: List<ConstructorOverviewModel>, vararg season: Int) {
        season.forEach { year ->
            assertTrue(list.any { it is ConstructorOverviewModel.History && it.season == year } )
        }
    }
}