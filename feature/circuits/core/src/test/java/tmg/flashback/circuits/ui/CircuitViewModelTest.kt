package tmg.flashback.circuits.ui

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.CircuitRepository
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Navigator
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest

internal class CircuitViewModelTest: BaseTest() {

    lateinit var sut: CircuitViewModel

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        every { mockConnectivityManager.isConnected } returns true
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(CircuitHistory.model()) }
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 1
        coEvery { mockCircuitRepository.fetchCircuit(any()) } returns true
    }

    private fun initSUT() {
        sut = CircuitViewModel(
            mockCircuitRepository,
            mockConnectivityManager,
            mockOpenWebpageUseCase,
            mockNavigator,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    //region List

    @Test
    fun `circuit data with empty results and no network shows pull to refresh`() = runTest {
        val input = CircuitHistory.model(results = emptyList())
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(input) }
        every { mockConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.load("circuitId")

        sut.outputs.list.test {
            assertEquals(listOf(
                CircuitModel.Stats.model(),
                CircuitModel.Error
            ), awaitItem())
        }
    }

    @Test
    fun `circuit data with null item and no network shows pull to refresh`() = runTest {
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.load("circuitId")

        sut.outputs.list.test {
            assertEquals(listOf(
                CircuitModel.Error
            ), awaitItem())
        }
    }

    @Test
    fun `circuit data with empty results and network shows data unavailable`() = runTest {
        val input = CircuitHistory.model(results = emptyList())
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(input) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        runBlocking {
            sut.inputs.load("circuitId")
        }

        sut.outputs.list.test {
            assertEquals(listOf(
                CircuitModel.Stats.model(),
                CircuitModel.Error
            ), awaitItem())
        }
    }

    @Test
    fun `circuit data with null item and network shows data unavailable`() = runTest {
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.load("circuitId")

        sut.outputs.list.test {
            assertEquals(listOf(
                CircuitModel.Error
            ), awaitItem())
        }
    }

    @Test
    fun `circuit data with results and network shows list of results in descending order`() = runTest {
        val input = CircuitHistory.model(results = listOf(
            CircuitHistoryRace.model(round = 1),
            CircuitHistoryRace.model(round = 2)
        ))
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(input) }

        initSUT()
        runBlocking {
            sut.inputs.load("circuitId")
        }

        sut.outputs.list.test {
            assertEquals(listOf(
                CircuitModel.Stats.model(
                    numberOfGrandPrix = 2,
                    startYear = 2020,
                    endYear = 2020
                ),
                CircuitModel.Item.model(data = CircuitHistoryRace.model(round = 2)),
                CircuitModel.Item.model(data = CircuitHistoryRace.model(round = 1)),
            ), awaitItem())
        }
    }

    //endregion

    //region Request

    @Test
    fun `circuit request is not made when rounds found in DB`() = runTest {
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 1

        initSUT()
        runBlocking {
            sut.inputs.load("circuitId")
        }

        coVerify(exactly = 0) {
            mockCircuitRepository.fetchCircuit(any())
        }
    }

    @Test
    fun `circuit request is made when no rounds found in DB showing skeleton loading view`() = runTest {
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 0

        initSUT()

        runBlocking {
            sut.inputs.load("circuitId")
        }

        sut.outputs.list.test {
            assertEquals(listOf(
                CircuitModel.Stats.model(
                    numberOfGrandPrix = 1,
                    startYear = 2020,
                    endYear = 2020
                ),
                CircuitModel.Item.model(),
            ), awaitItem())
        }
        coVerify {
            mockCircuitRepository.fetchCircuit("circuitId")
        }
    }

    //endregion

    //region link

    @Test
    fun `clicking link fires go to link event`() {
        initSUT()
        sut.inputs.linkClicked("link")

        verify {
            mockOpenWebpageUseCase.open(url = "link", title = "")
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls circuit repository fetch circuit and shows loading false when done`() = runTest {
        initSUT()
        sut.inputs.load("circuitId")

        runBlocking {
            sut.inputs.refresh()
        }

        coVerify {
            mockCircuitRepository.fetchCircuit("circuitId")
        }
        sut.outputs.showLoading.test {
            assertEquals(false, awaitItem())
        }
    }

    //endregion

    @Test
    fun `clicking item navigats to stats`() {
        val item: CircuitModel.Item = mockk(relaxed = true)

        initSUT()
        sut.inputs.itemClicked(item)

        val slot = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(slot))
        }
        assertTrue(slot.captured.route.startsWith("weekend"))
    }
}