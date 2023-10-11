package tmg.flashback.results.ui.dashboard.schedule

import app.cash.turbine.Event.Item
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
import org.threeten.bp.LocalDate
import tmg.flashback.domain.repo.EventsRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.results.model.toScreenWeekendData
import tmg.flashback.results.repository.HomeRepository
import tmg.flashback.results.contract.repository.models.NotificationSchedule
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendNav
import tmg.flashback.weekend.contract.with
import tmg.testutils.BaseTest

internal class ScheduleViewModelTest: BaseTest() {

    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockEventsRepository: EventsRepository = mockk(relaxed = true)
    private val mockFetchSeasonUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationsRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockResultsNavigationComponent: ResultsNavigationComponent = mockk(relaxed = true)
    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var underTest: ScheduleViewModel

    private fun initUnderTest() {
        underTest = ScheduleViewModel(
            fetchSeasonUseCase = mockFetchSeasonUseCase,
            overviewRepository = mockOverviewRepository,
            notificationRepository = mockNotificationRepository,
            homeRepository = mockHomeRepository,
            eventsRepository = mockEventsRepository,
            navigator = mockNavigator,
            resultsNavigationComponent = mockResultsNavigationComponent,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockOverviewRepository.getOverview(2020) } returns flow { emit(
            Overview.model(
                overviewRaces = listOf(
                    OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1)),
                    OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 3))
                )
            ))
        }
        every { mockHomeRepository.collapseList } returns false
        every { mockEventsRepository.getEvents(any()) } returns flow { emit(emptyList()) }
        every { mockNotificationRepository.notificationSchedule } returns fakeNotificationSchedule
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(true) }
    }

    @Test
    fun `current season use case is fetched on initial load`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertNotNull(awaitItem())
        }

        verify { mockFetchSeasonUseCase.fetch(2020) }
    }

    @Test
    fun `null is returned when DB returns no standings and hasnt made request`() = runTest(testDispatcher) {
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(false) }
        every { mockOverviewRepository.getOverview(any()) } returns flow { emit(Overview.model(overviewRaces = emptyList())) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertEquals(listOf(ScheduleModel.Loading), awaitItem())
        }
    }

    @Test
    fun `expected list is returned when items are loaded from the DB`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertEquals(listOf(
                ScheduleModel.RaceWeek(
                    model = OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1)),
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                ),
                ScheduleModel.RaceWeek(
                    model = OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 3)),
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                )
            ), awaitItem())
        }
    }


    @Test
    fun `show events returns true when events are contained`() = runTest(testDispatcher) {
        every { mockEventsRepository.getEvents(2020) } returns flow { emit(listOf(
            Event.model(date = LocalDate.of(2020, 1, 2)),
            Event.model(date = LocalDate.now().plusDays(1))
        )) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertNotNull(awaitItem())
        }
    }


    @Test
    fun `expected list shows upcoming events intertwined with calendar models`() = runTest(testDispatcher) {
        every { mockEventsRepository.getEvents(2020) } returns flow { emit(listOf(
            Event.model(date = LocalDate.of(2020, 1, 2)),
            Event.model(date = LocalDate.now().plusDays(1))
        )) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertEquals(listOf(
                ScheduleModel.RaceWeek(
                    model = OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1)),
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                ),
                ScheduleModel.RaceWeek(
                    model = OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 3)),
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                )
            ), awaitItem())
        }
    }

    @Test
    fun `expected list shows collapsible list section if pref is enabled`() = runTest(testDispatcher) {

        val dayBeforeDayBeforeYesterday = OverviewRace.model(round = 1, date = LocalDate.now().minusDays(3L))
        val dayBeforeYesterday = OverviewRace.model(round = 2, date = LocalDate.now().minusDays(2L))
        val yesterday = OverviewRace.model(round = 3, date = LocalDate.now().minusDays(1L))
        val today = OverviewRace.model(round = 4, date = LocalDate.now())
        val tomorrow = OverviewRace.model(round = 5, date = LocalDate.now().plusDays(1L))

        every { mockHomeRepository.collapseList } returns true
        every { mockOverviewRepository.getOverview(any()) } returns flow { emit(Overview.model(
            overviewRaces = listOf(dayBeforeDayBeforeYesterday, dayBeforeYesterday, yesterday, today, tomorrow)
        )) }

        initUnderTest()
        underTest.load(LocalDate.now().year)

        underTest.outputs.items.test {
            assertEquals(listOf(
                ScheduleModel.GroupedCompletedRaces(
                    first = dayBeforeDayBeforeYesterday,
                    last = dayBeforeYesterday
                ),
                ScheduleModel.RaceWeek(
                    model = yesterday,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                ),
                ScheduleModel.RaceWeek(
                    model = today,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = true
                ),
                ScheduleModel.RaceWeek(
                    model = tomorrow,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                )
            ), awaitItem())
        }
    }

    @Test
    fun `expected list doesnt show collapsible list section if no previous`() = runTest(testDispatcher) {

        val today = OverviewRace.model(round = 3, date = LocalDate.now())
        val tomorrow = OverviewRace.model(round = 4, date = LocalDate.now().plusDays(1L))

        every { mockHomeRepository.collapseList } returns true
        every { mockOverviewRepository.getOverview(any()) } returns flow { emit(Overview.model(
            overviewRaces = listOf(today, tomorrow)
        )) }

        initUnderTest()
        underTest.load(LocalDate.now().year)

        underTest.outputs.items.test {
            assertEquals(listOf(
                ScheduleModel.RaceWeek(
                    model = today,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = true
                ),
                ScheduleModel.RaceWeek(
                    model = tomorrow,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                )
            ), awaitItem())
        }
    }

    @Test
    fun `refresh calls fetch season and updates is refreshing`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.load(2020)

        val observer = underTest.outputs.isRefreshing.testIn(this)

        underTest.refresh()

        advanceUntilIdle()
        val items = observer.cancelAndConsumeRemainingEvents()
        assertEquals(false, (items[0] as Item<Boolean>).value) // Initialise
        assertEquals(true, (items[1] as Item<Boolean>).value)
        assertEquals(false, (items[2] as Item<Boolean>).value) // Refresh
        coVerify {
            mockFetchSeasonUseCase.fetchSeason(2020)
        }
    }


    @Test
    fun `clicking item goes to weekend overview with tab RACE`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.load(2020)
        val model = ScheduleModel.RaceWeek(
            model = OverviewRace.model(round = 1),
            notificationSchedule = fakeNotificationSchedule,
            showScheduleList = false
        )

        underTest.clickItem(model)

        verify {
            mockNavigator.navigate(
                Screen.Weekend.with(
                    weekendInfo = model.model.toRaceInfo().toScreenWeekendData(),
                    tab = ScreenWeekendNav.RACE
                )
            )
        }
    }

    @Test
    fun `clicking tyre with season launches tyre sheet`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.load(2020)

        underTest.inputs.clickTyre(2020)

        verify {
            mockResultsNavigationComponent.tyres(2020)
        }
    }

    @Test
    fun `clicking preseason with season launches preseason sheet`() = runTest(testDispatcher) {
        initUnderTest()
        underTest.load(2020)
        underTest.clickItem(ScheduleModel.AllEvents)

        verify {
            mockResultsNavigationComponent.preseasonEvents(2020)
        }
    }

    private val fakeNotificationSchedule: NotificationSchedule = NotificationSchedule(
        freePractice = true,
        qualifying = true,
        sprint = true,
        sprintQualifying = true,
        race = true,
        other = true,
    )
}