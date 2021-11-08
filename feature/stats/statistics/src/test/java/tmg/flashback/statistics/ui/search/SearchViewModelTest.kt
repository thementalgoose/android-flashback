package tmg.flashback.statistics.ui.search

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SearchViewModelTest: BaseTest() {

    private val mockCircuitRepository: CircuitRepository = mockk(relaxed = true)
    private val mockConstructorsRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockDriversRepository: DriverRepository = mockk(relaxed = true)
    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)

    private lateinit var sut: SearchViewModel

    private fun initSUT() {
        sut = SearchViewModel(
            mockDriversRepository,
            mockConstructorsRepository,
            mockCircuitRepository,
            mockOverviewRepository
        )
    }

    @BeforeEach
    internal fun setUp() {
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
    fun `search defaults to placeholder`() {
        initSUT()
        sut.outputs.results.test {
            assertValue(listOf(SearchItem.Placeholder))
        }
    }

    @Test
    fun `search category drivers returns driver models`() {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.DRIVER)
        sut.outputs.results.test {
            assertValue(listOf(SearchItem.driverModel()))
        }
    }

    @Test
    fun `search category constructors returns constructors models`() {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.CONSTRUCTOR)
        sut.outputs.results.test {
            assertValue(listOf(SearchItem.constructorModel()))
        }
    }

    @Test
    fun `search category circuits returns circuits models`() = coroutineTest {
        initSUT()
        runBlockingTest {
            sut.inputs.inputCategory(SearchCategory.CIRCUIT)
        }
        sut.outputs.results.test {
            assertValue(listOf(SearchItem.circuitModel()))
        }
    }

    @Test
    fun `search category races returns races models`() {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.RACE)
        sut.outputs.results.test {
            assertValue(listOf(SearchItem.raceModel()))
        }
    }

    @Test
    fun `search shows no results when search term not met`() {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.DRIVER)
        sut.inputs.inputSearch("zzzzzz")
        sut.outputs.results.test {
            assertValue(listOf(SearchItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.NO_SEARCH_RESULTS))))
        }
    }

    @Test
    fun `search refines items down`() = coroutineTest {

        val input1 = Driver.model()
        val input2 = Driver.model(id = "driverId2", firstName = "z", lastName = "fish")
        every { mockDriversRepository.getDrivers() } returns flow { emit(listOf(input1, input2)) }

        val model1 = SearchItem.driverModel()
        val model2 = SearchItem.driverModel(name = "z fish", driverId = "driverId2")

        initSUT()
        runBlockingTest {
            sut.inputs.inputCategory(SearchCategory.DRIVER)
            sut.inputs.inputSearch("")
        }
        sut.outputs.results.test {
            assertValue(listOf(model1, model2))
        }

        sut.inputs.inputSearch("z")
        sut.outputs.results.test {
            assertValue(listOf(model2))
        }

        sut.inputs.inputSearch("zz")
        sut.outputs.results.test {
            assertValue(listOf(SearchItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.NO_SEARCH_RESULTS))))
        }

        sut.inputs.inputSearch("last")
        sut.outputs.results.test {
            assertValue(listOf(model1))
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh when category is drivers fetches drivers`() = coroutineTest {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.DRIVER)
        sut.inputs.refresh()

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
        sut.inputs.inputCategory(SearchCategory.RACE)
        sut.inputs.refresh()

        coVerify {
            mockOverviewRepository.fetchOverview()
        }
        sut.outputs.isLoading.test {
            assertValue(false)
        }
    }

    //endregion

    @Test
    fun `click item fires click event`() {
        val model = SearchItem.constructorModel()

        initSUT()
        sut.inputs.clickItem(model)

        sut.outputs.openLink.test {
            assertDataEventValue(model)
        }
    }

    @Test
    fun `clicking open category opens the category event`() {
        initSUT()
        sut.inputs.openCategory()

        sut.outputs.openCategoryPicker.test {
            assertEventFired()
        }
    }
}