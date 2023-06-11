package tmg.flashback.results.ui.dashboard.drivers

import app.cash.turbine.Event
import app.cash.turbine.test
import app.cash.turbine.testIn
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.usecases.FetchSeasonUseCase
import tmg.testutils.BaseTest

internal class DriversStandingViewModelTest: BaseTest() {

    private val mockSeasonRepository: SeasonRepository = mockk(relaxed = true)
    private val mockFetchSeasonUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: DriversStandingViewModel

    private fun initUnderTest() {
        underTest = DriversStandingViewModel(
            seasonRepository = mockSeasonRepository,
            fetchSeasonUseCase = mockFetchSeasonUseCase,
            navigator = mockNavigator,
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
    fun `current season use case is fetched on initial load`() = runTest {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertNotNull(awaitItem())
        }

        verify { mockFetchSeasonUseCase.fetch(2020) }
    }

    @Test
    fun `loading is returned when DB returns no standings and hasnt made request`() = runTest {
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(false) }
        every { mockSeasonRepository.getDriverStandings(any()) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertEquals(listOf(DriverStandingsModel.Loading), awaitItem())
        }
    }

    @Test
    fun `null is returned when DB returns no standings and has made request`() = runTest {
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(true) }
        every { mockSeasonRepository.getDriverStandings(any()) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertEquals(null, awaitItem())
        }
    }

    @Test
    fun `expected list is returned when items are loaded from the DB`() = runTest {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertEquals(listOf(
                DriverStandingsModel.Standings(
                    standings = SeasonDriverStandingSeason.model(points = 3.0, driver = Driver.model(id = "2"), championshipPosition = 1)
                ),
                DriverStandingsModel.Standings(
                    standings = SeasonDriverStandingSeason.model(points = 2.0, driver = Driver.model(id = "1"), championshipPosition = 2),
                )
            ), awaitItem())
        }
    }


    @Test
    fun `refresh calls fetch season and updates is refreshing`() = runTest {
        initUnderTest()
        underTest.load(2020)

        val observer = underTest.outputs.isRefreshing.testIn(this)

        underTest.refresh()
        advanceUntilIdle()

        val items = observer.cancelAndConsumeRemainingEvents()
        assertEquals(false, (items[0] as Event.Item<Boolean>).value) // Initialise
        assertEquals(true, (items[1] as Event.Item<Boolean>).value)
        assertEquals(false, (items[2] as Event.Item<Boolean>).value) // Refresh

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
            mockNavigator.navigate(
                Screen.Driver.with(
                driverId = model.standings.driver.id,
                driverName = model.standings.driver.name
            ))
        }
    }
}
