package tmg.flashback.home

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.LocalDate
import tmg.flashback.*
import tmg.flashback.di.device.BuildConfigManager
import tmg.flashback.home.HomeMenuItem.*
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.flashback.repo.db.DataRepository
import tmg.flashback.repo.db.stats.HistoryRepository
import tmg.flashback.repo.db.stats.SeasonOverviewRepository
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.repo.models.stats.History
import tmg.flashback.repo.pref.PrefDeviceRepository
import tmg.flashback.shared.sync.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.testutils.*
import tmg.flashback.utils.StringHolder

@Nested
class HomeViewModelTest : BaseTest() {

    lateinit var sut: HomeViewModel

    private val mockSeasonOverviewRepository: SeasonOverviewRepository = mock()
    private val mockHistoryRepository: HistoryRepository = mock()
    private val mockDataRepository: DataRepository = mock()
    private val mockPrefsCustomiseRepository: PrefCustomisationRepository = mock()
    private val mockPrefsDeviceRepository: PrefDeviceRepository = mock()
    private val mockConnectivityManager: NetworkConnectivityManager = mock()
    private val mockRemoteConfigRepository: RemoteConfigRepository = mock()
    private val mockBuildConfigProvider: BuildConfigManager = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockRemoteConfigRepository.defaultYear).thenReturn(2021)
        whenever(mockRemoteConfigRepository.banner).thenReturn(null)

        whenever(mockConnectivityManager.isConnected).thenReturn(true)
        whenever(mockPrefsDeviceRepository.shouldShowReleaseNotes).thenReturn(false)
        whenever(mockPrefsDeviceRepository.appFirstBootTime).thenReturn(LocalDate.now())
        whenever(mockPrefsCustomiseRepository.barAnimation).thenReturn(BarAnimation.NONE)
        whenever(mockSeasonOverviewRepository.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason) })
        whenever(mockHistoryRepository.historyFor(any())).thenReturn(flow { emit(mockHistory) })
        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(null) })
    }

    private fun initSUT() {

        sut = HomeViewModel(mockSeasonOverviewRepository, mockHistoryRepository, mockRemoteConfigRepository, mockDataRepository, mockPrefsCustomiseRepository, mockPrefsDeviceRepository, mockConnectivityManager, mockBuildConfigProvider)
    }

    //region Open release notes

    @Test
    fun `HomeViewModel open release notes fires if the prefs signal we should show release notes`() = coroutineTest {

        whenever(mockPrefsDeviceRepository.shouldShowReleaseNotes).thenReturn(true)

        initSUT()

        sut.outputs.openReleaseNotes.test {
            assertEventFired()
        }
    }

    //endregion

    //region Defaults

    @Test
    fun `HomeViewModel defaults to calendar type by default`() = coroutineTest {

        whenever(mockPrefsDeviceRepository.shouldShowReleaseNotes).thenReturn(true)

        initSUT()

        // Track items should be calendar items
        sut.outputs.list.test {
            assertListMatchesItem { it is HomeItem.Track }
        }
    }

    @Test
    fun `HomeViewModel show loading is set to true initially`() = coroutineTest {

        initSUT()

        sut.outputs.showLoading.test {
            assertValue(true)
        }
    }

    //endregion

    //region App lockout

    @Test
    fun `HomeViewModel app lockout event is fired if show is true and build config provider says version is should lockout`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(expectedAppLockout) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventFired()
        }
    }

    @Test
    fun `HomeViewModel app lockout event is not fired if show is false and build config provider says version is should lockout`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(expectedAppLockout.copy(show = false)) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `HomeViewModel app lockout event is not fired if show is true and build config provider says version is should not lockout`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(expectedAppLockout.copy()) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(false)

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `HomeViewModel app lockout event is not fired if app lockout value is null`() = coroutineTest {

        whenever(mockDataRepository.appLockout()).thenReturn(flow { emit(null) })
        whenever(mockBuildConfigProvider.shouldLockoutBasedOnVersion(any())).thenReturn(true)

        initSUT()
        advanceUntilIdle()

        sut.outputs.openAppLockout.test {
            assertEventNotFired()
        }
    }

    //endregion

    //region App Banner

    @Test
    fun `HomeViewModel when app banner model exists and show is true then it's added to the data`() = coroutineTest {

        val expectedMessage = "Testing the custom app banner!"
        whenever(mockRemoteConfigRepository.banner).thenReturn(expectedMessage)

        initSUT()

        sut.outputs.list.test {
            assertListMatchesItem { it is HomeItem.ErrorItem && (it.item as? SyncDataItem.Message)?.msg == expectedMessage }
        }
    }

    @Test
    fun `HomeViewModel when app banner model is null then it's not available in the list`() = coroutineTest {

        initSUT()

        sut.outputs.list.test {
            assertListDoesNotMatchItem { it is HomeItem.ErrorItem && it.item is SyncDataItem.Message }
        }
    }

    //endregion

    //region Calendar

    @Test
    fun `HomeViewModel when home type is calendar and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockHistoryRepository.historyFor(any())).thenReturn(flow { emit(historyListWithEmptyRound) })
        val expected = listOf<HomeItem>(
                HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
                HomeItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `HomeViewModel when home type is calendar and history rounds is empty and network is connected and year is current year, show early in season error`() = coroutineTest {

        val historyItemWithEmptyRound = History(currentYear, null, emptyList())

        whenever(mockSeasonOverviewRepository.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(season = currentYear)) })
        whenever(mockHistoryRepository.historyFor(any())).thenReturn(flow { emit(historyItemWithEmptyRound) })
        val expected = listOf<HomeItem>(
                HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
                HomeItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `HomeViewModel when home type is calendar and history rounds is empty and network is connected and year is in the past, show missing race data message`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())
        whenever(mockHistoryRepository.historyFor(any())).thenReturn(flow { emit(historyListWithEmptyRound) })
        val expected = listOf<HomeItem>(
            HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
            HomeItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE))
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
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

        sut.outputs.list.test {
            assertListHasSublist(expected)
        }
    }

    //endregion

    //region Drivers

    @Test
    fun `HomeViewModel when home type is drivers and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockSeasonOverviewRepository.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(rounds = emptyList())) })
        val expected = listOf<HomeItem>(
            HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
            HomeItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()
        sut.inputs.clickItem(DRIVERS)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `HomeViewModel when home type is drivers and history rounds is empty, show in future season`() = coroutineTest {

        whenever(mockSeasonOverviewRepository.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(rounds = emptyList())) })
        val expected = listOf<HomeItem>(
            HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
            HomeItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
        )

        initSUT()
        sut.inputs.clickItem(DRIVERS)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `HomeViewModel when home type is drivers list driver standings in order`() = coroutineTest {

        val expected = listOf<HomeItem>(
                HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
                expectedDriver3,
                expectedDriver4,
                expectedDriver1,
                expectedDriver2
        )

        initSUT()
        sut.inputs.clickItem(DRIVERS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `HomeViewModel when home type is drivers and history rounds size doesnt match rounds available, show results as of header box in response`() = coroutineTest {

        whenever(mockHistoryRepository.historyFor(any())).thenReturn(flow { emit(History(2019, null, listOf(mockHistoryRound1, mockHistoryRound2, mockHistoryRound3))) })

        initSUT()
        sut.inputs.clickItem(DRIVERS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(HomeItem.ErrorItem(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(mockRound2.name, mockRound2.round))))
        }
    }

    //endregion


    //region Constructors

    @Test
    fun `HomeViewModel when home type is constructors and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        whenever(mockConnectivityManager.isConnected).thenReturn(false)
        whenever(mockSeasonOverviewRepository.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(rounds = emptyList())) })
        val expected = listOf<HomeItem>(
            HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
            HomeItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `HomeViewModel when home type is constructors and history rounds is empty, show in future season`() = coroutineTest {

        whenever(mockSeasonOverviewRepository.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(rounds = emptyList())) })
        val expected = listOf<HomeItem>(
            HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
            HomeItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
        )

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `HomeViewModel when home type is constructors list driver standings in order`() = coroutineTest {

        val expected = listOf<HomeItem>(
                HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
                expectedConstructorAlpha,
                expectedConstructorBeta
        )

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `HomeViewModel when home type is constructors and history rounds size doesnt match rounds available, show results as of header box in response`() = coroutineTest {

        whenever(mockHistoryRepository.historyFor(any())).thenReturn(flow { emit(History(2019, null, listOf(mockHistoryRound1, mockHistoryRound2, mockHistoryRound3))) })

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        sut.inputs.selectSeason(2019)
        advanceUntilIdle()

        sut.outputs.list.test {
            assertListContainsItem(HomeItem.ErrorItem(SyncDataItem.MessageRes(R.string.results_accurate_for, listOf(mockRound2.name, mockRound2.round))))
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [1950, 1951, 1952, 1953, 1954, 1955, 1956, 1957])
    fun `HomeViewModel when season is before constructor standing season the championship did not start message is displayed`(season: Int) = coroutineTest {

        whenever(mockSeasonOverviewRepository.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(season = season)) })

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        sut.inputs.selectSeason(season)

        sut.outputs.list.test {
            assertValue(listOf(
                HomeItem.ErrorItem(SyncDataItem.ProvidedBy),
                HomeItem.ErrorItem(SyncDataItem.ConstructorsChampionshipNotAwarded)
            ))
        }
    }

    @Test
    fun `HomeViewModel when season is on border of constructor standing season the championship did not start message is not displayed`() = coroutineTest {

        val season = 1958
        whenever(mockSeasonOverviewRepository.getSeasonOverview(any())).thenReturn(flow { emit(mockSeason.copy(season = season)) })

        initSUT()
        sut.inputs.clickItem(CONSTRUCTORS)
        sut.inputs.selectSeason(season)

        sut.outputs.list.test {
            assertListExcludesItem(HomeItem.ErrorItem(SyncDataItem.ConstructorsChampionshipNotAwarded))
        }
    }

    //endregion

    //region Season list

    @Test
    fun `HomeViewModel clicking item seasons fires open season list event`() = coroutineTest {

        initSUT()

        whenever(mockPrefsCustomiseRepository.showBottomSheetExpanded).thenReturn(true)
        sut.inputs.clickItem(SEASONS)
        advanceUntilIdle()

        sut.outputs.openSeasonList.test {
            assertDataEventValue(true)
        }

        whenever(mockPrefsCustomiseRepository.showBottomSheetExpanded).thenReturn(false)
        sut.inputs.clickItem(SEASONS)
        advanceUntilIdle()

        sut.outputs.openSeasonList.test {
            assertDataEventValue(false)
        }
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

        sut.outputs.label.test {
            assertValue(StringHolder(msg = "2019"))
        }
    }

    //endregion

    @Test
    fun `HomeViewModel banner moves to the bottom when first boot time is greater than 10 days after`() = coroutineTest {

        whenever(mockPrefsDeviceRepository.appFirstBootTime).thenReturn(LocalDate.now().minusDays(daysUntilDataProvidedBannerMovedToBottom.toLong() + 1L))

        initSUT()

        sut.outputs.list.test {
            assertListHasLastItem(HomeItem.ErrorItem(SyncDataItem.ProvidedBy))
        }
    }

    @Test
    fun `HomeViewModel banner is at the top when first boot time is less than or equal to 10 days after`() = coroutineTest {

        whenever(mockPrefsDeviceRepository.appFirstBootTime).thenReturn(LocalDate.now().minusDays(daysUntilDataProvidedBannerMovedToBottom.toLong()))

        initSUT()

        sut.outputs.list.test {
            assertListHasFirstItem(HomeItem.ErrorItem(SyncDataItem.ProvidedBy))
        }
    }

    @AfterEach
    internal fun tearDown() {
        reset(mockSeasonOverviewRepository, mockHistoryRepository, mockDataRepository, mockPrefsCustomiseRepository, mockConnectivityManager, mockBuildConfigProvider)
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
            maxPointsInSeason = 27,
            barAnimation = BarAnimation.NONE
    )
    private val expectedDriver2 = HomeItem.Driver(
            season = 2019,
            points = 18,
            driver = mockDriver2,
            driverId = mockDriver2.id,
            position = 4,
            bestQualifying = Pair(2, listOf(mockRound1)),
            bestFinish = Pair(3, listOf(mockRound1, mockRound2)),
            maxPointsInSeason = 27,
            barAnimation = BarAnimation.NONE
    )
    private val expectedDriver3 = HomeItem.Driver(
            season = 2019,
            points = 27,
            driver = mockDriver3,
            driverId = mockDriver3.id,
            position = 1,
            bestQualifying = Pair(3, listOf(mockRound1, mockRound2)),
            bestFinish = Pair(2, listOf(mockRound1, mockRound2)),
            maxPointsInSeason = 27,
            barAnimation = BarAnimation.NONE
    )
    private val expectedDriver4 = HomeItem.Driver(
            season = 2019,
            points = 24,
            driver = mockDriver4,
            driverId = mockDriver4.id,
            position = 2,
            bestQualifying = Pair(1, listOf(mockRound2)),
            bestFinish = Pair(1, listOf(mockRound1)),
            maxPointsInSeason = 27,
            barAnimation = BarAnimation.NONE
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
            maxPointsInSeason = 48,
            barAnimation = BarAnimation.NONE
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
            maxPointsInSeason = 48,
            barAnimation = BarAnimation.NONE
    )

    //endregion
}