package tmg.flashback.season.presentation.dashboard.races

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.domain.repo.EventsRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.season.contract.ResultsNavigationComponent
import tmg.flashback.season.contract.repository.NotificationsRepository
import tmg.flashback.season.repository.HomeRepository
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import tmg.testutils.BaseTest

internal class RacesViewModelTest: BaseTest() {

    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockEventsRepository: EventsRepository = mockk(relaxed = true)
    private val mockFetchSeasonUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationsRepository = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockResultsNavigationComponent: ResultsNavigationComponent = mockk(relaxed = true)
    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var underTest: RacesViewModel

    private fun initUnderTest() {
        underTest = RacesViewModel(
            fetchSeasonUseCase = mockFetchSeasonUseCase,
            overviewRepository = mockOverviewRepository,
            defaultSeasonUseCase = mockDefaultSeasonUseCase,
            notificationRepository = mockNotificationRepository,
            homeRepository = mockHomeRepository,
            resultsNavigationComponent = mockResultsNavigationComponent,
            eventsRepository = mockEventsRepository,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    private val overview1 = OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1))
    private val overview2 = OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 3))
    private val overview3 = OverviewRace.model(round = 3, date = LocalDate.of(2020, 1, 6))
    private val overview4 = OverviewRace.model(round = 4, date = LocalDate.of(2020, 1, 9))
    private val expectedRaceWeek1 = RacesModel.RaceWeek(model = overview1, notificationSchedule = fakeNotificationSchedule)
    private val expectedRaceWeek2 = RacesModel.RaceWeek(model = overview2, notificationSchedule = fakeNotificationSchedule)
    private val expectedRaceWeek3 = RacesModel.RaceWeek(model = overview3, notificationSchedule = fakeNotificationSchedule)

    @BeforeEach
    internal fun setUp() {
        every { mockOverviewRepository.getOverview(2020) } returns flow { emit(
            Overview.model(
                overviewRaces = listOf(overview1, overview2)
            ))
        }
        every { mockHomeRepository.collapseList } returns false
        every { mockEventsRepository.getEvents(any()) } returns flow { emit(emptyList()) }
        every { mockNotificationRepository.notificationSchedule } returns fakeNotificationSchedule
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(true) }
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
    fun `initial load calls refresh`() = runTest {
        initUnderTest()
        coVerify {
            mockFetchSeasonUseCase.fetchSeason(2020)
        }
    }

    @Test
    fun `initial load sets scheduled list of items`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(listOf(expectedRaceWeek1, expectedRaceWeek2), awaitItem().items)
        }
    }

    @Test
    fun `initial load with empty overview sets items to null`() = runTest {
        every { mockOverviewRepository.getOverview(2020) } returns flow { emit(
            Overview(
                season = 2020,
                overviewRaces = emptyList()
            ))
        }
        initUnderTest()
        underTest.outputs.uiState.test {
            val item = awaitItem()
            assertEquals(null, item.items)
            assertEquals(false, item.isLoading)
            assertEquals(null, item.currentRace)
        }
    }

    @Test
    fun `refresh calls refresh`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(listOf(expectedRaceWeek1, expectedRaceWeek2), awaitItem().items)

            every { mockOverviewRepository.getOverview(2020) } returns flow { emit(
                Overview.model(
                    overviewRaces = listOf(overview1, overview2, overview3)
                ))
            }
            underTest.inputs.refresh()
            coVerify {
                mockFetchSeasonUseCase.fetchSeason(2020)
            }
            assertEquals(listOf(expectedRaceWeek1, expectedRaceWeek2, expectedRaceWeek3), awaitItem().items)
        }
    }

    @Test
    fun `clicking race item updates state to current race`() = runTest {
        initUnderTest()

        underTest.clickItem(expectedRaceWeek1)
        underTest.outputs.uiState.test {
            assertEquals(overview1, awaitItem().currentRace)
        }
    }

    @Test
    fun `clicking tyre opens navigation component`() = runTest {
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2023

        initUnderTest()
        underTest.clickTyre()

        verify {
            mockResultsNavigationComponent.tyres(2023)
        }
    }

    @Test
    fun `clicking grouped races uncollapses races`() = runTest {
        every { mockOverviewRepository.getOverview(2020) } returns flow { emit(
            Overview.model(
                overviewRaces = listOf(
                    OverviewRace.model(round = 1, date = LocalDate.now().minusDays(5L)),
                    OverviewRace.model(round = 2, date = LocalDate.now().minusDays(2L)),
                    OverviewRace.model(round = 3, date = LocalDate.now().minusDays(1L)),
                    OverviewRace.model(round = 4, date = LocalDate.now().plusDays(5L)),
                )
            ))
        }
        every { mockHomeRepository.collapseList } returns true
        initUnderTest()
        underTest.outputs.uiState.test {
            assertTrue(awaitItem().items?.any { it is RacesModel.GroupedCompletedRaces } == true)

            val groupedRaces: RacesModel.GroupedCompletedRaces = mockk(relaxed = true)
            underTest.inputs.clickItem(groupedRaces)

            testScheduler.advanceUntilIdle()

            assertTrue(awaitItem().items?.none { it is RacesModel.GroupedCompletedRaces } == true)
        }
    }

    @Test
    fun `clicking all events loads preseason events`() = runTest {
        initUnderTest()
        underTest.inputs.clickItem(RacesModel.AllEvents)
        verify {
            mockResultsNavigationComponent.preseasonEvents(2020)
        }
    }
}