package tmg.flashback.season.presentation.dashboard.races

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.ads.ads.repository.model.AdvertConfig
import tmg.flashback.data.repo.EventsRepository
import tmg.flashback.data.repo.OverviewRepository
import tmg.flashback.data.repo.usecases.FetchSeasonUseCase
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.navigation.Navigator
import tmg.flashback.reviews.usecases.AppSection
import tmg.flashback.reviews.usecases.ReviewSectionSeenUseCase
import tmg.flashback.season.contract.repository.NotificationsRepository
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.CurrentSeasonHolder
import tmg.flashback.season.repository.HomeRepository
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.testutils.BaseTest

internal class RacesViewModelTest: BaseTest() {

    private val mockOverviewRepository: OverviewRepository = mockk(relaxed = true)
    private val mockEventsRepository: EventsRepository = mockk(relaxed = true)
    private val mockFetchSeasonUseCase: FetchSeasonUseCase = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationsRepository = mockk(relaxed = true)
    private val mockCurrentSeasonHolder: CurrentSeasonHolder = mockk(relaxed = true)
    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockAdvertRepository: AdsRepository = mockk(relaxed = true)
    private val mockReviewSectionSeenUseCase: ReviewSectionSeenUseCase = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    private lateinit var underTest: RacesViewModel

    private fun initUnderTest() {
        underTest = RacesViewModel(
            fetchSeasonUseCase = mockFetchSeasonUseCase,
            overviewRepository = mockOverviewRepository,
            currentSeasonHolder = mockCurrentSeasonHolder,
            notificationRepository = mockNotificationRepository,
            homeRepository = mockHomeRepository,
            eventsRepository = mockEventsRepository,
            navigator = mockNavigator,
            networkConnectivityManager = mockNetworkConnectivityManager,
            adsRepository = mockAdvertRepository,
            reviewSectionSeenUseCase = mockReviewSectionSeenUseCase,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    private val currentSeasonFlow: MutableStateFlow<Int> = MutableStateFlow(2020)

    private val overview1 = OverviewRace.model(round = 1, date = LocalDate.of(2020, 1, 1))
    private val overview2 = OverviewRace.model(round = 2, date = LocalDate.of(2020, 1, 3))
    private val overview3 = OverviewRace.model(round = 3, date = LocalDate.of(2020, 1, 6))
    private val overview4 = OverviewRace.model(round = 4, date = LocalDate.of(2020, 1, 9))
    private val overview5 = OverviewRace.model(round = 1, date = LocalDate.of(2021, 1, 9))
    private val expectedRaceWeek1 = RacesModel.RaceWeek(model = overview1, notificationSchedule = fakeNotificationSchedule)
    private val expectedRaceWeek2 = RacesModel.RaceWeek(model = overview2, notificationSchedule = fakeNotificationSchedule)
    private val expectedRaceWeek3 = RacesModel.RaceWeek(model = overview3, notificationSchedule = fakeNotificationSchedule)
    private val expectedRaceWeek4 = RacesModel.RaceWeek(model = overview4, notificationSchedule = fakeNotificationSchedule)
    private val expectedRaceWeek5 = RacesModel.RaceWeek(model = overview5, notificationSchedule = fakeNotificationSchedule)

    @BeforeEach
    internal fun setUp() {
        every { mockOverviewRepository.getOverview(2020) } returns flow { emit(
            Overview.model(
                overviewRaces = listOf(overview1, overview2)
            ))
        }
        every { mockOverviewRepository.getOverview(2021) } returns flow { emit(
            Overview.model(
                overviewRaces = listOf(overview5)
            ))
        }
        every { mockAdvertRepository.advertConfig } returns AdvertConfig(onHomeScreen = false)
        every { mockHomeRepository.collapseList } returns false
        every { mockEventsRepository.getEvents(any()) } returns flow { emit(emptyList()) }
        every { mockNotificationRepository.notificationSchedule } returns fakeNotificationSchedule
        every { mockFetchSeasonUseCase.fetch(any()) } returns flow { emit(true) }
        every { mockCurrentSeasonHolder.currentSeason } returns 2020
        every { mockCurrentSeasonHolder.currentSeasonFlow } returns currentSeasonFlow
    }

    @Test
    fun `content refreshed when current season flow changes`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(listOf(expectedRaceWeek1, expectedRaceWeek2), awaitItem().items)

            currentSeasonFlow.emit(2021)
            val item = awaitItem()
            assertEquals(listOf(expectedRaceWeek5), item.items)
            assertEquals(2021, item.season)
        }
    }

    @Test
    fun `initial load marks review section seen`() = runTest {
        initUnderTest()
        mockReviewSectionSeenUseCase.invoke(AppSection.HOME_CALENDAR)
    }

    @Test
    fun `initial load sets default season`() = runTest {
        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(2020, awaitItem().season)
        }
    }

    @Test
    fun `initial load calls refresh if in current year`() = runTest {
        every { mockCurrentSeasonHolder.defaultSeason } returns 2020
        initUnderTest()
        coVerify {
            mockFetchSeasonUseCase.fetchSeason(2020)
        }
    }

    @Test
    fun `initial load does not calls refresh if not in current year`() = runTest {
        every { mockCurrentSeasonHolder.defaultSeason } returns 2021
        initUnderTest()
        coVerify(exactly = 0) {
            mockFetchSeasonUseCase.fetchSeason(any())
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
            assertEquals(ScreenWeekendData(overview1), awaitItem().currentRace)

            underTest.back()
            assertEquals(null, awaitItem().currentRace)
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
    fun `injecting deeplink updates state`() = runTest {
        val screenWeekendData = mockk<ScreenWeekendData>(relaxed = true)
        initUnderTest()
        underTest.inputs.deeplinkToo(screenWeekendData)

        underTest.outputs.uiState.test {
            assertEquals(screenWeekendData, awaitItem().currentRace)

            underTest.inputs.back()
            assertEquals(null, awaitItem().currentRace)

            underTest.inputs.deeplinkToo(screenWeekendData)
            this.ensureAllEventsConsumed()
        }
    }
}