package tmg.flashback.circuit

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.TrackLayout.MONACO
import tmg.flashback.circuit.list.CircuitItem
import tmg.flashback.extensions.circuitIcon
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.flashback.repo.db.stats.CircuitDB
import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.repo.models.stats.CircuitRace
import tmg.flashback.shared.sync.SyncDataItem
import tmg.flashback.testutils.*

class CircuitInfoViewModelTest: BaseTest() {

    lateinit var sut: CircuitInfoViewModel

    private val mockCircuitDB: CircuitDB = mock()
    private val mockConnectivityManager: NetworkConnectivityManager = mock()

    private val mockCircuitId: String = MONACO.circuitId
    private val mockInvalidCircuitId: String = "mockCircuitId"
    private val mockLocalDate: LocalDate = LocalDate.now()
    private val mockLocalTime: LocalTime = LocalTime.now()
    private val mockCircuitRace = CircuitRace(
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

        sut = CircuitInfoViewModel(mockCircuitDB, mockConnectivityManager, testScopeProvider)
    }

    @Test
    fun `CircuitInfoViewModel isLoading defaults to true`() {

        initSUT()

        sut.outputs.isLoading.test {
            assertValue(true)
        }
    }

    @Test
    fun `CircuitInfoViewModel when circuit provided is null and network isn't connected show no network error`() = coroutineTest {

        whenever(mockCircuitDB.getCircuit(any())).thenReturn(emptyCircuitFlow)
        whenever(mockConnectivityManager.isConnected).thenReturn(false)

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(CircuitItem.ErrorItem(SyncDataItem.NoNetwork))
        }
    }

    @Test
    fun `CircuitInfoViewModel when circuit provided is null and network is connected show internal error state`() = coroutineTest {

        whenever(mockCircuitDB.getCircuit(any())).thenReturn(emptyCircuitFlow)
        whenever(mockConnectivityManager.isConnected).thenReturn(true)

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(CircuitItem.ErrorItem(SyncDataItem.InternalError))
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
            ),
            CircuitItem.ErrorItem(SyncDataItem.ProvidedBy)
        )
        whenever(mockCircuitDB.getCircuit(any())).thenReturn(circuitWithTrackFlow)
        whenever(mockConnectivityManager.isConnected).thenReturn(true)

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
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
            ),
            CircuitItem.ErrorItem(SyncDataItem.ProvidedBy)
        )

        whenever(mockCircuitDB.getCircuit(any())).thenReturn(circuitWithoutTrackFlow)
        whenever(mockConnectivityManager.isConnected).thenReturn(true)

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `CircuitInfoViewModel loading gets set to false once data has appeared`() = coroutineTest {

        whenever(mockCircuitDB.getCircuit(any())).thenReturn(circuitWithTrackFlow)
        whenever(mockConnectivityManager.isConnected).thenReturn(true)

        initSUT()

        sut.outputs.isLoading.test {
            assertValue(true)
        }

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test {
            assertListNotEmpty()
        }
        sut.outputs.isLoading.test {
            assertValue(false)
        }
    }

    @Test
    fun `CircuitInfoViewModel click showOnMap launches show map action`() = coroutineTest {

        initSUT()

        sut.inputs.clickShowOnMap()

        sut.outputs.goToMap.test {
            assertDataEventMatches { (mapUri, _) -> mapUri.startsWith("geo:0,0?q=") }
        }
    }

    @Test
    fun `CircuitInfoViewModel click wikipedia launches wikipedia action`() = coroutineTest {

        initSUT()

        sut.inputs.clickWikipedia()

        sut.outputs.goToWikipediaPage.test {
            assertEventFired()
        }
    }

    @AfterEach
    internal fun tearDown() {

        reset(mockCircuitDB, mockConnectivityManager)
    }
}