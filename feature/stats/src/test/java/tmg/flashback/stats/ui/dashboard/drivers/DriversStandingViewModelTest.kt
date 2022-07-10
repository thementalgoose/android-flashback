package tmg.flashback.stats.ui.dashboard.drivers

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.usecases.FetchSeasonUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class DriversStandingViewModelTest: BaseTest() {

    private val mockSeasonRepository: SeasonRepository = mockk(relaxed = true)
    private val mockFetchSeasonUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockStatsNavigator: StatsNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: DriversStandingViewModel

    private fun initUnderTest() {
        underTest = DriversStandingViewModel(
            seasonRepository = mockSeasonRepository,
            fetchSeasonUseCase = mockFetchSeasonUseCase,
            statsNavigator = mockStatsNavigator,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockSeasonRepository.getDriverStandings(2020) } returns flow { emit(SeasonDriverStandings.model(
            standings = listOf(
                SeasonDriverStandingSeason.model(points = 2.0, driver = Driver.model(id = "1"), championshipPosition = 2),
                SeasonDriverStandingSeason.model(points = 3.0, driver = Driver.model(id = "2"), championshipPosition = 1)
            )
        )) }
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(true) }
    }

    @Test
    fun `current season use case is fetched on initial load`() {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.testObserve()

        verify { mockFetchSeasonUseCase.fetch(2020) }
    }

    @Test
    fun `loading is returned when DB returns no standings and hasnt made request`() {
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(false) }
        every { mockSeasonRepository.getDriverStandings(any()) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertValue(listOf(DriverStandingsModel.Loading))
        }
    }

    @Test
    fun `null is returned when DB returns no standings and has made request`() {
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(true) }
        every { mockSeasonRepository.getDriverStandings(any()) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertValue(null)
        }
    }

    @Test
    fun `expected list is returned when items are loaded from the DB`() {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertValue(listOf(
                DriverStandingsModel.Standings(
                    standings = SeasonDriverStandingSeason.model(points = 3.0, driver = Driver.model(id = "2"), championshipPosition = 1)
                ),
                DriverStandingsModel.Standings(
                    standings = SeasonDriverStandingSeason.model(points = 2.0, driver = Driver.model(id = "1"), championshipPosition = 2),
                )
            ))
        }
    }


    @Test
    fun `refresh calls fetch season and updates is refreshing`() {
        initUnderTest()
        underTest.load(2020)

        val refreshing = underTest.outputs.isRefreshing.testObserve()
        refreshing.assertValueAt(false, 0)
        runBlocking {
            underTest.refresh()
        }

        refreshing.assertValueAt(true, 1)
        refreshing.assertValueAt(false, 2)
        coVerify {
            mockFetchSeasonUseCase.fetchSeason(2020)
        }
    }


    @Test
    fun `clicking item goes to driver overview`() = coroutineTest {
        initUnderTest()
        underTest.load(2020)
        val model = DriverStandingsModel.Standings(
            standings = SeasonDriverStandingSeason.model(points = 3.0, driver = Driver.model(id = "2"), championshipPosition = 1)
        )

        underTest.clickItem(model)

        verify {
            mockStatsNavigator.driverOverview(
                id = model.standings.driver.id,
                name = model.standings.driver.name
            )
        }
    }
}
