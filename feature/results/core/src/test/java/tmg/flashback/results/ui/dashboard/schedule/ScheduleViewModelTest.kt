package tmg.flashback.results.ui.dashboard.schedule

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.EventsRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.results.repository.HomeRepository
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.NotificationSchedule
import tmg.flashback.results.usecases.FetchSeasonUseCase
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.model.from
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.WeekendInfo
import tmg.flashback.weekend.contract.with
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class ScheduleViewModelTest: BaseTest() {

    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockEventsRepository: EventsRepository = mockk(relaxed = true)
    private val mockFetchSeasonUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)
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
    fun `current season use case is fetched on initial load`() {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.testObserve()

        verify { mockFetchSeasonUseCase.fetch(2020) }
    }

    @Test
    fun `null is returned when DB returns no standings and hasnt made request`() {
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(false) }
        every { mockOverviewRepository.getOverview(any()) } returns flow { emit(Overview.model(overviewRaces = emptyList())) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertValue(listOf(ScheduleModel.Loading))
        }
    }

    @Test
    fun `expected list is returned when items are loaded from the DB`() {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertValue(listOf(
                ScheduleModel.List(
                    model = OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1)),
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                ),
                ScheduleModel.List(
                    model = OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 3)),
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                )
            ))
        }
    }


    @Test
    fun `show events returns true when events are contained`() {
        every { mockEventsRepository.getEvents(2020) } returns flow { emit(listOf(
            tmg.flashback.formula1.model.Event.model(date = LocalDate.of(2020, 1, 2)),
            tmg.flashback.formula1.model.Event.model(date = LocalDate.now().plusDays(1))
        )) }

        initUnderTest()
        underTest.load(2020)

        val loadInfo = underTest.outputs.items.testObserve()
        underTest.outputs.showEvents.test {
            assertValue(true)
        }
    }


    @Test
    fun `expected list shows upcoming events intertwined with calendar models`() {
        every { mockEventsRepository.getEvents(2020) } returns flow { emit(listOf(
            tmg.flashback.formula1.model.Event.model(date = LocalDate.of(2020, 1, 2)),
            tmg.flashback.formula1.model.Event.model(date = LocalDate.now().plusDays(1))
        )) }

        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertValue(listOf(
                ScheduleModel.List(
                    model = OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1)),
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                ),
                ScheduleModel.List(
                    model = OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 3)),
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                ),
                ScheduleModel.Event(
                    Event.model(date = LocalDate.now().plusDays(1))
                )
            ))
        }
    }

    @Test
    fun `expected list shows collapsible list section if pref is enabled`() {

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
            assertValue(listOf(
                ScheduleModel.CollapsableList(
                    first = dayBeforeDayBeforeYesterday,
                    last = dayBeforeYesterday
                ),
                ScheduleModel.List(
                    model = yesterday,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                ),
                ScheduleModel.List(
                    model = today,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = true
                ),
                ScheduleModel.List(
                    model = tomorrow,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                )
            ))
        }
    }

    @Test
    fun `expected list doesnt show collapsible list section if no previous`() {

        val today = OverviewRace.model(round = 3, date = LocalDate.now())
        val tomorrow = OverviewRace.model(round = 4, date = LocalDate.now().plusDays(1L))

        every { mockHomeRepository.collapseList } returns true
        every { mockOverviewRepository.getOverview(any()) } returns flow { emit(Overview.model(
            overviewRaces = listOf(today, tomorrow)
        )) }

        initUnderTest()
        underTest.load(LocalDate.now().year)

        underTest.outputs.items.test {
            assertValue(listOf(
                ScheduleModel.List(
                    model = today,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = true
                ),
                ScheduleModel.List(
                    model = tomorrow,
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                )
            ))
        }
    }

    @Test
    fun `refresh calls fetch season and updates is refreshing`() = coroutineTest {
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
    fun `clicking item goes to constructor overview`() {
        initUnderTest()
        underTest.load(2020)
        val model = ScheduleModel.List(
            model = OverviewRace.model(round = 1),
            notificationSchedule = fakeNotificationSchedule,
            showScheduleList = false
        )

        underTest.clickItem(model)

        verify {
            mockNavigator.navigate(
                Screen.Weekend.with(
                WeekendInfo.from(model.model.toRaceInfo())
            ))
        }
    }

    @Test
    fun `clicking tyre with season launches tyre sheet`() {
        initUnderTest()
        underTest.load(2020)

        underTest.inputs.clickTyre(2020)

        verify {
            mockResultsNavigationComponent.tyres(2020)
        }
    }

    @Test
    fun `clicking preseason with season launches preseason sheet`() {
        initUnderTest()
        underTest.load(2020)

        underTest.inputs.clickPreseason(2020)

        verify {
            mockResultsNavigationComponent.preseasonEvents(2020)
        }
    }

    private val fakeNotificationSchedule: NotificationSchedule = NotificationSchedule(
        freePractice = true,
        qualifying = true,
        sprint = true,
        race = true,
        other = true,
    )
}