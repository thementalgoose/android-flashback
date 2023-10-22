package tmg.flashback.constructors.presentation.overview

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
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.formula1.model.ConstructorHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest


internal class ConstructorOverviewViewModelTest: BaseTest() {

    private val mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: ConstructorOverviewViewModel

    private fun initUnderTest() {
        underTest = ConstructorOverviewViewModel(
            constructorRepository = mockConstructorRepository,
            openWebpageUseCase = mockOpenWebpageUseCase,
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
        underTest.inputs.setup("constructorId", "constructorName")
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
        underTest.inputs.setup("constructorId", "constructorName")
        underTest.outputs.uiState.test {
            awaitItem()

            underTest.inputs.refresh()
            coVerify { mockConstructorRepository.fetchConstructor("constructorId") }
            testScheduler.advanceUntilIdle()

            val state = awaitItem()
            assertEquals(constructor, state.constructor)
            assertEquals("constructorId", state.constructorId)
            assertEquals("constructorName", state.constructorName)
            assertEquals(false, state.networkError)
            assertEquals(false, state.isLoading)
            assertStatModels(state.list)
            assertSeasonRacedFor(state.list, 2020)
        }
    }

    @Test
    fun `open stat history calls navigation component`() = runTest {
        initUnderTest()
        underTest.inputs.setup("constructorId", "constructorName")
        underTest.outputs.uiState.test {
            val currentState = awaitItem()

            every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(null) }
            underTest.inputs.refresh()

            coVerify { mockConstructorRepository.fetchConstructor("constructorId") }
            val state = awaitItem()
            assertTrue(state.networkError)
        }
    }

    @Test
    fun `opening constructor season updates selected season, back clears selected season`() = runTest {
        initUnderTest()
        underTest.inputs.setup("constructorId", "constructorName")
        underTest.inputs.openSeason(2020)
        underTest.outputs.uiState.test {
            assertEquals(2020, awaitItem().selectedSeason)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSeason)
        }
    }

    private fun assertStatModels(list: List<ConstructorOverviewModel>) {
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_standings })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_podium })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_starts })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_finishes })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_retirements })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_best_finish })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_points })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_front_row })
        assertTrue(list.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_top_ten })
    }

    private fun assertSeasonRacedFor(list: List<ConstructorOverviewModel>, vararg season: Int) {
        season.forEach { year ->
            assertTrue(list.any { it is ConstructorOverviewModel.History && it.season == year } )
        }
    }
}