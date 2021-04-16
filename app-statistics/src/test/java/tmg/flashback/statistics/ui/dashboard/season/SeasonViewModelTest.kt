package tmg.flashback.statistics.ui.dashboard.season

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.LocalDate
import tmg.flashback.core.controllers.AnalyticsController
import tmg.flashback.statistics.controllers.NotificationController.Companion.daysUntilDataProvidedBannerMovedToBottom
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.controllers.DeviceController
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.managers.NetworkConnectivityManager
import tmg.flashback.data.db.stats.HistoryRepository
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.flashback.data.models.stats.History
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.core.utils.StringHolder
import tmg.flashback.core.controllers.FeatureController
import tmg.flashback.statistics.*
import tmg.flashback.statistics.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.constants.ViewType
import tmg.flashback.statistics.constants.logEvent
import tmg.flashback.statistics.controllers.NotificationController
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.testutils.*
import tmg.flashback.statistics.testutils.BaseTest
import tmg.flashback.statistics.testutils.assertEventFired
import tmg.flashback.statistics.testutils.test
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable

internal class SeasonViewModelTest: BaseTest() {

    lateinit var sut: SeasonViewModel

    private val mockDeviceController: DeviceController = mockk(relaxed = true)
    private val mockAppearanceController: AppearanceController = mockk(relaxed = true)
    private val mockFeatureController: FeatureController = mockk(relaxed = true)
    private val mockHistoryRepository: HistoryRepository = mockk(relaxed = true)
    private val mockSeasonOverviewRepository: SeasonOverviewRepository = mockk(relaxed = true)
    private val mockSeasonController: SeasonController = mockk(relaxed = true)
    private val mockNotificationController: NotificationController = mockk(relaxed = true)
    private val mockNetworkConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)
    private val mockAnalyticsController: AnalyticsController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationController.banner } returns null

        every { mockNetworkConnectivityManager.isConnected } returns true

        every { mockAppearanceController.animationSpeed } returns AnimationSpeed.NONE
        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason) }
        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(mockHistory) }
    }

    private fun initSUT() {
        sut = SeasonViewModel(
            mockDeviceController,
            mockAppearanceController,
            mockHistoryRepository,
            mockSeasonOverviewRepository,
            mockNotificationController,
            mockSeasonController,
            mockNetworkConnectivityManager,
            mockAnalyticsController
        )
    }

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
    fun `defaults to calendar type by default`() = coroutineTest {

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
        every { mockNotificationController.banner } returns expectedMessage

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

    //region Calendar

    @Test
    fun `when home type is calendar and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())

        every { mockNetworkConnectivityManager.isConnected } returns false
        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(historyListWithEmptyRound) }

        val expected = listOf<SeasonItem>(
                SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
                SeasonItem.ErrorItem(SyncDataItem.NoNetwork)
        )

        initSUT()

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
                SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
                SeasonItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is calendar and history rounds is empty and network is connected and year is in the past, show missing race data message`() = coroutineTest {

        val historyListWithEmptyRound = History(2019, null, emptyList())

        every { mockHistoryRepository.historyFor(any()) } returns flow { emit(historyListWithEmptyRound) }

        val expected = listOf<SeasonItem>(
                SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
                SeasonItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.MISSING_RACE))
        )

        initSUT()

        sut.outputs.list.test {
            assertValue(expected)
        }
    }

    @Test
    fun `when home type is calendar show calendar history list`() = coroutineTest {

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
            mockAnalyticsController.logEvent(ViewType.DASHBOARD_SEASON_SCHEDULE, any())
        }
    }

    //endregion

    //region Drivers

    @Test
    fun `when home type is drivers and history rounds is empty and network not connected, show no network error`() = coroutineTest {

        every { mockNetworkConnectivityManager.isConnected } returns false
        every { mockSeasonOverviewRepository.getSeasonOverview(any()) } returns flow { emit(mockSeason.copy(rounds = emptyList())) }

        val expected = listOf<SeasonItem>(
                SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
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
                SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
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
                SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
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
            mockAnalyticsController.logEvent(ViewType.DASHBOARD_SEASON_DRIVER, any())
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
                SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
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
                SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
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
                SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
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
            mockAnalyticsController.logEvent(ViewType.DASHBOARD_SEASON_CONSTRUCTOR, any())
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
                    SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()),
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

    //region Data provided by banner

    @Test
    fun `banner moves to the bottom when first boot time is greater than 5 days after`() = coroutineTest {

        every { mockDeviceController.appFirstBoot } returns LocalDate.now().minusDays(daysUntilDataProvidedBannerMovedToBottom.toLong() + 1L)

        initSUT()

        sut.outputs.list.test {
            assertListHasLastItem(SeasonItem.ErrorItem(SyncDataItem.ProvidedBy("calendar")))
        }
    }

    @Test
    fun `banner is at the top when first boot time is less than or equal to 10 days after`() = coroutineTest {

        every { mockDeviceController.appFirstBoot } returns LocalDate.now().minusDays(daysUntilDataProvidedBannerMovedToBottom.toLong())

        initSUT()

        sut.outputs.list.test {
            assertListHasFirstItem(SeasonItem.ErrorItem(SyncDataItem.ProvidedBy()))
        }
    }

    //endregion



    //region Mock Data - Drivers

    private val expectedDriver1 = SeasonItem.Driver(
            season = 2019,
            points = 21,
            driver = mockDriver1,
            driverId = mockDriver1.id,
            position = 3,
            bestQualifying = Pair(1, listOf(mockRound1)),
            bestFinish = Pair(1, listOf(mockRound2)),
            maxPointsInSeason = 27,
            animationSpeed = AnimationSpeed.NONE
    )
    private val expectedDriver2 = SeasonItem.Driver(
            season = 2019,
            points = 18,
            driver = mockDriver2,
            driverId = mockDriver2.id,
            position = 4,
            bestQualifying = Pair(2, listOf(mockRound1)),
            bestFinish = Pair(3, listOf(mockRound1, mockRound2)),
            maxPointsInSeason = 27,
            animationSpeed = AnimationSpeed.NONE
    )
    private val expectedDriver3 = SeasonItem.Driver(
            season = 2019,
            points = 27,
            driver = mockDriver3,
            driverId = mockDriver3.id,
            position = 1,
            bestQualifying = Pair(3, listOf(mockRound1, mockRound2)),
            bestFinish = Pair(2, listOf(mockRound1, mockRound2)),
            maxPointsInSeason = 27,
            animationSpeed = AnimationSpeed.NONE
    )
    private val expectedDriver4 = SeasonItem.Driver(
            season = 2019,
            points = 24,
            driver = mockDriver4,
            driverId = mockDriver4.id,
            position = 2,
            bestQualifying = Pair(1, listOf(mockRound2)),
            bestFinish = Pair(1, listOf(mockRound1)),
            maxPointsInSeason = 27,
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
                    Pair(mockDriver3.toDriver(), 27),
                    Pair(mockDriver1.toDriver(), 21)
            ),
            points = 48,
            maxPointsInSeason = 48,
            barAnimation = AnimationSpeed.NONE
    )
    private val expectedConstructorBeta = SeasonItem.Constructor(
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
            barAnimation = AnimationSpeed.NONE
    )

    //endregion
}