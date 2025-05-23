package tmg.flashback.season.presentation.dashboard.constructors

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.data.repo.SeasonRepository
import tmg.flashback.data.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.formula1.model.SeasonConstructorStandings
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.CurrentSeasonHolder
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import tmg.testutils.BaseTest

internal class ConstructorStandingsViewModelTest: BaseTest() {

    private val mockSeasonRepository: SeasonRepository = mockk(relaxed = true)
    private val mockCurrentSeasonHolder: CurrentSeasonHolder = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockFetchSeasonsUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    private lateinit var underTest: ConstructorStandingsViewModel

    private fun initUnderTest() {
        underTest = ConstructorStandingsViewModel(
            seasonRepository = mockSeasonRepository,
            fetchSeasonUseCase = mockFetchSeasonsUseCase,
            currentSeasonHolder = mockCurrentSeasonHolder,
            navigator = mockNavigator,
            networkConnectivityManager = mockNetworkConnectivityManager,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    private val standing1 = SeasonConstructorStandingSeason.model()
    private val standing2 = SeasonConstructorStandingSeason.model(constructor = Constructor.model(id = "2"))
    private val mockCurrentSeasonFlow: MutableStateFlow<Int> = MutableStateFlow(2020)

    @BeforeEach
    fun setUp() {
        every { mockSeasonRepository.getConstructorStandings(2020) } returns flow { emit(
            SeasonConstructorStandings.model(standings = listOf(standing1))
        ) }
        every { mockSeasonRepository.getConstructorStandings(2021) } returns flow { emit(
            SeasonConstructorStandings.model(standings = listOf(standing2))
        ) }
        every { mockCurrentSeasonHolder.currentSeason } returns 2020
        every { mockCurrentSeasonHolder.currentSeasonFlow } returns mockCurrentSeasonFlow
    }

    @Test
    fun `current season holder emits new season calls populate and refresh`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(2020, awaitItem().season)

            mockCurrentSeasonFlow.emit(2021)

            val item = awaitItem()
            assertEquals(2021, item.season)
            assertEquals(listOf(standing2), item.standings)
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
        coVerify(exactly = 0) {
            mockFetchSeasonsUseCase.fetchSeason(any())
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