package tmg.flashback.stats.ui.search

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ads.repository.AdsRepository
import tmg.flashback.ads.repository.model.AdvertConfig
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.di.StatsNavigator
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.assertListDoesNotMatchItem
import tmg.testutils.livedata.test

internal class SearchViewModelTest: BaseTest() {

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConstructorsRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockDriversRepository: DriverRepository = mockk(relaxed = true)
    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockStatsNavigator: StatsNavigator = mockk(relaxed = true)
    private val mockStatisticsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)

    private lateinit var sut: SearchViewModel

    private fun initSUT() {
        sut = SearchViewModel(
            mockDriversRepository,
            mockConstructorsRepository,
            mockCircuitRepository,
            mockOverviewRepository,
            mockAdsRepository,
            mockStatisticsNavigationComponent,
            mockStatsNavigator,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockAdsRepository.advertConfig } returns AdvertConfig(onSearch = true)
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
    fun `search hides adverts if toggle is off`() {
        every { mockAdsRepository.advertConfig } returns AdvertConfig(onSearch = false)
        initSUT()
        sut.outputs.results.test {
            assertListDoesNotMatchItem { it is SearchItem.Advert }
        }
    }

    @Test
    fun `search defaults to placeholder`() {
        initSUT()
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Advert,
                SearchItem.Placeholder
            ))
        }
    }

    @Test
    fun `search category drivers returns driver models`() {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.DRIVER)
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Advert,
                SearchItem.Driver.model()
            ))
        }
    }

    @Test
    fun `search category constructors returns constructors models`() {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.CONSTRUCTOR)
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Advert,
                SearchItem.Constructor.model()
            ))
        }
    }

    @Test
    fun `search category circuits returns circuits models`() = coroutineTest {
        initSUT()
        runBlockingTest {
            sut.inputs.inputCategory(SearchCategory.CIRCUIT)
        }
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Advert,
                SearchItem.Circuit.model()
            ))
        }
    }

    @Test
    fun `search category races returns races models`() {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.RACE)
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Advert,
                SearchItem.Race.model()
            ))
        }
    }

    @Test
    fun `search shows no results when search term not met`() {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.DRIVER)
        sut.inputs.inputSearch("zzzzzz")
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.ErrorItem
            ))
        }
    }

    @Test
    fun `search refines items down`() = coroutineTest {

        val input1 = Driver.model()
        val input2 = Driver.model(id = "driverId2", firstName = "z", lastName = "fish")
        every { mockDriversRepository.getDrivers() } returns flow { emit(listOf(input1, input2)) }

        val model1 = SearchItem.Driver.model()
        val model2 = SearchItem.Driver.model(name = "z fish", driverId = "driverId2")

        initSUT()
        sut.inputs.inputCategory(SearchCategory.DRIVER)
        sut.inputs.inputSearch("")
        advanceUntilIdle()

        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Advert,
                model1,
                model2
            ))
        }

        sut.inputs.inputSearch("z")
        advanceUntilIdle()
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Advert,
                model2
            ))
        }

        sut.inputs.inputSearch("zz")
        advanceUntilIdle()
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.ErrorItem
            ))
        }

        sut.inputs.inputSearch("last")
        advanceUntilIdle()
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Advert,
                model1
            ))
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh when category is drivers fetches drivers`() = coroutineTest {
        initSUT()
        runBlockingTest {
            sut.inputs.inputCategory(SearchCategory.DRIVER)
            sut.inputs.refresh()
        }

        coVerify {
            mockDriversRepository.fetchDrivers()
        }
        sut.outputs.isLoading.test {
            assertValue(false)
        }
    }

    @Test
    fun `refresh when category is constructors fetches constructors`() = coroutineTest {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.CONSTRUCTOR)
        sut.inputs.refresh()

        coVerify {
            mockConstructorsRepository.fetchConstructors()
        }
        sut.outputs.isLoading.test {
            assertValue(false)
        }
    }

    @Test
    fun `refresh when category is circuits fetches circuits`() = coroutineTest {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.CIRCUIT)
        sut.inputs.refresh()

        coVerify {
            mockCircuitRepository.fetchCircuits()
        }
        sut.outputs.isLoading.test {
            assertValue(false)
        }
    }

    @Test
    fun `refresh when category is overview fetches overview`() = coroutineTest {
        initSUT()
        runBlockingTest {
            sut.inputs.inputCategory(SearchCategory.RACE)
            sut.inputs.refresh()
        }

        coVerify {
            mockOverviewRepository.fetchOverview()
        }
        sut.outputs.isLoading.test {
            assertValue(false)
        }
    }

    //endregion

    @Test
    fun `click item for constructor fires click event`() {
        val model = SearchItem.Constructor.model()

        initSUT()
        sut.inputs.clickItem(model)

        verify {
            mockStatsNavigator.goToConstructorOverview(any(), any())
        }
    }

    @Test
    fun `click item for driver fires click event`() {
        val model = SearchItem.Driver.model()

        initSUT()
        sut.inputs.clickItem(model)

        verify {
            mockStatsNavigator.goToDriverOverview(any(), any())
        }
    }

    @Test
    fun `click item for race fires click event`() {
        val model = SearchItem.Race.model()

        initSUT()
        sut.inputs.clickItem(model)

        verify {
            mockStatisticsNavigationComponent.weekend(any())
        }
    }

    @Test
    fun `click item for circuit fires click event`() {
        val model = SearchItem.Circuit.model()

        initSUT()
        sut.inputs.clickItem(model)

        verify {
            mockStatisticsNavigationComponent.circuit(any(), any())
        }
    }
}