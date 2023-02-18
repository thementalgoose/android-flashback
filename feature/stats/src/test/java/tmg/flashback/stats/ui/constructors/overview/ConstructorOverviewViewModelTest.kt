package tmg.flashback.stats.ui.constructors.overview

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.formula1.model.ConstructorHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.stats.ConstructorSeason
import tmg.flashback.stats.R
import tmg.flashback.stats.with
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test


internal class ConstructorOverviewViewModelTest: BaseTest() {

    private val mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockOpenWebpageUseCase: OpenWebpageUseCase = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var sut: ConstructorOverviewViewModel

    private fun initSUT() {
        sut = ConstructorOverviewViewModel(
            mockConstructorRepository,
            mockNetworkConnectivityManager,
            mockOpenWebpageUseCase,
            mockNavigator,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(
            ConstructorHistory.model()) }
        every { mockNetworkConnectivityManager.isConnected } returns true
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 1
        coEvery { mockConstructorRepository.fetchConstructor(any()) } returns true
    }

    //region List

    @Test
    fun `constructor data with empty results and no network shows pull to refresh`() = coroutineTest {
        val input = ConstructorHistory.model(standings = emptyList())
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("constructorId", "name")

        sut.outputs.list.test {
            assertValue(listOf(
                ConstructorOverviewModel.headerModel(),
                ConstructorOverviewModel.NetworkError
            ))
        }
    }

    @Test
    fun `constructor data with null item and no network shows pull to refresh`() = coroutineTest {
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(null) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("constructorId", "name")

        sut.outputs.list.test {
            assertValue(listOf(
                ConstructorOverviewModel.NetworkError
            ))
        }
    }

    @Test
    fun `constructor data with empty results and network shows data unavailable`() = coroutineTest {
        val input = ConstructorHistory.model(standings = emptyList())
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.setup("constructorId", "name")

        sut.outputs.list.test {
            assertValue(mutableListOf(
                ConstructorOverviewModel.headerModel(),
                ConstructorOverviewModel.InternalError
            ))
        }
    }

    @Test
    fun `constructor data with results and network shows list of results in descending order`() = coroutineTest {
        val input = ConstructorHistory.model(standings = listOf(
            ConstructorHistorySeason.model(season = 2019),
            ConstructorHistorySeason.model(season = 2020)
        ))
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(input) }

        initSUT()
        sut.inputs.setup("constructorId", "name")

        sut.outputs.list.test {
            assertListMatchesItem { it is ConstructorOverviewModel.History && it.season == 2019 }
            assertListMatchesItem { it is ConstructorOverviewModel.History && it.season == 2020 }

            assertListMatchesItem { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_menu_drivers }
            assertListMatchesItem { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_menu_constructors }
            assertListMatchesItem { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_grid }
            assertListMatchesItem { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_standings }
            assertListMatchesItem { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_podium }
            assertListMatchesItem { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_points }
            assertListMatchesItem { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points }
            assertListMatchesItem { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole }
        }
    }

    //endregion

    //region Request

    @Test
    fun `constructor request is not made when season count is found`() = coroutineTest {
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 1

        initSUT()
        sut.inputs.setup("constructorId", "name")

        coVerify(exactly = 0) {
            mockConstructorRepository.fetchConstructor(any())
        }
    }

    @Test
    fun `constructor request is made when season count is 0`() = coroutineTest {
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 0

        initSUT()

        runBlocking {
            sut.inputs.setup("constructorId", "name")
        }

        sut.outputs.list.test {
            assertValueAt(listOf(
                ConstructorOverviewModel.Loading
            ), 0)

            assertListMatchesItem(atIndex = 1) { it is ConstructorOverviewModel.History && it.season == 2020 }

            assertListMatchesItem(atIndex = 1) { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_menu_constructors }
            assertListMatchesItem(atIndex = 1) { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_grid }
            assertListMatchesItem(atIndex = 1) { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_standings }
            assertListMatchesItem(atIndex = 1) { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_podium }
            assertListMatchesItem(atIndex = 1) { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_points }
            assertListMatchesItem(atIndex = 1) { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points }
            assertListMatchesItem(atIndex = 1) { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole }
        }
    }

    //endregion

    //region Url

    @Test
    fun `open url fies open url event`() {

        initSUT()
        sut.inputs.openUrl("url")
        verify {
            mockOpenWebpageUseCase.open(url = "url", title = "")
        }
    }

    //endregion

    //region Open season

    @Test
    fun `open season opens season event`() {
        initSUT()
        sut.inputs.setup("constructorId", "name")

        sut.inputs.openSeason(2020)

        verify {
            mockNavigator.navigate(Screen.ConstructorSeason.with(
                constructorId = "constructorId",
                constructorName = "name",
                season = 2020
            ))
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls constructor repository`() = coroutineTest {
        initSUT()
        sut.inputs.setup("constructorId", "name")

        runBlocking {
            sut.inputs.refresh()
        }

        coVerify {
            mockConstructorRepository.fetchConstructor(any())
        }
        sut.outputs.showLoading.test {
            assertValue(false)
        }
    }

    //endregion
}