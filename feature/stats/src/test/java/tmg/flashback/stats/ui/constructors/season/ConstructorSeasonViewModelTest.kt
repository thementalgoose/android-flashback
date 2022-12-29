package tmg.flashback.stats.ui.constructors.season

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
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.stats.R
import tmg.flashback.web.WebNavigationComponent
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class ConstructorSeasonViewModelTest: BaseTest() {

    private val mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockWebNavigationComponent: WebNavigationComponent = mockk(relaxed = true)

    private lateinit var sut: ConstructorSeasonViewModel

    private fun initSUT() {
        sut = ConstructorSeasonViewModel(
            mockConstructorRepository,
            mockNetworkConnectivityManager,
            mockWebNavigationComponent,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow {
            emit(
                ConstructorHistory.model()
            )
        }
        every { mockNetworkConnectivityManager.isConnected } returns true
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 1
        coEvery { mockConstructorRepository.fetchConstructor(any()) } returns true
    }

    //region List

    @Test
    fun `constructor data with empty results and no network shows pull to refresh`() =
        coroutineTest {
            val input = ConstructorHistory.model(standings = emptyList())
            every { mockConstructorRepository.getConstructorOverview(any()) } returns flow {
                emit(
                    input
                )
            }
            every { mockNetworkConnectivityManager.isConnected } returns false

            initSUT()
            sut.inputs.setup("constructorId", 2020)

            sut.outputs.list.test {
                assertValue(
                    listOf(
                        ConstructorSeasonModel.headerModel(),
                        ConstructorSeasonModel.NetworkError
                    )
                )
            }
        }

    @Test
    fun `constructor data with null item and no network shows pull to refresh`() = coroutineTest {
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(null) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("constructorId", 2020)

        sut.outputs.list.test {
            assertValue(
                listOf(
                    ConstructorSeasonModel.NetworkError
                )
            )
        }
    }

    @Test
    fun `constructor data with empty results and network shows data unavailable`() = coroutineTest {
        val input = ConstructorHistory.model(standings = emptyList())
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.setup("constructorId", 2020)

        sut.outputs.list.test {
            assertValue(
                mutableListOf(
                    ConstructorSeasonModel.headerModel(),
                    ConstructorSeasonModel.InternalError
                )
            )
        }
    }

    @Test
    fun `constructor data with results and network shows list of results in descending order`() =
        coroutineTest {
            val input = ConstructorHistory.model(
                standings = listOf(
                    ConstructorHistorySeason.model(season = 2020)
                )
            )
            every { mockConstructorRepository.getConstructorOverview(any()) } returns flow {
                emit(input)
            }

            initSUT()
            sut.inputs.setup("constructorId", 2020)

            sut.outputs.list.test {
                assertListMatchesItem { it is ConstructorSeasonModel.Driver && it.data.driver.driver.id == Driver.model().id }

                assertListMatchesItem { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_menu_constructors }
                assertListMatchesItem { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_race_grid }
                assertListMatchesItem { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_standings }
                assertListMatchesItem { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_podium }
                assertListMatchesItem { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_race_points }
                assertListMatchesItem { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_finishes_in_points }
                assertListMatchesItem { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_pole }
            }
        }

    //endregion

    //region Request

    @Test
    fun `constructor request is not made when season count is found`() = coroutineTest {
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 1

        initSUT()
        sut.inputs.setup("constructorId", 2020)

        coVerify(exactly = 0) {
            mockConstructorRepository.fetchConstructor(any())
        }
    }

    @Test
    fun `constructor request is made when season count is 0`() = coroutineTest {
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 0

        initSUT()

        runBlocking {
            sut.inputs.setup("constructorId", 2020)
        }

        sut.outputs.list.test {
            assertListMatchesItem(atIndex = 0) { it is ConstructorSeasonModel.Loading }

            assertListMatchesItem(atIndex = 1) { it is ConstructorSeasonModel.Driver && it.data.driver.driver.id == Driver.model().id }

            assertListMatchesItem(atIndex = 1) { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_menu_constructors }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_race_grid }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_standings }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_podium }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_race_points }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_finishes_in_points }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSeasonModel.Stat && it.icon == R.drawable.ic_qualifying_pole }
        }
    }

    //endregion

    //region Url

    @Test
    fun `open url fies open url event`() {

        initSUT()
        sut.inputs.openUrl("url")
        verify {
            mockWebNavigationComponent.web("url")
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls constructor repository`() = coroutineTest {
        initSUT()
        sut.inputs.setup("constructorId", 2020)

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