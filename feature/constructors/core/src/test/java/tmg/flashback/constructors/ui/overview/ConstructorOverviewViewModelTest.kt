package tmg.flashback.constructors.ui.overview

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.constructors.R
import tmg.flashback.constructors.contract.ConstructorSeason
import tmg.flashback.constructors.contract.with
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.formula1.model.ConstructorHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.testutils.BaseTest


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
    fun `constructor data with empty results and no network shows pull to refresh`() = runTest {
        val input = ConstructorHistory.model(standings = emptyList())
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("constructorId", "name")

        sut.outputs.list.test {
            assertEquals(listOf(
                ConstructorOverviewModel.headerModel(),
                ConstructorOverviewModel.NetworkError
            ), awaitItem())
        }
    }

    @Test
    fun `constructor data with null item and no network shows pull to refresh`() = runTest {
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(null) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("constructorId", "name")

        sut.outputs.list.test {
            assertEquals(listOf(
                ConstructorOverviewModel.NetworkError
            ), awaitItem())
        }
    }

    @Test
    fun `constructor data with empty results and network shows data unavailable`() = runTest {
        val input = ConstructorHistory.model(standings = emptyList())
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.setup("constructorId", "name")

        sut.outputs.list.test {
            assertEquals(mutableListOf(
                ConstructorOverviewModel.headerModel(),
                ConstructorOverviewModel.InternalError
            ), awaitItem())
        }
    }

    @Test
    fun `constructor data with results and network shows list of results in descending order`() = runTest {
        val input = ConstructorHistory.model(standings = listOf(
            ConstructorHistorySeason.model(season = 2019),
            ConstructorHistorySeason.model(season = 2020)
        ))
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(input) }

        initSUT()
        sut.inputs.setup("constructorId", "name")

        sut.outputs.list.test {
            val item = awaitItem()
            assertTrue(item.any { it is ConstructorOverviewModel.History && it.season == 2019 })
            assertTrue(item.any { it is ConstructorOverviewModel.History && it.season == 2020 })

            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_menu_drivers })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_menu_constructors })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_grid })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_standings })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_podium })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_points })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole })
        }
    }

    //endregion

    //region Request

    @Test
    fun `constructor request is not made when season count is found`() = runTest {
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 1

        initSUT()
        sut.inputs.setup("constructorId", "name")

        coVerify(exactly = 0) {
            mockConstructorRepository.fetchConstructor(any())
        }
    }

    @Test
    fun `constructor request is made when season count is 0`() = runTest {
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 0

        initSUT()

        runBlocking {
            sut.inputs.setup("constructorId", "name")
        }

        sut.outputs.list.test {
            val item = awaitItem()
            assertTrue(item.any { it is ConstructorOverviewModel.History && it.season == 2020 })

            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_menu_constructors })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_grid })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_standings })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_podium })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_race_points })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_finishes_in_points })
            assertTrue(item.any { it is ConstructorOverviewModel.Stat && it.icon == R.drawable.ic_qualifying_pole })
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
            mockNavigator.navigate(
                Screen.ConstructorSeason.with(
                constructorId = "constructorId",
                constructorName = "name",
                season = 2020
            ))
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls constructor repository`() = runTest {
        initSUT()
        sut.inputs.setup("constructorId", "name")

        runBlocking {
            sut.inputs.refresh()
        }

        coVerify {
            mockConstructorRepository.fetchConstructor(any())
        }
        sut.outputs.showLoading.test {
            assertEquals(false, awaitItem())
        }
    }

    //endregion
}