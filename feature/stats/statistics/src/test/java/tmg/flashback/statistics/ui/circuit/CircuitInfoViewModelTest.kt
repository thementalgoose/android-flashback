package tmg.flashback.statistics.ui.circuit

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.ui.search.SearchItem
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class CircuitInfoViewModelTest: BaseTest() {

    lateinit var sut: CircuitInfoViewModel

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        every { mockConnectivityManager.isConnected } returns true
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(CircuitHistory.model()) }
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 1
        coEvery { mockCircuitRepository.fetchCircuit(any()) } returns true
    }

    private fun initSUT() {
        sut = CircuitInfoViewModel(
            mockCircuitRepository,
            mockConnectivityManager,
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
        sut.inputs.circuitId("circuitId")

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitItem.circuitInfoModel(input),
                CircuitItem.errorItemModel(SyncDataItem.PullRefresh)
            ))
        }
    }

    @Test
    fun `circuit data with null item and no network shows pull to refresh`() = coroutineTest {
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.circuitId("circuitId")

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitItem.errorItemModel(SyncDataItem.PullRefresh)
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
            sut.inputs.circuitId("circuitId")
        }

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitItem.circuitInfoModel(input),
                CircuitItem.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.CONSTRUCTOR_HISTORY_INTERNAL_ERROR))
            ))
        }
    }

    @Test
    fun `circuit data with null item and network shows data unavailable`() = coroutineTest {
        every { mockCircuitRepository.getCircuitHistory(any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.circuitId("circuitId")

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitItem.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.CONSTRUCTOR_HISTORY_INTERNAL_ERROR))
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
            sut.inputs.circuitId("circuitId")
        }

        sut.outputs.list.test {
            assertValue(listOf(
                CircuitItem.circuitInfoModel(input),
                CircuitItem.raceModel(round = 2),
                CircuitItem.raceModel(round = 1)
            ))
        }
        sut.outputs.circuitName.test {
            assertValue(CircuitItem.circuitInfoModel().circuit.data.name)
        }
    }

    //endregion

    //region Request

    @Test
    fun `circuit request is not made when rounds found in DB`() = coroutineTest {
        coEvery { mockCircuitRepository.getCircuitRounds(any()) } returns 1

        initSUT()
        runBlockingTest {
            sut.inputs.circuitId("circuitId")
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
            sut.inputs.circuitId("circuitId")
        }

        sut.outputs.list.test {
            assertValueAt(listOf(
                CircuitItem.errorItemModel(SyncDataItem.Skeleton)
            ), 0)
            assertValueAt(listOf(
                CircuitItem.circuitInfoModel(),
                CircuitItem.raceModel(),
            ), 1)
        }
        coVerify {
            mockCircuitRepository.fetchCircuit("circuitId")
        }
    }

    //endregion

    //region Map

    @Test
    fun `clicking show on map fires go to map event`() {
        val input = Location.model()
        val expectedIntent = "geo:0,0?q=${input.lat},${input.lng} (name)"
        val expectedCoords = "${input.lat},${input.lng}"

        initSUT()
        sut.inputs.clickShowOnMap(Location.model(), "name")
        sut.outputs.goToMap.test {

            assertDataEventValue(Pair(expectedIntent, expectedCoords))
        }
    }

    //endregion

    //region link

    @Test
    fun `clicking link fires go to link event`() {
        initSUT()
        sut.inputs.clickLink("link")
        sut.outputs.goToLink.test {
            assertDataEventValue("link")
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls circuit repository fetch circuit and shows loading false when done`() {
        initSUT()
        sut.inputs.circuitId("circuitId")

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
}