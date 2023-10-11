package tmg.flashback.season.presentation.dashboard.drivers

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.formula1.model.model
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import tmg.testutils.BaseTest

internal class DriverStandingsViewModelTest: BaseTest() {

    private val mockSeasonRepository: SeasonRepository = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockFetchSeasonsUseCase: FetchSeasonUseCase = mockk(relaxed = true)

    private lateinit var underTest: DriverStandingsViewModel

    private fun initUnderTest() {
        underTest = DriverStandingsViewModel(
            seasonRepository = mockSeasonRepository,
            fetchSeasonUseCase = mockFetchSeasonsUseCase,
            defaultSeasonUseCase = mockDefaultSeasonUseCase,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2020
        every { mockSeasonRepository.getDriverStandings(2020) } returns flow {
            emit(SeasonDriverStandings.model())
        }
    }

    @Test
    fun `initial load sets default season`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(2020, awaitItem().season)
        }
    }

    @Test
    fun `initial load calls refresh`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(2020, awaitItem().season)
        }
    }

    @Test
    fun `initial load populates data from repo`() = runTest {

    }

    @Test
    fun `selecting item updates state`() = runTest {

    }

    @Test
    fun `closing item updates state`() = runTest {

    }
}