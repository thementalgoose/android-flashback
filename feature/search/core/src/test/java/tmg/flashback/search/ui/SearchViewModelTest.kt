package tmg.flashback.search.ui

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.ads.repository.model.AdvertConfig
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.with
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.with
import tmg.flashback.domain.repo.CircuitRepository
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.domain.repo.DriverRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.with
import tmg.testutils.BaseTest

internal class SearchViewModelTest: BaseTest() {

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConstructorsRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockDriversRepository: DriverRepository = mockk(relaxed = true)
    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)

    private lateinit var sut: SearchViewModel

    private fun initSUT() {
        sut = SearchViewModel(
            mockDriversRepository,
            mockConstructorsRepository,
            mockCircuitRepository,
            mockOverviewRepository,
            mockAdsRepository,
            mockNavigator,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockAdsRepository.advertConfig } returns AdvertConfig(
            onSearch = true
        )
        every { mockCircuitRepository.getCircuits() } returns flow {
            emit(listOf(Circuit.model()))
        }
        every { mockConstructorsRepository.getConstructors() } returns flow {
            emit(listOf(Constructor.model()))
        }
        every { mockDriversRepository.getDrivers() } returns flow {
            emit(listOf(Driver.model()))
        }
        every { mockOverviewRepository.getOverview() } returns flow {
            emit(listOf(OverviewRace.model()))
        }
    }

    //region Content

    @Test
    fun `search hides adverts if toggle is off`() = runTest {
        every { mockAdsRepository.advertConfig } returns AdvertConfig(
            onSearch = false
        )
        initSUT()
        sut.outputs.results.test {
            assertFalse(awaitItem().any { it is SearchItem.Advert })
        }
    }

    @Test
    fun `search defaults to placeholder`() = runTest {
        initSUT()
        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.Advert,
                SearchItem.Placeholder
            ), awaitItem())
        }
    }

    @Test
    fun `search category drivers returns driver models`() = runTest {
        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.DRIVER)
        }
        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.Advert,
                SearchItem.Driver.model()
            ), awaitItem())
        }
    }

    @Test
    fun `search category constructors returns constructors models`() = runTest {
        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.CONSTRUCTOR)
        }
        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.Advert,
                SearchItem.Constructor.model()
            ), awaitItem())
        }
    }

    @Test
    fun `search category circuits returns circuits models`() = runTest {
        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.CIRCUIT)
        }
        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.Advert,
                SearchItem.Circuit.model()
            ), awaitItem())
        }
    }

    @Test
    fun `search category races returns races models`() = runTest {
        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.RACE)
        }
        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.Advert,
                SearchItem.Race.model()
            ), awaitItem())
        }
    }

    @Test
    fun `search shows no results when search term not met`() = runTest {
        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.DRIVER)
            sut.inputs.inputSearch("zzzzzz")
        }
        sut.outputs.isLoading.test {
            assertEquals(false, awaitItem())
        }
        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.ErrorItem
            ), awaitItem())
        }
    }

    @Test
    fun `search refines items down`() = runTest {

        val input1 = Driver.model()
        val input2 = Driver.model(id = "driverId2", firstName = "z", lastName = "fish")
        every { mockDriversRepository.getDrivers() } returns flow { emit(listOf(input1, input2)) }

        val model1 = SearchItem.Driver.model()
        val model2 = SearchItem.Driver.model(name = "z fish", driverId = "driverId2")

        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.DRIVER)
            sut.inputs.inputSearch("")
        }
        advanceUntilIdle()

        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.Advert,
                model1,
                model2
            ), awaitItem())
        }

        runBlocking {
            sut.inputs.inputSearch("z")
        }
        advanceUntilIdle()
        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.Advert,
                model2
            ), awaitItem())
        }

        runBlocking {
            sut.inputs.inputSearch("zz")
        }
        advanceUntilIdle()
        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.ErrorItem
            ), awaitItem())
        }

        runBlocking {
            sut.inputs.inputSearch("last")
        }
        advanceUntilIdle()
        sut.outputs.results.test {
            assertEquals(listOf(
                SearchItem.Advert,
                model1
            ), awaitItem())
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh when category is drivers fetches drivers`() = runTest {
        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.DRIVER)
            sut.inputs.refresh()
        }

        coVerify {
            mockDriversRepository.fetchDrivers()
        }
        sut.outputs.isLoading.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `refresh when category is constructors fetches constructors`() = runTest {
        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.CONSTRUCTOR)
            sut.inputs.refresh()
        }

        coVerify {
            mockConstructorsRepository.fetchConstructors()
        }
        sut.outputs.isLoading.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `refresh when category is circuits fetches circuits`() = runTest {
        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.CIRCUIT)
            sut.inputs.refresh()
        }

        coVerify {
            mockCircuitRepository.fetchCircuits()
        }
        sut.outputs.isLoading.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `refresh when category is overview fetches overview`() = runTest {
        initSUT()
        runBlocking {
            sut.inputs.inputCategory(SearchCategory.RACE)
            sut.inputs.refresh()
        }

        coVerify {
            mockOverviewRepository.fetchOverview()
        }
        sut.outputs.isLoading.test {
            assertEquals(false, awaitItem())
        }
    }

    //endregion

    @Test
    fun `click item for constructor fires click event`() = runTest {
        val model = SearchItem.Constructor.model()

        initSUT()
        runBlocking {
            sut.inputs.clickItem(model)
        }

        verify {
            mockNavigator.navigate(
                Screen.Constructor.with(
                model.constructorId, model.name
            ))
        }
    }

    @Test
    fun `click item for driver fires click event`() = runTest {
        val model = SearchItem.Driver.model()

        initSUT()
        runBlocking {
            sut.inputs.clickItem(model)
        }

        verify {
            mockNavigator.navigate(
                Screen.Driver.with(
                model.driverId, model.name
            ))
        }
    }

    @Test
    fun `click item for race fires click event`() = runTest {
        val model = SearchItem.Race.model()

        initSUT()
        runBlocking {
            sut.inputs.clickItem(model)
        }

        verify {
            mockNavigator.navigate(
                Screen.Weekend.with(
                ScreenWeekendData(
                    season = model.season,
                    round = model.round,
                    raceName = model.raceName,
                    circuitId = model.circuitId,
                    circuitName = model.circuitName,
                    country = model.country,
                    countryISO = model.countryISO,
                    date = model.date
                )
            ))
        }
    }

    @Test
    fun `click item for circuit fires click event`() = runTest {
        val model = SearchItem.Circuit.model()

        initSUT()
        runBlocking {
            sut.inputs.clickItem(model)
        }

        verify {
            mockNavigator.navigate(
                Screen.Circuit.with(
                model.circuitId,
                model.name
            ))
        }
    }
}