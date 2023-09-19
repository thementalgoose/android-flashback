package tmg.flashback.results.ui.dashboard.calendar

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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.domain.repo.EventsRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.model.toScreenWeekendData
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.contract.repository.models.NotificationSchedule
import tmg.flashback.results.usecases.FetchSeasonUseCase
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendNav
import tmg.flashback.weekend.contract.with
import tmg.testutils.BaseTest

internal class CalendarViewModelTest: BaseTest() {

    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockEventsRepository: EventsRepository = mockk(relaxed = true)
    private val mockFetchSeasonUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockResultsNavigationComponent: ResultsNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: CalendarViewModel

    private val race1 = OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1))
    private val race2 = OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 20))

    private fun initUnderTest() {
        underTest = CalendarViewModel(
            fetchSeasonUseCase = mockFetchSeasonUseCase,
            overviewRepository = mockOverviewRepository,
            eventsRepository = mockEventsRepository,
            navigator = mockNavigator,
            resultsNavigationComponent = mockResultsNavigationComponent,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockOverviewRepository.getOverview(2020) } returns flow { emit(
            Overview.model(overviewRaces = listOf(race1, race2)))
        }
        every { mockEventsRepository.getEvents(any()) } returns flow { emit(emptyList()) }
        every { mockNotificationRepository.notificationSchedule } returns fakeNotificationSchedule
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
    fun `null is returned when DB returns no standings and hasnt made request`() = runTest {
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(false) }
        every { mockOverviewRepository.getOverview(any()) } returns flow { emit(Overview.model(overviewRaces = emptyList())) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertEquals(listOf(CalendarModel.Loading), awaitItem())
        }
    }

    @Test
    fun `expected list is returned when items are loaded from the DB`() = runTest {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            val item = awaitItem()
            assertTrue(item.any { it is CalendarModel.Week && it.race == race1 && it.startOfWeek == LocalDate.of(2019, 12, 30) })
            assertTrue(item.any { it is CalendarModel.Week && it.race == race2 && it.startOfWeek == LocalDate.of(2020, 1, 20) })
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
    fun `clicking item goes to constructor overview`() = runTest {
        initUnderTest()
        underTest.load(2020)
        val model = CalendarModel.Week(
            race = OverviewRace.model(round = 1),
            season = 2020,
            startOfWeek = LocalDate.of(2019, 12, 30)
        )

        underTest.clickItem(model)

        verify {
            mockNavigator.navigate(
                Screen.Weekend.with(
                    OverviewRace.model(round = 1).toRaceInfo().toScreenWeekendData(), ScreenWeekendNav.RACE
            ))
        }
    }

    @Test
    fun `clicking tyre with season launches tyre sheet`() = runTest {
        initUnderTest()
        underTest.load(2020)

        underTest.inputs.clickTyre(2020)

        verify {
            mockResultsNavigationComponent.tyres(2020)
        }
    }

    private val fakeNotificationSchedule: NotificationSchedule = NotificationSchedule(
        freePractice = true,
        qualifying = true,
        sprintQualifying = true,
        sprint = true,
        race = true,
        other = true,
    )
}