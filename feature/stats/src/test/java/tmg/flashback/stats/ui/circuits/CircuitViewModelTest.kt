package tmg.flashback.stats.ui.circuits

import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.web.WebNavigationComponent
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class CircuitViewModelTest: BaseTest() {

    lateinit var sut: CircuitViewModel

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockWebNavigationComponent: WebNavigationComponent = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)

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
            mockWebNavigationComponent,
            mockStatsNavigationComponent,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    //region List

    @Test
    fun `circuit data with empty results and no network shows pull to refresh`() = coroutineTest {
        val input = CircuitHistory.model(results = emptyList())
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(input) }
        every { mockConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.load("circuitId")

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitModel.Stats.model(),
                CircuitModel.Error
            ))
        }
    }

    @Test
    fun `circuit data with null item and no network shows pull to refresh`() = coroutineTest {
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.load("circuitId")

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitModel.Error
            ))
        }
    }

    @Test
    fun `circuit data with empty results and network shows data unavailable`() = coroutineTest {
        val input = CircuitHistory.model(results = emptyList())
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(input) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        runBlockingTest {
            sut.inputs.load("circuitId")
        }

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitModel.Stats.model(),
                CircuitModel.Error
            ))
        }
    }

    @Test
    fun `circuit data with null item and network shows data unavailable`() = coroutineTest {
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.load("circuitId")

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitModel.Error
            ))
        }
    }

    @Test
    fun `circuit data with results and network shows list of results in descending order`() = coroutineTest {
        val input = CircuitHistory.model(results = listOf(
            CircuitHistoryRace.model(round = 1),
            CircuitHistoryRace.model(round = 2)
        ))
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(input) }

        initSUT()
        runBlockingTest {
            sut.inputs.load("circuitId")
        }

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitModel.Stats.model(
                    numberOfGrandPrix = 2,
                    startYear = 2020,
                    endYear = 2020
                ),
                CircuitModel.Item.model(data = CircuitHistoryRace.model(round = 2)),
                CircuitModel.Item.model(data = CircuitHistoryRace.model(round = 1)),
            ))
        }
    }

    //endregion

    //region Request

    @Test
    fun `circuit request is not made when rounds found in DB`() = coroutineTest {
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 1

        initSUT()
        runBlockingTest {
            sut.inputs.load("circuitId")
        }

        coVerify(exactly = 0) {
            mockCircuitRepository.fetchCircuit(any())
        }
    }

    @Test
    fun `circuit request is made when no rounds found in DB showing skeleton loading view`() = coroutineTest {
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 0

        initSUT()

        runBlockingTest {
            sut.inputs.load("circuitId")
        }

        sut.outputs.list.test {
            assertValueAt(listOf(
                CircuitModel.Loading
            ), 0)
            assertValueAt(listOf(
                CircuitModel.Stats.model(
                    numberOfGrandPrix = 1,
                    startYear = 2020,
                    endYear = 2020
                ),
                CircuitModel.Item.model(),
            ), 1)
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
            mockWebNavigationComponent.web("link")
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls circuit repository fetch circuit and shows loading false when done`() {
        initSUT()
        sut.inputs.load("circuitId")

        runBlockingTest {
            sut.inputs.refresh()
        }

        coVerify {
            mockCircuitRepository.fetchCircuit("circuitId")
        }
        sut.outputs.showLoading.test {
            assertValue(false)
        }
    }

    //endregion

    @Test
    fun `clicking item navigats to stats`() {
        val item: CircuitModel.Item = mockk(relaxed = true)

        initSUT()
        sut.inputs.itemClicked(item)

        verify {
            mockStatsNavigationComponent.weekend(any())
        }
    }
}