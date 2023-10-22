package tmg.flashback.circuits.presentation

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
import tmg.flashback.circuits.contract.model.ScreenCircuitData
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.CircuitRepository
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.model
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest

internal class CircuitViewModelTest: BaseTest() {

    private lateinit var underTest: CircuitViewModel

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)

    private val circuit: Circuit = Circuit.model()
    private val circuitHistoryRace: CircuitHistoryRace = CircuitHistoryRace.model()
    private val circuitHistory: CircuitHistory = CircuitHistory.model(
        data = circuit,
        results = listOf(circuitHistoryRace)
    )

    @BeforeEach
    internal fun setUp() {
        every { mockConnectivityManager.isConnected } returns true
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(circuitHistory) }
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 1
        coEvery { mockCircuitRepository.fetchCircuit(any()) } returns true
    }

    private fun initUnderTest() {
        val savedStateHandle = SavedStateHandle(mapOf("data" to ScreenCircuitData("circuitId", "circuitName")))
        underTest = CircuitViewModel(
            circuitRepository = mockCircuitRepository,
            openWebpageUseCase = mockOpenWebpageUseCase,
            networkConnectivityManager = mockConnectivityManager,
            savedStateHandle = savedStateHandle,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @Test
    fun `setup adds circuit id and name to state, calls refresh`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            val item = awaitItem()
            assertEquals("circuitId", item.circuitId)
            assertEquals("circuitName", item.circuitName)
            assertEquals(circuit, item.circuit)
            assertEquals(true, item.networkAvailable)
        }
    }

    @Test
    fun `open url fies open url event`() {
        initUnderTest()
        underTest.inputs.linkClicked("url")
        verify {
            mockOpenWebpageUseCase.open(url = "url", title = "")
        }
    }

    @Test
    fun `refresh calls populate, fetch circuit and populate`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {

            underTest.inputs.refresh()
            coVerify { mockCircuitRepository.fetchCircuit("circuitId") }
            testScheduler.advanceUntilIdle()

            val state = awaitItem()
            assertEquals(circuit, state.circuit)
            assertEquals("circuitId", state.circuitId)
            assertEquals("circuitName", state.circuitName)
            assertEquals(true, state.networkAvailable)
            assertEquals(false, state.isLoading)
            assertStatModels(state.list)
            assertRaces(state.list)
        }
    }

    @Test
    fun `open stat history calls navigation component`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            val currentState = awaitItem()

            every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(null) }
            underTest.inputs.refresh()

            coVerify { mockCircuitRepository.fetchCircuit("circuitId") }
            val state = awaitItem()
            assertTrue(state.networkAvailable)
        }
    }

    @Test
    fun `opening circuit season updates selected season, back clears selected season`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(CircuitModel.Item.model())
        underTest.outputs.uiState.test {
            assertEquals(CircuitHistoryRace.model(), awaitItem().selectedRace)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedRace)
        }
    }

    private fun assertStatModels(list: List<CircuitModel>) {
        assertTrue(list.count { it is CircuitModel.Stats } == 1)
        val item = list.firstOrNull { it is CircuitModel.Stats } as CircuitModel.Stats
        assertEquals(circuit.id, item.circuitId)
        assertEquals(circuit.name, item.name)
        assertEquals(circuit.country, item.country)
        assertEquals(circuit.countryISO, item.countryISO)
        assertEquals(1, item.numberOfGrandPrix)
        assertEquals(2020, item.startYear)
        assertEquals(2020, item.endYear)
    }

    private fun assertRaces(list: List<CircuitModel>) {
        assertTrue(list.any { it is CircuitModel.Item && it.data == circuitHistoryRace })
    }
}