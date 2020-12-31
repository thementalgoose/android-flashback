package tmg.flashback.circuit

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.circuit.list.CircuitItem
import tmg.flashback.constants.TrackLayout
import tmg.flashback.extensions.circuitIcon
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.flashback.repo.db.stats.CircuitRepository
import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.repo.models.stats.CircuitRace
import tmg.flashback.shared.sync.SyncDataItem
import tmg.flashback.testutils.*

internal class CircuitInfoViewModelTest: BaseTest() {

    lateinit var sut: CircuitInfoViewModel

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    private val mockCircuitId: String = TrackLayout.MONACO.circuitId
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

        every { mockCircuitRepository.getCircuit(any()) } returns circuitWithTrackFlow
    }

    private fun initSUT() {

        sut = CircuitInfoViewModel(mockCircuitRepository, mockConnectivityManager)
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

        every { mockCircuitRepository.getCircuit(any()) } returns emptyCircuitFlow
        every { mockConnectivityManager.isConnected } returns false

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(CircuitItem.ErrorItem(SyncDataItem.NoNetwork))
        }
    }

    @Test
    fun `CircuitInfoViewModel when circuit provided is null and network is connected show internal error state`() = coroutineTest {

        every { mockCircuitRepository.getCircuit(any()) } returns emptyCircuitFlow
        every { mockConnectivityManager.isConnected } returns true

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
            )
        )

        every { mockCircuitRepository.getCircuit(any()) } returns circuitWithTrackFlow
        every { mockConnectivityManager.isConnected } returns true

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test {
            assertListHasSublist(expected)
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
            )
        )

        every { mockCircuitRepository.getCircuit(any()) } returns circuitWithoutTrackFlow
        every { mockConnectivityManager.isConnected } returns true

        initSUT()

        sut.inputs.circuitId(mockCircuitId)

        advanceUntilIdle()

        sut.outputs.list.test {
            assertListHasSublist(expected)
        }
    }

    @Test
    fun `CircuitInfoViewModel loading gets set to false once data has appeared`() = coroutineTest {

        every { mockCircuitRepository.getCircuit(any()) } returns circuitWithTrackFlow
        every { mockConnectivityManager.isConnected } returns true

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
}