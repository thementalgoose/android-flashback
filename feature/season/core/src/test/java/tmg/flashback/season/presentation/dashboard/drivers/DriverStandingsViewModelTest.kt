package tmg.flashback.season.presentation.dashboard.drivers

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import tmg.testutils.BaseTest

internal class DriverStandingsViewModelTest: BaseTest() {

    private val mockSeasonRepository: SeasonRepository = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockFetchSeasonsUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)

    private lateinit var underTest: DriverStandingsViewModel

    private fun initUnderTest() {
        underTest = DriverStandingsViewModel(
            seasonRepository = mockSeasonRepository,
            fetchSeasonUseCase = mockFetchSeasonsUseCase,
            defaultSeasonUseCase = mockDefaultSeasonUseCase,
            navigator = mockNavigator,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    private val standing1 = SeasonDriverStandingSeason.model()
    private val standing2 = SeasonDriverStandingSeason.model(driver = Driver.model(id = "2"))

    @BeforeEach
    fun setUp() {
        every { mockSeasonRepository.getDriverStandings(2020) } returns flow { emit(SeasonDriverStandings.model(
            standings = listOf(standing1)
        )) }
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2020
    }

    @Test
    fun `initial load sets default season`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(2020, awaitItem().season)
        }
    }

    @Test
    fun `refresh calls fetch`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(listOf(standing1), awaitItem().standings)

            every { mockSeasonRepository.getDriverStandings(2020) } returns flow { emit(SeasonDriverStandings.model(
                standings = listOf(standing1, standing2)
            )) }

            underTest.refresh()
            coVerify {
                mockFetchSeasonsUseCase.fetchSeason(2020)
            }
            assertEquals(listOf(standing1, standing2), awaitItem().standings)
        }
    }

    @Test
    fun `initial load populates data from repo`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(listOf(standing1), awaitItem().standings)
        }
    }

    @Test
    fun `selecting item updates state`() = runTest {
        initUnderTest()
        underTest.inputs.selectDriver(standing1)
        underTest.outputs.uiState.test {
            assertEquals(standing1, awaitItem().currentlySelected)
        }
    }

    @Test
    fun `closing item updates state`() = runTest {
        initUnderTest()
        underTest.inputs.selectDriver(standing1)
        underTest.outputs.uiState.test {
            assertEquals(standing1, awaitItem().currentlySelected)

            underTest.inputs.closeDriverDetails()

            assertEquals(null, awaitItem().currentlySelected)
        }
    }
}