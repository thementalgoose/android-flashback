package tmg.flashback.statistics.ui.dashboard.season

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.temporal.TemporalAdjusters
import tmg.core.analytics.manager.AnalyticsManager
import tmg.core.device.controllers.DeviceController
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.core.ui.controllers.ThemeController
import tmg.core.ui.model.AnimationSpeed
import tmg.flashback.data.db.stats.HistoryRepository
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.flashback.data.models.stats.History
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.*
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.repository.models.Banner
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.assertListContainsItem
import tmg.testutils.livedata.assertListDoesNotMatchItem
import tmg.testutils.livedata.assertListExcludesItem
import tmg.testutils.livedata.assertListHasFirstItem
import tmg.testutils.livedata.assertListHasLastItem
import tmg.testutils.livedata.assertListHasSublist
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test
import tmg.utilities.models.StringHolder

internal class SeasonViewModelTest: BaseTest() {

    lateinit var sut: SeasonViewModel

    private val mockThemeController: ThemeController = mockk(relaxed = true)
    private val mockHistoryRepository: HistoryRepository = mockk(relaxed = true)
    private val mockSeasonOverviewRepository: SeasonOverviewRepository = mockk(relaxed = true)
    private val mockSeasonController: SeasonController = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockAnalyticsController: AnalyticsManager = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        every { mockSeasonController.banner } returns null

        every { mockNetworkConnectivityManager.isConnected } returns true

        every { mockThemeController.animationSpeed } returns AnimationSpeed.NONE
        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason) }
        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(mockHistory) }
    }

    private fun initSUT() {
        sut = SeasonViewModel(
            mockThemeController,
            mockHistoryRepository,
            mockSeasonOverviewRepository,
            mockSeasonController,
            mockNetworkConnectivityManager,
            mockAnalyticsController
        )
    }

    //region Showing up next

    @Test
    fun `showUpNext defaults to false`() {

        initSUT()

        sut.outputs.showUpNext.test {
            assertValue(false)
        }
    }

    @Test
    fun `showUpNext changes to true when input with true`() {

        initSUT()

        sut.inputs.showUpNext()

        sut.outputs.showUpNext.test {
            assertValue(true)
        }
    }

    //endregion

    //region Navigation

    @Test
    fun `clickMenu fires open menu event`() {

        initSUT()

        sut.inputs.clickMenu()

        sut.outputs.openMenu.test {
            assertEventFired()
        }
    }

    @Test
    fun `clickNow fires open now event`() {

        initSUT()

        sut.inputs.clickNow()

        sut.outputs.openNow.test {
            assertEventFired()
        }
    }

    @Test
    fun `clickTrack fires show race event`() {

        val mock: SeasonItem.Track = mockk(relaxed = true)
        initSUT()

        sut.inputs.clickTrack(mock)

        sut.outputs.openRace.test {
            assertDataEventValue(mock)
        }
    }

    @Test
    fun `clickDriver fires show driver event`() {

        val mock: SeasonItem.Driver = mockk(relaxed = true)
        initSUT()

        sut.inputs.clickDriver(mock)

        sut.outputs.openDriver.test {
            assertDataEventValue(mock)
        }
    }

    @Test
    fun `clickConstructor fires show constructor event`() {

        val mock: SeasonItem.Constructor = mockk(relaxed = true)
        initSUT()

        sut.inputs.clickConstructor(mock)

        sut.outputs.openConstructor.test {
            assertDataEventValue(mock)
        }
    }

    //endregion

    //region Defaults

    @Test
    fun `defaults to schedule type by default`() = coroutineTest {

        initSUT()

        // Track items should be calendar items
        sut.outputs.list.test {
            assertListMatchesItem { it is SeasonItem.Track }
        }
    }

    @Test
    fun `show loading is set to true initially`() = coroutineTest {

        initSUT()

        sut.outputs.showLoading.test {
            assertValue(true)
        }
    }

    @Test
    fun `defaults to value in remote config and not current year`() = coroutineTest {

        every { mockSeasonController.defaultSeason } returns 2018

        initSUT()

        sut.outputs.label.test {
            assertValue(StringHolder( "2018"))
        }
    }

    //endregion

    //region App Banner

    @Test
    fun `when app banner model exists and show is true then it's added to the data`() = coroutineTest {

        val expectedMessage = "Testing the custom app banner!"
        every { mockSeasonController.banner } returns Banner(expectedMessage)

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is SeasonItem.ErrorItem && (it.item as? SyncDataItem.Message)?.msg == expectedMessage }
        }
    }

    @Test
    fun `when app banner model is null then it's not available in the list`() = coroutineTest {

        initSUT()

        sut.outputs.list.test {
            assertListDoesNotMatchItem { it is SeasonItem.ErrorItem && it.item is SyncDataItem.Message }
        }
    }

    //endregion

    //region Schedule

    @Test
    fun `when home type is schedule and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())

        every { mockNetworkConnectivityManager.isConnected } returns false
        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(historyListWithEmptyRound) }

        val expected = listOf<SeasonItem>(
            SeasonItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is schedule and history rounds is empty and network is connected and year is current year, show early in season error`() = coroutineTest {

        val historyItemWithEmptyRound = History(currentSeasonYear, null, emptyList())

        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason.copy(season = currentSeasonYear)) }
        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(historyItemWithEmptyRound) }

        val expected = listOf<SeasonItem>(
            SeasonItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is schedule and history rounds is empty and network is connected and year is in the past, show missing race data message`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())

        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(historyListWithEmptyRound) }

        val expected = listOf<SeasonItem>(
            SeasonItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE))
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is schedule show calendar history list`() = coroutineTest {

        val expected = listOf<SeasonItem>(
            SeasonItem.Track(
                season = mockHistoryRound1.season,
                round = mockHistoryRound1.round,
                raceName = mockHistoryRound1.raceName,
                circuitId = mockHistoryRound1.circuitId,
                circuitName = mockHistoryRound1.circuitName,
                raceCountry = mockHistoryRound1.country,
                raceCountryISO = mockHistoryRound1.countryISO,
                date = mockHistoryRound1.date,
                hasQualifying = mockHistoryRound1.hasQualifying,
                hasResults = mockHistoryRound1.hasResults
            ),
            SeasonItem.Track(
                season = mockHistoryRound2.season,
                round = mockHistoryRound2.round,
                raceName = mockHistoryRound2.raceName,
                circuitId = mockHistoryRound2.circuitId,
                circuitName = mockHistoryRound2.circuitName,
                raceCountry = mockHistoryRound2.country,
                raceCountryISO = mockHistoryRound2.countryISO,
                date = mockHistoryRound2.date,
                hasQualifying = mockHistoryRound2.hasQualifying,
                hasResults = mockHistoryRound2.hasResults
            )
        )

        initSUT()

        sut.outputs.list.test {
            assertListHasSublist(expected)
        }

        verify {
            mockAnalyticsController.logEvent("select_dashboard_schedule", any())
        }
    }

    //endregion

    //region Calendar

    @Test
    fun `when home type is calendar and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())

        every { mockNetworkConnectivityManager.isConnected } returns false
        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(historyListWithEmptyRound) }

        val expected = listOf<SeasonItem>(
            SeasonItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CALENDAR)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is calendar and history rounds is empty and network is connected and year is current year, show early in season error`() = coroutineTest {

        val historyItemWithEmptyRound = History(currentSeasonYear, null, emptyList())

        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason.copy(season = currentSeasonYear)) }
        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(historyItemWithEmptyRound) }

        val expected = listOf<SeasonItem>(
            SeasonItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
        )

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CALENDAR)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is calendar and history rounds is empty and network is connected and year is in the past, show missing race data message`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())

        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(historyListWithEmptyRound) }

        val expected = listOf<SeasonItem>(
            SeasonItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE))
        )

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CALENDAR)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is calendar show calendar history list`() = coroutineTest {

        val expected = expectedFullCalendar(2019)

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CALENDAR)
        advanceUntilIdle()

        sut.outputs.list.test {
        }

        verify {
            mockAnalyticsController.logEvent("select_dashboard_calendar", any())
        }
    }

    @Test
    fun `when home type is calendar show race in expected calendar entry`() = coroutineTest {

        val historyRound1 = mockHistoryRound1.copy(date = LocalDate.of(2019, 7, 7))
        val historyRound2 = mockHistoryRound2.copy(date = LocalDate.of(2019, 10, 13))
        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(mockHistory.copy(rounds = listOf(historyRound1, historyRound2))) }

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CALENDAR)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertListMatchesItem { it is SeasonItem.CalendarWeek && it.race == historyRound1 && it.forMonth == Month.JULY }
            assertListMatchesItem { it is SeasonItem.CalendarWeek && it.race == historyRound2 && it.forMonth == Month.OCTOBER }
        }

        verify {
            mockAnalyticsController.logEvent("select_dashboard_calendar", any())
        }
    }

    //endregion

    //region Drivers

    @Test
    fun `when home type is drivers and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        every { mockNetworkConnectivityManager.isConnected } returns false
        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason.copy(rounds = emptyList())) }

        val expected = listOf<SeasonItem>(
                SeasonItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.DRIVERS)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is drivers and history rounds is empty, show in future season`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason.copy(rounds = emptyList())) }

        val expected = listOf<SeasonItem>(
                SeasonItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
        )

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.DRIVERS)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is drivers list driver standings in order`() = coroutineTest {

        val expected = listOf<SeasonItem>(
            expectedDriver3,
            expectedDriver4,
            expectedDriver1,
            expectedDriver2
        )

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.DRIVERS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }

        verify {
            mockAnalyticsController.logEvent("select_dashboard_driver", any())
        }
    }

    @Test
    fun `when home type is drivers and history rounds size doesnt match rounds available, show results as of header box in response`() = coroutineTest {

        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(History(2019, null, listOf(mockHistoryRound1, mockHistoryRound2, mockHistoryRound3))) }

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.DRIVERS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(SeasonItem.ErrorItem(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(mockRound2.name, mockRound2.round))))
        }
    }

    //endregion

    //region Constructors

    @Test
    fun `when home type is constructors and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        every { mockNetworkConnectivityManager.isConnected } returns false
        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason.copy(rounds = emptyList())) }

        val expected = listOf<SeasonItem>(
                SeasonItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is constructors and history rounds is empty, show in future season`() = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason.copy(rounds = emptyList())) }

        val expected = listOf<SeasonItem>(
                SeasonItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
        )

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is constructors list driver standings in order`() = coroutineTest {

        val expected = listOf<SeasonItem>(
            expectedConstructorAlpha,
            expectedConstructorBeta
        )

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }

        verify {
            mockAnalyticsController.logEvent("select_dashboard_constructor", any())
        }
    }

    @Test
    fun `when home type is constructors and history rounds size doesnt match rounds available, show results as of header box in response`() = coroutineTest {

        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(History(2019, null, listOf(mockHistoryRound1, mockHistoryRound2, mockHistoryRound3))) }

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(SeasonItem.ErrorItem(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(mockRound2.name, mockRound2.round))))
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [1950, 1951, 1952, 1953, 1954, 1955, 1956, 1957])
    fun `when season is before constructor standing season the championship did not start message is displayed`(season: Int) = coroutineTest {

        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason.copy(season = season)) }

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
        sut.inputs.selectSeason(season)

        sut.outputs.list.test {
            assertValue(listOf(
                    SeasonItem.ErrorItem(SyncDataItem.ConstructorsChampionshipNotAwarded)
            ))
        }
    }

    @Test
    fun `when season is on border of constructor standing season the championship did not start message is not displayed`() = coroutineTest {

        val season = 1958
        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason.copy(season = season)) }

        initSUT()
        sut.inputs.clickItem(SeasonNavItem.CONSTRUCTORS)
        sut.inputs.selectSeason(season)

        sut.outputs.list.test {
            assertListExcludesItem(SeasonItem.ErrorItem(SyncDataItem.ConstructorsChampionshipNotAwarded))
        }
    }

    //endregion

    //region Mock Data - Calendar

    private fun expectedFullCalendar(season: Int = 2019): List<SeasonItem> {
        // Build up date
        return listOf(SeasonItem.CalendarHeader) + Month
            .values()
            .map { month ->
                val list = mutableListOf<SeasonItem>()
                list.add(SeasonItem.CalendarMonth(month, season))
                list.add(SeasonItem.CalendarWeek(month, LocalDate.of(season, month.value, 1), null))
                val nextMonday = LocalDate.of(season, month.value, 1).with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                for (x in 0 until LocalDate.of(season, month.value, 1).with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth step 7) {
                    val date = nextMonday.plusDays(x.toLong())
                    if (date.month == month) {
                        list.add(SeasonItem.CalendarWeek(month, date, null))
                    }
                }
                return@map list
            }
            .flatten()
    }

    //endregion

    //region Mock Data - Drivers

    private val expectedDriver1 = SeasonItem.Driver(
            season = 2019,
            points = 21.0,
            driver = mockDriver1,
            driverId = mockDriver1.id,
            position = 3,
            bestQualifying = Pair(1, listOf(mockRound1)),
            bestFinish = Pair(1, listOf(mockRound2)),
            maxPointsInSeason = 27.0,
            animationSpeed = AnimationSpeed.NONE
    )
    private val expectedDriver2 = SeasonItem.Driver(
            season = 2019,
            points = 18.0,
            driver = mockDriver2,
            driverId = mockDriver2.id,
            position = 4,
            bestQualifying = Pair(2, listOf(mockRound1)),
            bestFinish = Pair(3, listOf(mockRound1, mockRound2)),
            maxPointsInSeason = 27.0,
            animationSpeed = AnimationSpeed.NONE
    )
    private val expectedDriver3 = SeasonItem.Driver(
            season = 2019,
            points = 27.0,
            driver = mockDriver3,
            driverId = mockDriver3.id,
            position = 1,
            bestQualifying = Pair(3, listOf(mockRound1, mockRound2)),
            bestFinish = Pair(2, listOf(mockRound1, mockRound2)),
            maxPointsInSeason = 27.0,
            animationSpeed = AnimationSpeed.NONE
    )
    private val expectedDriver4 = SeasonItem.Driver(
            season = 2019,
            points = 24.0,
            driver = mockDriver4,
            driverId = mockDriver4.id,
            position = 2,
            bestQualifying = Pair(1, listOf(mockRound2)),
            bestFinish = Pair(1, listOf(mockRound1)),
            maxPointsInSeason = 27.0,
            animationSpeed = AnimationSpeed.NONE
    )

    //endregion

    //region Mock Data - Constructors

    private val expectedConstructorAlpha = SeasonItem.Constructor(
            season = 2019,
            position = 1,
            constructor = mockConstructorAlpha,
            constructorId = mockConstructorAlpha.id,
            driver = listOf(
                    Pair(mockDriver3.toConstructorDriver(), 27.0),
                    Pair(mockDriver1.toConstructorDriver(), 21.0)
            ),
            points = 48.0,
            maxPointsInSeason = 48.0,
            barAnimation = AnimationSpeed.NONE
    )
    private val expectedConstructorBeta = SeasonItem.Constructor(
            season = 2019,
            position = 2,
            constructor = mockConstructorBeta,
            constructorId = mockConstructorBeta.id,
            driver = listOf(
                    Pair(mockDriver4.toConstructorDriver(), 24.0),
                    Pair(mockDriver2.toConstructorDriver(), 18.0)
            ),
            points = 42.0,
            maxPointsInSeason = 48.0,
            barAnimation = AnimationSpeed.NONE
    )

    //endregion
}