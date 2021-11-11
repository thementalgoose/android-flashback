package tmg.flashback.statistics.ui.overview.constructor

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.formula1.model.ConstructorHistorySeason
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.R
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryItem
import tmg.flashback.statistics.ui.overview.constructor.summary.errorItemModel
import tmg.flashback.statistics.ui.overview.constructor.summary.headerModel
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class ConstructorViewModelTest: BaseTest() {

    private val mockConstructorRepository: ConstructorRepository = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    private lateinit var sut: ConstructorViewModel

    private fun initSUT() {
        sut = ConstructorViewModel(
            mockConstructorRepository,
            mockNetworkConnectivityManager,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(ConstructorHistory.model()) }
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
        sut.inputs.setup("constructorId")

        sut.outputs.list.test {
            assertValue(listOf(
                ConstructorSummaryItem.headerModel(),
                ConstructorSummaryItem.errorItemModel(SyncDataItem.PullRefresh)
            ))
        }
    }

    @Test
    fun `constructor data with null item and no network shows pull to refresh`() = coroutineTest {
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(null) }
        every { mockNetworkConnectivityManager.isConnected } returns false

        initSUT()
        sut.inputs.setup("constructorId")

        sut.outputs.list.test {
            assertValue(listOf(
                ConstructorSummaryItem.errorItemModel(SyncDataItem.PullRefresh)
            ))
        }
    }

    @Test
    fun `constructor data with empty results and network shows data unavailable`() = coroutineTest {
        val input = ConstructorHistory.model(standings = emptyList())
        every { mockConstructorRepository.getConstructorOverview(any()) } returns flow { emit(input) }
        every { mockNetworkConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.setup("constructorId")

        sut.outputs.list.test {
            assertValue(mutableListOf(
                ConstructorSummaryItem.headerModel(),
                ConstructorSummaryItem.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.CONSTRUCTOR_HISTORY_INTERNAL_ERROR))
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
        sut.inputs.setup("constructorId")

        sut.outputs.list.test {
            assertListMatchesItem { it is ConstructorSummaryItem.History && it.season == 2019 }
            assertListMatchesItem { it is ConstructorSummaryItem.History && it.season == 2020 }

            assertListMatchesItem { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_menu_constructors }
            assertListMatchesItem { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_race_grid }
            assertListMatchesItem { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_standings }
            assertListMatchesItem { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_podium }
            assertListMatchesItem { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_race_points }
            assertListMatchesItem { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_finishes_in_points }
            assertListMatchesItem { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_qualifying_pole }
        }
    }

    //endregion

    //region Request

    @Test
    fun `constructor request is not made when season count is found`() = coroutineTest {
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 1

        initSUT()
        sut.inputs.setup("constructorId")

        coVerify(exactly = 0) {
            mockConstructorRepository.fetchConstructor(any())
        }
    }

    @Test
    fun `constructor request is made when season count is 0`() = coroutineTest {
        coEvery { mockConstructorRepository.getConstructorSeasonCount(any()) } returns 0

        initSUT()

        runBlockingTest {
            sut.inputs.setup("constructorId")
        }

        sut.outputs.list.test {
            assertValueAt(listOf(
                ConstructorSummaryItem.errorItemModel(SyncDataItem.Skeleton)
            ), 0)

            assertListMatchesItem(atIndex = 1) { it is ConstructorSummaryItem.History && it.season == 2020 }

            assertListMatchesItem(atIndex = 1) { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_menu_constructors }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_race_grid }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_standings }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_podium }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_race_points }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_finishes_in_points }
            assertListMatchesItem(atIndex = 1) { it is ConstructorSummaryItem.Stat && it.icon == R.drawable.ic_qualifying_pole }
        }
    }

    //endregion

    //region Url

    @Test
    fun `open url fies open url event`() {

        initSUT()
        sut.inputs.openUrl("url")
        sut.outputs.openUrl.test {
            assertDataEventValue("url")
        }
    }

    //endregion

    //region Open season

    @Test
    fun `open season opens season event`() {
        initSUT()
        sut.inputs.setup("constructorId")

        sut.inputs.openSeason(2020)
        sut.outputs.openSeason.test {
            assertDataEventValue(Pair("constructorId", 2020))
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls constructor repository`() = coroutineTest {
        initSUT()
        sut.inputs.setup("constructorId")

        runBlockingTest {
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