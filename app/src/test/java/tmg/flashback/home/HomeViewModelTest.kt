package tmg.flashback.home

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.*
import tmg.flashback.di.device.BuildConfigProvider
import tmg.flashback.home.HomeMenuItem.*
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.DataDB
import tmg.flashback.repo.db.stats.HistoryDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.repo.models.stats.History
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.testutils.*
import tmg.flashback.utils.StringHolder

@FlowPreview
@ExperimentalCoroutinesApi
@Nested
class HomeViewModelTest : BaseTest() {

    lateinit var sut: HomeViewModel

    private val mockSeasonOverviewDB: SeasonOverviewDB = mock()
    private val mockHistoryDB: HistoryDB = mock()
    private val mockDataDB: DataDB = mock()
    private val mockPrefsDB: PrefsDB = mock()
    private val mockConnectivityManager: ConnectivityManager = mock()
    private val mockBuildConfigProvider: BuildConfigProvider = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockConnectivityManager.isConnected).thenReturn(true)
        whenever(mockPrefsDB.shouldShowReleaseNotes).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason) })
        whenever(mockHistoryDB.historyFor(any())).thenReturn(flow { emit(mockHistory) })
        whenever(mockDataDB.appBanner()).thenReturn(flow { emit(null) })
        whenever(mockDataDB.appLockout()).thenReturn(flow { emit(null) })
    }

    private fun initSUT() {

        sut = HomeViewModel(mockSeasonOverviewDB, mockHistoryDB, mockDataDB, mockPrefsDB, mockConnectivityManager, mockBuildConfigProvider, testScopeProvider)
    }

    //region Open release notes

    @Test
    fun `HomeViewModel open release notes fires if the prefs signal we should show release notes`() = coroutineTest {

        whenever(mockPrefsDB.shouldShowReleaseNotes).thenReturn(true)

        initSUT()

        assertEventFired(sut.outputs.openReleaseNotes)
    }

    //endregion

    //region Defaults

    @Test
    fun `HomeViewModel defaults to calendar type by default`() = coroutineTest {

        whenever(mockPrefsDB.shouldShowReleaseNotes).thenReturn(true)

        initSUT()

        // Track items should be calendar items
        assertListContains(sut.outputs.list) {
            it is HomeItem.Track
        }
    }

    @Test
    fun `HomeViewModel show loading is set to true initially`() = coroutineTest {

        initSUT()

        assertValue(true, sut.outputs.showLoading)
    }

    //endregion

    //region App lockout

    @Test
    fun `HomeViewModel app lockout event is fired if show is true and build config provider says version is should lockout`() = coroutineTest {

        whenever(mockDataDB.appLockout()).thenReturn(flow { emit(expectedAppLockout) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)

        initSUT()
        advanceUntilIdle()

        assertEventFired(sut.outputs.openAppLockout)
    }

    @Test
    fun `HomeViewModel app lockout event is not fired if show is false and build config provider says version is should lockout`() = coroutineTest {

        whenever(mockDataDB.appLockout()).thenReturn(flow { emit(expectedAppLockout.copy(show = false)) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)

        initSUT()
        advanceUntilIdle()

        assertEventNotFired(sut.outputs.openAppLockout)
    }

    @Test
    fun `HomeViewModel app lockout event is not fired if show is true and build config provider says version is should not lockout`() = coroutineTest {

        whenever(mockDataDB.appLockout()).thenReturn(flow { emit(expectedAppLockout.copy()) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(false)

        initSUT()
        advanceUntilIdle()

        assertEventNotFired(sut.outputs.openAppLockout)
    }

    @Test
    fun `HomeViewModel app lockout event is not fired if app lockout value is null`() = coroutineTest {

        whenever(mockDataDB.appLockout()).thenReturn(flow { emit(null) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)

        initSUT()
        advanceUntilIdle()

        assertEventNotFired(sut.outputs.openAppLockout)
    }

    //endregion

    //region App Banner

    @Test
    fun `HomeViewModel when app banner model exists and show is true then it's added to the data`() = coroutineTest {

        val expectedMessage = "Testing the custom app banner!"
        whenever(mockDataDB.appBanner()).thenReturn(flow { emit(AppBanner(show = true, message = expectedMessage)) })

        initSUT()

        assertListContains(sut.outputs.list) {
            it is HomeItem.ErrorItem && (it.item as? SyncDataItem.Message)?.msg == expectedMessage
        }
    }

    @Test
    fun `HomeViewModel when app banner model exists and show is false then it's not available in the list`() = coroutineTest {

        val expectedMessage = "Testing the custom app banner!"
        whenever(mockDataDB.appBanner()).thenReturn(flow { emit(AppBanner(show = false, message = expectedMessage)) })

        initSUT()

        assertListDoesntContains(sut.outputs.list) {
            it is HomeItem.ErrorItem && (it.item as? SyncDataItem.Message)?.msg == expectedMessage
        }
    }

    @Test
    fun `HomeViewModel when app banner model exists with empty message and show is true then it's not available in the list`() = coroutineTest {

        val expectedMessage = ""
        whenever(mockDataDB.appBanner()).thenReturn(flow { emit(AppBanner(show = true, message = expectedMessage)) })

        initSUT()

        assertListDoesntContains(sut.outputs.list) {
            it is HomeItem.ErrorItem && (it.item as? SyncDataItem.Message)?.msg == expectedMessage
        }
    }

    @Test
    fun `HomeViewModel when app banner model is null then it's not available in the list`() = coroutineTest {

        val expectedMessage = "Testing the custom app banner!"
        whenever(mockDataDB.appBanner()).thenReturn(flow { emit(null) })

        initSUT()

        assertListDoesntContains(sut.outputs.list) {
            it is HomeItem.ErrorItem && (it.item as? SyncDataItem.Message)?.msg == expectedMessage
        }
    }

    //endregion

    //region Calendar

    @Test
    fun `HomeViewModel when home type is calendar and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockHistoryDB.historyFor(any())).thenReturn(flow { emit(historyListWithEmptyRound) })
        val expected = listOf<HomeItem>(
                HomeItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `HomeViewModel when home type is calendar and history rounds is empty and network is connected and year is current year, show early in season error`() = coroutineTest {

        val historyItemWithEmptyRound = History(currentYear, null, emptyList())

        whenever(mockSeasonOverviewDB.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(season = currentYear)) })
        whenever(mockHistoryDB.historyFor(any())).thenReturn(flow { emit(historyItemWithEmptyRound) })
        val expected = listOf<HomeItem>(
                HomeItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
        )

        initSUT()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `HomeViewModel when home type is calendar and history rounds is empty and network is connected and year is in the past, show missing race data message`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())
        whenever(mockHistoryDB.historyFor(any())).thenReturn(flow { emit(historyListWithEmptyRound) })
        val expected = listOf<HomeItem>(HomeItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE)))

        initSUT()

        assertValue(expected, sut.outputs.list)
    }


    @Test
    fun `HomeViewModel when home type is calendar show calendar history list`() = coroutineTest {

        val expected = listOf<HomeItem>(
                HomeItem.Track(
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
                HomeItem.Track(
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

        assertValue(expected, sut.outputs.list)
    }

    //endregion

    //region Drivers

    @Test
    fun `HomeViewModel when home type is drivers and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(rounds = emptyList())) })
        val expected = listOf<HomeItem>(HomeItem.ErrorItem(SyncDataItem.NoNetwork))

        initSUT()
        sut.inputs.clickItem(DRIVERS)
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `HomeViewModel when home type is drivers and history rounds is empty, show in future season`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(rounds = emptyList())) })
        val expected = listOf<HomeItem>(HomeItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON)))

        initSUT()
        sut.inputs.clickItem(DRIVERS)
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `HomeViewModel when home type is drivers list driver standings in order`() = coroutineTest {

        val expected = listOf<HomeItem>(
                expectedDriver3,
                expectedDriver4,
                expectedDriver1,
                expectedDriver2
        )

        initSUT()
        sut.inputs.clickItem(DRIVERS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `HomeViewModel when home type is drivers and history rounds size doesnt match rounds available, show results as of header box in response`() = coroutineTest {

        whenever(mockHistoryDB.historyFor(any())).thenReturn(flow { emit(History(2019, null, listOf(mockHistoryRound1, mockHistoryRound2, mockHistoryRound3))) })

        initSUT()
        sut.inputs.clickItem(DRIVERS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        assertListContains(sut.outputs.list) {
            it is HomeItem.ErrorItem &&
                    (it.item as? SyncDataItem.MessageRes)?.msg == R.string.results_accurate_for &&
                    (it.item as? SyncDataItem.MessageRes)?.values == listOf(mockRound2.name, mockRound2.round)
        }
    }

    //endregion


    //region Constructors

    @Test
    fun `HomeViewModel when home type is constructors and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockSeasonOverviewDB.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(rounds = emptyList())) })
        val expected = listOf<HomeItem>(HomeItem.ErrorItem(SyncDataItem.NoNetwork))

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `HomeViewModel when home type is constructors and history rounds is empty, show in future season`() = coroutineTest {

        whenever(mockSeasonOverviewDB.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(rounds = emptyList())) })
        val expected = listOf<HomeItem>(HomeItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON)))

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `HomeViewModel when home type is constructors list driver standings in order`() = coroutineTest {

        val expected = listOf<HomeItem>(
                expectedConstructorAlpha,
                expectedConstructorBeta
        )

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        assertValue(expected, sut.outputs.list)
    }

    @Test
    fun `HomeViewModel when home type is constructors and history rounds size doesnt match rounds available, show results as of header box in response`() = coroutineTest {

        whenever(mockHistoryDB.historyFor(any())).thenReturn(flow { emit(History(2019, null, listOf(mockHistoryRound1, mockHistoryRound2, mockHistoryRound3))) })

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        assertListContains(sut.outputs.list) {
            it is HomeItem.ErrorItem &&
                    (it.item as? SyncDataItem.MessageRes)?.msg == R.string.results_accurate_for &&
                    (it.item as? SyncDataItem.MessageRes)?.values == listOf(mockRound2.name, mockRound2.round)
        }
    }

    //endregion

    //region Season list

    @Test
    fun `HomeViewModel clicking item seasons fires open season list event`() = coroutineTest {

        initSUT()

        whenever(mockPrefsDB.showBottomSheetExpanded).thenReturn(true)
        sut.inputs.clickItem(SEASONS)
        advanceUntilIdle()

        assertDataEventValue(true, sut.outputs.openSeasonList)

        whenever(mockPrefsDB.showBottomSheetExpanded).thenReturn(false)
        sut.inputs.clickItem(SEASONS)
        advanceUntilIdle()

        assertDataEventValue(false, sut.outputs.openSeasonList)
    }

    //endregion

    //region Labels and current season

    @ParameterizedTest
    @CsvSource(
            "CALENDAR",
            "DRIVERS",
            "CONSTRUCTORS",
            "SEASONS"
    )
    fun `HomeViewModel label output when current tab changes maps to correct value`(enum: HomeMenuItem) = coroutineTest {

        initSUT()
        sut.inputs.selectSeason(2019)
        sut.inputs.clickItem(enum)

        advanceUntilIdle()

        assertValue(StringHolder(msg = "2019"), sut.outputs.label)
    }

    //endregion

    @AfterEach
    internal fun tearDown() {
        reset(mockSeasonOverviewDB, mockHistoryDB, mockDataDB, mockPrefsDB, mockConnectivityManager, mockBuildConfigProvider)
    }



    //region Mock Data - App lockout

    private val expectedAppLockout = AppLockout(
            show = true,
            title = "msg",
            message = "Another msg",
            linkText = null,
            link = null,
            version = 10
    )

    //endregion

    //region Mock Data - Drivers

    private val expectedDriver1 = HomeItem.Driver(
            season = 2019,
            points = 21,
            driver = mockDriver1,
            driverId = mockDriver1.id,
            position = 3,
            bestQualifying = Pair(1, listOf(mockRound1)),
            bestFinish = Pair(1, listOf(mockRound2)),
            maxPointsInSeason = 27
    )
    private val expectedDriver2 = HomeItem.Driver(
            season = 2019,
            points = 18,
            driver = mockDriver2,
            driverId = mockDriver2.id,
            position = 4,
            bestQualifying = Pair(2, listOf(mockRound1)),
            bestFinish = Pair(3, listOf(mockRound1, mockRound2)),
            maxPointsInSeason = 27
    )
    private val expectedDriver3 = HomeItem.Driver(
            season = 2019,
            points = 27,
            driver = mockDriver3,
            driverId = mockDriver3.id,
            position = 1,
            bestQualifying = Pair(3, listOf(mockRound1, mockRound2)),
            bestFinish = Pair(2, listOf(mockRound1, mockRound2)),
            maxPointsInSeason = 27
    )
    private val expectedDriver4 = HomeItem.Driver(
            season = 2019,
            points = 24,
            driver = mockDriver4,
            driverId = mockDriver4.id,
            position = 2,
            bestQualifying = Pair(1, listOf(mockRound2)),
            bestFinish = Pair(1, listOf(mockRound1)),
            maxPointsInSeason = 27
    )

    //endregion

    //region Mock Data - Constructors

    private val expectedConstructorAlpha = HomeItem.Constructor(
            season = 2019,
            position = 1,
            constructor = mockConstructorAlpha,
            constructorId = mockConstructorAlpha.id,
            driver = listOf(
                    Pair(mockDriver3.toDriver(), 27),
                    Pair(mockDriver1.toDriver(), 21)
            ),
            points = 48,
            maxPointsInSeason = 48
    )
    private val expectedConstructorBeta = HomeItem.Constructor(
            season = 2019,
            position = 2,
            constructor = mockConstructorBeta,
            constructorId = mockConstructorBeta.id,
            driver = listOf(
                    Pair(mockDriver4.toDriver(), 24),
                    Pair(mockDriver2.toDriver(), 18)
            ),
            points = 42,
            maxPointsInSeason = 48
    )

    //endregion
}