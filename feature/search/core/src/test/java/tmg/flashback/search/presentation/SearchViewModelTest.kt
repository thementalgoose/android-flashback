package tmg.flashback.search.presentation

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.domain.repo.CircuitRepository
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.domain.repo.DriverRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.testutils.BaseTest

internal class SearchViewModelTest: BaseTest() {

    private val mockDriverRepository: DriverRepository = mockk(relaxed = true)
    private val mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)

    private lateinit var underTest: SearchViewModel

    private fun initUnderTest() {
        underTest = SearchViewModel(
            driverRepository = mockDriverRepository,
            constructorRepository = mockConstructorRepository,
            circuitRepository = mockCircuitRepository,
            overviewRepository = mockOverviewRepository,
            adsRepository = mockAdsRepository,
            ioDispatcher = testDispatcher,
        )
    }

    private val driver1 = Driver.model(id = "1")
    private val driver2 = Driver.model(id = "2", lastName = "x")
    private val constructor1 = Constructor.model(id = "1")
    private val constructor2 = Constructor.model(id = "2", name = "x")
    private val circuit1 = Circuit.model(id = "1")
    private val circuit2 = Circuit.model(id = "2", name = "x")
    private val race1 = OverviewRace.model(season = 2020, round = 1)
    private val race2 = OverviewRace.model(season = 2020, round = 2, raceName = "x")

    @BeforeEach
    fun setUp() {
        every { mockDriverRepository.getDrivers() } returns flow { emit(listOf(driver1, driver2)) }
        every { mockConstructorRepository.getConstructors() } returns flow { emit(listOf(constructor1, constructor2)) }
        every { mockCircuitRepository.getCircuits() } returns flow { emit(listOf(circuit1, circuit2)) }
        every { mockOverviewRepository.getOverview() } returns flow { emit(listOf(race1, race2)) }
        every { mockAdsRepository.advertConfig } returns mockk(relaxed = true) {
            every { onSearch } returns false
        }
    }

    @Test
    fun `uistate lists all options and empty search term`() = runTest {

        initUnderTest()
        underTest.uiState.test {
            val item = awaitItem()

            assertEquals(listOf(driver1, driver2), item.drivers)
            assertEquals(listOf(constructor1, constructor2), item.constructors)
            assertEquals(listOf(circuit1, circuit2), item.circuits)
            assertEquals(listOf(race2, race1), item.races)
            assertEquals("", item.searchTerm)
        }
    }

    @Test
    fun `initial state sets no selected item`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(null, awaitItem().selected)
        }
    }

    @Test
    fun `updating search term filters out drivers`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(listOf(driver1, driver2), awaitItem().drivers)

            underTest.search("x")
            assertEquals(listOf(driver2), awaitItem().drivers)

            underTest.search("xx")
            assertEquals(emptyList<Driver>(), awaitItem().drivers)

            underTest.searchClear()
            assertEquals(listOf(driver1, driver2), awaitItem().drivers)
        }
    }

    @Test
    fun `updating search term filters out constructors`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(listOf(constructor1, constructor2), awaitItem().constructors)

            underTest.search("x")
            assertEquals(listOf(constructor2), awaitItem().constructors)

            underTest.search("xx")
            assertEquals(emptyList<Constructor>(), awaitItem().constructors)

            underTest.searchClear()
            assertEquals(listOf(constructor1, constructor2), awaitItem().constructors)
        }
    }

    @Test
    fun `updating search term filters out circuits`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(listOf(circuit1, circuit2), awaitItem().circuits)

            underTest.search("x")
            assertEquals(listOf(circuit2), awaitItem().circuits)

            underTest.search("xx")
            assertEquals(emptyList<Circuit>(), awaitItem().circuits)

            underTest.searchClear()
            assertEquals(listOf(circuit1, circuit2), awaitItem().circuits)
        }
    }

    @Test
    fun `updating search term filters out races`() = runTest {
        initUnderTest()
        underTest.uiState.test {
            assertEquals(listOf(race2, race1), awaitItem().races)

            underTest.search("x")
            assertEquals(listOf(race2), awaitItem().races)

            underTest.search("xx")
            assertEquals(emptyList<OverviewRace>(), awaitItem().races)

            underTest.searchClear()
            assertEquals(listOf(race2, race1), awaitItem().races)
        }
    }

    @Test
    fun `refresh syncs all api calls`() = runTest {
        initUnderTest()
        underTest.refresh()

        coVerify {
            mockDriverRepository.fetchDrivers()
            mockConstructorRepository.fetchConstructors()
            mockCircuitRepository.fetchCircuits()
            mockOverviewRepository.fetchOverview()
        }
    }

    @Test
    fun `uistate shows ad state from search config`() = runTest {
        every { mockAdsRepository.advertConfig } returns mockk(relaxed = true) {
            every { onSearch } returns true
        }
        initUnderTest()
        underTest.uiState.test {
            assertEquals(true, awaitItem().showAdvert)
        }
    }

    @Test
    fun `clicking circuit updates state to circuit, press back sets to null`() = runTest {
        initUnderTest()
        underTest.clickCircuit(circuit1)
        underTest.uiState.test {
            assertEquals(SearchScreenSubState.Circuit(circuit1), awaitItem().selected)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selected)
        }
    }

    @Test
    fun `clicking race updates state to race, press back sets to null`() = runTest {
        initUnderTest()
        underTest.clickRace(race1)
        underTest.uiState.test {
            assertEquals(SearchScreenSubState.Race(race1), awaitItem().selected)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selected)
        }
    }

    @Test
    fun `clicking driver updates state to driver, press back sets to null`() = runTest {
        initUnderTest()
        underTest.clickDriver(driver1)
        underTest.uiState.test {
            assertEquals(SearchScreenSubState.Driver(driver1), awaitItem().selected)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selected)
        }
    }

    @Test
    fun `clicking driver season updates state to driver, press back sets to driver`() = runTest {
        initUnderTest()
        underTest.clickDriver(driver1, 2020)
        underTest.uiState.test {
            assertEquals(SearchScreenSubState.Driver(driver1, 2020), awaitItem().selected)

            underTest.inputs.back()
            assertEquals(SearchScreenSubState.Driver(driver1), awaitItem().selected)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selected)
        }
    }

    @Test
    fun `clicking constructor updates state to constructor, press back sets to null`() = runTest {
        initUnderTest()
        underTest.clickConstructor(constructor1)
        underTest.uiState.test {
            assertEquals(SearchScreenSubState.Constructor(constructor1), awaitItem().selected)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selected)
        }
    }

    @Test
    fun `clicking constructor season updates state to constructor, press back sets to constructor`() = runTest {
        initUnderTest()
        underTest.clickConstructor(constructor1, 2020)
        underTest.uiState.test {
            assertEquals(SearchScreenSubState.Constructor(constructor1, 2020), awaitItem().selected)

            underTest.inputs.back()
            assertEquals(SearchScreenSubState.Constructor(constructor1), awaitItem().selected)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selected)
        }
    }
}