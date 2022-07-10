package tmg.flashback.stats.ui.dashboard.calendar

import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationSchedule
import tmg.flashback.stats.usecases.FetchSeasonUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class CalendarViewModelTest: BaseTest() {

    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockFetchSeasonUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: CalendarViewModel

    private fun initUnderTest() {
        underTest = CalendarViewModel(
            fetchSeasonUseCase = mockFetchSeasonUseCase,
            overviewRepository = mockOverviewRepository,
            notificationRepository = mockNotificationRepository,
            statsNavigationComponent = mockStatsNavigationComponent,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockOverviewRepository.getOverview(2020) } returns flow { emit(
            Overview.model(
                overviewRaces = listOf(
                    OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1)),
                    OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 2))
                )
            ))
        }
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
            assertValue(listOf(CalendarModel.Loading))
        }
    }

    @Test
    fun `expected list is returned when items are loaded from the DB`() {
        initUnderTest()
        underTest.load(2020)

        underTest.outputs.items.test {
            assertValue(listOf(
                CalendarModel.List(
                    model = OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1)),
                    notificationSchedule = fakeNotificationSchedule,
                    showScheduleList = false
                ),
                CalendarModel.List(
                    model = OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 2)),
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
        val model = CalendarModel.List(
            model = OverviewRace.model(round = 1),
            notificationSchedule = fakeNotificationSchedule,
            showScheduleList = false
        )

        underTest.clickItem(model)

        verify {
            mockStatsNavigationComponent.weekend(any())
        }
    }

    @Test
    fun `clicking tyre with season launches tyre sheet`() {
        initUnderTest()
        underTest.load(2020)

        underTest.inputs.clickTyre(2020)

        verify {
            mockStatsNavigationComponent.tyres(2020)
        }
    }

    private val fakeNotificationSchedule: NotificationSchedule = NotificationSchedule(
        freePractice = true,
        qualifying = true,
        race = true,
        other = true,
    )
}