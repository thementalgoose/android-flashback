package tmg.flashback.circuit

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.TrackLayout
import tmg.flashback.TrackLayout.MONACO
import tmg.flashback.circuit.list.CircuitItem
import tmg.flashback.extensions.circuitIcon
import tmg.flashback.repo.db.stats.CircuitDB
import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.repo.models.stats.CircuitRace
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.testutils.*

@FlowPreview
@ExperimentalCoroutinesApi
class CircuitInfoViewModelTest: BaseTest() {

    lateinit var sut: CircuitInfoViewModel

    private val mockCircuitDB: CircuitDB = mock()
    private val mockConnectivityManager: ConnectivityManager = mock()

    private val mockCircuitId: String = MONACO.circuitId
    private val mockInvalidCircuitId: String = "mockCircuitId"
    private val mockLocalDate: LocalDate = LocalDate.now()
    private val mockLocalTime: LocalTime = LocalTime.now()
    private val mockCircuitRace =CircuitRace(
        name = "Test circuit",
        season = 2020,
        round = 1,
        wikiUrl = "https://www.mockwikiurl.com",
        date = mockLocalDate,
        time = mockLocalTime
    )
    private val mockCircuit: Circuit = Circuit(
        id = mockCircuitId,
        name = "Test circuit name",
        wikiUrl = "https://www.mockwikiurl.com",
        locality = "Italy",
        country = "Italy",
        countryISO = "ITA",
        locationLat = 51.0,
        locationLng = 1.0,
        results = listOf(mockCircuitRace)
    )

    private val emptyCircuitFlow: Flow<Circuit?> = flow { emit(null) }
    private val circuitWithTrackFlow: Flow<Circuit?> = flow { emit(mockCircuit) }
    private val circuitWithoutTrackFlow: Flow<Circuit?> = flow { emit(mockCircuit.copy(id = mockInvalidCircuitId)) }

    @BeforeEach
    internal fun setUp() {

        whenever(mockCircuitDB.getCircuit(any())).thenReturn(circuitWithTrackFlow)
    }

    private fun initSUT() {

        sut = CircuitInfoViewModel(mockCircuitDB, mockConnectivityManager, testScope)
    }

    @Test
    fun `CircuitInfoViewModel isLoading defaults to true`() {

        initSUT()

        assertTrue(sut.outputs.isLoading.test().latestValue() == true)
    }

    @Test
    fun `CircuitInfoViewModel when circuit provided is null and network isn't connected show no network error`() = coroutineTest {

        whenever(mockCircuitDB.getCircuit(any())).thenReturn(emptyCircuitFlow)
        whenever(mockConnectivityManager.isConnected).thenReturn(false)

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test().assertListContains {
            it is CircuitItem.ErrorItem && it.item == SyncDataItem.NoNetwork
        }
    }

    @Test
    fun `CircuitInfoViewModel when circuit provided is null and network is connected show internal error state`() = coroutineTest {

        whenever(mockCircuitDB.getCircuit(any())).thenReturn(emptyCircuitFlow)
        whenever(mockConnectivityManager.isConnected).thenReturn(true)

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test().assertListContains {
            it is CircuitItem.ErrorItem && it.item == SyncDataItem.InternalError
        }
    }

    @Test
    fun `CircuitInfoViewModel when circuit provided is not null show correct circuit information with supported track image`() = coroutineTest {

        val expected = listOf(
            CircuitItem.TrackImage(mockCircuit.circuitIcon!!),
            CircuitItem.CircuitInfo(mockCircuit),
            CircuitItem.Race(
                name = mockCircuitRace.name,
                season = mockCircuitRace.season,
                round = mockCircuitRace.round,
                date = mockCircuitRace.date,
                time = mockCircuitRace.time
            )
        )
        whenever(mockCircuitDB.getCircuit(any())).thenReturn(circuitWithTrackFlow)
        whenever(mockConnectivityManager.isConnected).thenReturn(true)

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        assertEquals(expected, sut.outputs.list.test().latestValue())
    }

    @Test
    fun `CircuitInfoViewModel when circuit provided is not null show correct circuit information with unsupported track image`() = coroutineTest {

        val expected = listOf(
            CircuitItem.CircuitInfo(mockCircuit.copy(id = mockInvalidCircuitId)),
            CircuitItem.Race(
                name = mockCircuitRace.name,
                season = mockCircuitRace.season,
                round = mockCircuitRace.round,
                date = mockCircuitRace.date,
                time = mockCircuitRace.time
            )
        )

        whenever(mockCircuitDB.getCircuit(any())).thenReturn(circuitWithoutTrackFlow)
        whenever(mockConnectivityManager.isConnected).thenReturn(true)

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        assertEquals(expected, sut.outputs.list.test().latestValue())
    }

    @Test
    fun `CircuitInfoViewModel click showOnMap launches show map action`() {

        initSUT()

        sut.inputs.clickShowOnMap()

        sut.outputs.showOnMap.test().assertEventFired()
    }

    @Test
    fun `CircuitInfoViewModel click wikipedia does nothing and does not show wikipedia link action while functionality is missing`() {

        initSUT()

        sut.inputs.clickWikipedia()

        sut.outputs.showWikipedia.test().assertEventNotFired()
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockCircuitDB, mockConnectivityManager)
    }
}