package tmg.flashback.season.presentation.dashboard.constructors

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.formula1.model.SeasonConstructorStandings
import tmg.flashback.formula1.model.model
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import tmg.testutils.BaseTest

internal class ConstructorStandingsViewModelTest: BaseTest() {

    private val mockSeasonRepository: SeasonRepository = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockFetchSeasonsUseCase: FetchSeasonUseCase = mockk(relaxed = true)

    private lateinit var underTest: ConstructorStandingsViewModel

    private fun initUnderTest() {
        underTest = ConstructorStandingsViewModel(
            seasonRepository = mockSeasonRepository,
            fetchSeasonUseCase = mockFetchSeasonsUseCase,
            defaultSeasonUseCase = mockDefaultSeasonUseCase,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    private val standing1 = SeasonConstructorStandingSeason.model()
    private val standing2 = SeasonConstructorStandingSeason.model(constructor = Constructor.model(id = "2"))

    @BeforeEach
    fun setUp() {
        every { mockSeasonRepository.getConstructorStandings(2020) } returns flow { emit(
            SeasonConstructorStandings.model(
                standings = listOf(standing1)
            )
        ) }
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

            every { mockSeasonRepository.getConstructorStandings(2020) } returns flow { emit(
                SeasonConstructorStandings.model(
                    standings = listOf(standing1, standing2)
                )
            ) }

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
        underTest.inputs.selectConstructor(standing1)
        underTest.outputs.uiState.test {
            assertEquals(standing1, awaitItem().currentlySelected)
        }
    }

    @Test
    fun `closing item updates state`() = runTest {
        initUnderTest()
        underTest.inputs.selectConstructor(standing1)
        underTest.outputs.uiState.test {
            assertEquals(standing1, awaitItem().currentlySelected)

            underTest.inputs.closeConstructor()

            assertEquals(null, awaitItem().currentlySelected)
        }
    }
}