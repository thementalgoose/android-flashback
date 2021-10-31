package tmg.flashback.statistics.ui.circuit

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.db.stats.CircuitRepository
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitRace
import tmg.flashback.formula1.model.Location
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.statistics.extensions.circuitIcon
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventMatches
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.assertListContainsItem
import tmg.testutils.livedata.assertListHasSublist
import tmg.testutils.livedata.assertListNotEmpty
import tmg.testutils.livedata.test

internal class CircuitInfoViewModelTest: BaseTest() {

    lateinit var sut: CircuitInfoViewModel

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockCrashController: CrashController = mockk(relaxed = true)

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
        location = Location(51.0, 1.0),
        results = listOf(mockCircuitRace)
    )

    private val emptyCircuitFlow: Flow<Circuit?> = flow { emit(null) }
    private val circuitWithTrackFlow: Flow<Circuit?> = flow { emit(mockCircuit) }
    private val circuitWithoutTrackFlow: Flow<Circuit?> =
        flow { emit(mockCircuit.copy(id = mockInvalidCircuitId)) }

    @BeforeEach
    internal fun setUp() {

        every { mockCircuitRepository.getCircuit(any()) } returns circuitWithTrackFlow
    }

    private fun initSUT() {

        sut = CircuitInfoViewModel(mockCircuitRepository, mockConnectivityManager, mockCrashController)
    }

    @Test
    fun `isLoading defaults to true`() {

        initSUT()

        sut.outputs.isLoading.test {
            assertValue(true)
        }
    }

    @Test
    fun `when circuit provided is null and network isn't connected show no network error`() = coroutineTest {

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
    fun `when circuit provided is null and network is connected show internal error state`() = coroutineTest {

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
    fun `when circuit provided is not null show correct circuit information with supported track image`() = coroutineTest {

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
    fun `when circuit provided is not null show correct circuit information with unsupported track image`() = coroutineTest {

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
    fun `loading gets set to false once data has appeared`() = coroutineTest {

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
    @Disabled("Due to way data is cached in VM this always resolves to empty. Working as of 6/10/21")
    fun `click showOnMap launches show map action`() = coroutineTest {

        initSUT()

        sut.inputs.clickShowOnMap()

        sut.outputs.goToMap.test {
            assertDataEventMatches { (mapUri, _) -> mapUri.startsWith("geo:0,0?q=") }
        }
    }

    @Test
    fun `click wikipedia launches wikipedia action`() = coroutineTest {

        initSUT()

        sut.inputs.clickWikipedia()

        sut.outputs.goToWikipediaPage.test {
            assertEventFired()
        }
    }
}