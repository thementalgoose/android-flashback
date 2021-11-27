package tmg.flashback.statistics.ui.race

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.ui.race.RaceDisplayType.*
import tmg.flashback.statistics.ui.race_old.*
import tmg.flashback.statistics.ui.race_old.driver1
import tmg.flashback.statistics.ui.race_old.driver2
import tmg.flashback.statistics.ui.race_old.driver3
import tmg.flashback.statistics.ui.race_old.driver4
import tmg.flashback.statistics.ui.race_old.raceModel
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.ui.controllers.ThemeController
import tmg.flashback.ui.model.AnimationSpeed
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class RaceViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)
    private val mockRaceController: RaceController = mockk(relaxed = true)
    private val mockThemeController: ThemeController = mockk(relaxed = true)
    private val mockConnectivityManager: NetworkConnectivityManager = mockk(relaxed = true)

    private lateinit var sut: RaceViewModel

    private fun initSUT() {
        sut = RaceViewModel(
            mockRaceRepository,
            mockRaceController,
            mockThemeController,
            mockConnectivityManager,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockConnectivityManager.isConnected } returns true
        coEvery { mockRaceRepository.shouldSyncRace(any(), any()) } returns false
        coEvery { mockRaceRepository.getRace(any(), any()) } returns flow { emit(raceModel) }
        coEvery { mockRaceRepository.fetchRaces(any()) } returns true
        every { mockThemeController.animationSpeed } returns AnimationSpeed.QUICK
    }

    //region Race loading states

    @Test
    fun `race data with null value and no internet shows pull to refresh`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns false

        initSUT()
        runBlockingTest {
            sut.inputs.initialise(2020, 1)
            sut.inputs.displayType(RACE)
        }
        sut.outputs.list.test {
            assertValue(listOf(
                RaceItem.errorItemModel(SyncDataItem.PullRefresh)
            ))
        }
    }

    @Test
    fun `race data with null value and internet shows race data empty`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(null) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.list.test {
            assertValue(listOf(
                RaceItem.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.RACE_DATA_EMPTY))
            ))
        }
    }

    @Test
    fun `race data with no data and date in the future shows race in future`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(
            Race.model(
                raceInfo = RaceInfo.model(
                    date = LocalDate.now().plusDays(1)
                ),
                qualifying = emptyList(),
                race = emptyList()
            )
        ) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.list.test {
            assertValue(listOf(
                RaceItem.overviewModel(raceDate = LocalDate.now().plusDays(1)),
                RaceItem.scheduleMaxModel(),
                RaceItem.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE))
            ))
        }
    }

    @Test
    fun `race data with no data and date today shows coming soon`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(
            Race.model(
                raceInfo = RaceInfo.model(
                    date = LocalDate.now()
                ),
                qualifying = emptyList(),
                race = emptyList()
            )
        ) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.list.test {
            assertValue(listOf(
                RaceItem.overviewModel(raceDate = LocalDate.now()),
                RaceItem.scheduleMaxModel(),
                RaceItem.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE))
            ))
        }
    }

    @Test
    fun `race data with no data and date within 10 days shows coming soon`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(
            Race.model(
                raceInfo = RaceInfo.model(
                    date = LocalDate.now().minusDays(1L)
                ),
                qualifying = emptyList(),
                race = emptyList()
            )
        ) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.list.test {
            assertValue(listOf(
                RaceItem.overviewModel(raceDate = LocalDate.now().minusDays(1L)),
                RaceItem.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE))
            ))
        }
    }

    @Test
    fun `race data with no data and date more than 10 days shows race data empty`() = coroutineTest {
        every { mockRaceRepository.getRace(any(), any()) } returns flow { emit(
            Race.model(
                raceInfo = RaceInfo.model(
                    date = LocalDate.now().minusDays(11L)
                ),
                qualifying = emptyList(),
                race = emptyList()
            )
        ) }
        every { mockConnectivityManager.isConnected } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)
        sut.outputs.list.test {
            assertValue(listOf(
                RaceItem.overviewModel(raceDate = LocalDate.now().minusDays(11L)),
                RaceItem.errorItemModel(SyncDataItem.Unavailable(DataUnavailable.RACE_DATA_EMPTY))
            ))
        }
    }

    //endregion

    //region Race display types

    @Test
    fun `race type displays race item`() = coroutineTest {
        initSUT()
        runBlockingTest {
            sut.inputs.initialise(2020, 1)
        }

        sut.outputs.list
            .test {
                latestValue!!.forEachIndexed { index, model ->
                    when (model) {
                        is RaceItem.Overview -> assertEquals(0, index)
                        is RaceItem.Podium -> {
                            assertEquals(1, index)
                            assertEquals(driver1, model.driverFirst.driver)
                            assertEquals(driver2, model.driverSecond.driver)
                            assertEquals(driver4, model.driverThird.driver)
                        }
                        is RaceItem.RaceHeader -> assertEquals(2, index)
                        is RaceItem.RaceResult -> {
                            assertEquals(3, index)
                            assertEquals(driver3, model.race.driver)
                        }
                        else -> fail("Unexpected value")
                    }
                }
            }
    }

    @Test
    fun `qualifying type displays race item`() = coroutineTest {
        initSUT()
        runBlockingTest {
            sut.inputs.initialise(2020, 1)
            sut.inputs.displayType(QUALIFYING)
        }

        sut.outputs.list
            .test {
                latestValue!!.forEachIndexed { index, model ->
                    when (model) {
                        is RaceItem.Overview -> assertEquals(0, index)
                        is RaceItem.QualifyingHeader -> assertEquals(1, index)
                        is RaceItem.QualifyingResultQ1Q2Q3 -> {
                            when (index) {
                                2 -> assertEquals(driver1, model.driver)
                                3 -> assertEquals(driver2, model.driver)
                                4 -> assertEquals(driver3, model.driver)
                                5 -> assertEquals(driver4, model.driver)
                                else -> fail("Unexpected value")
                            }
                        }
                        else -> fail("Unexpected value")
                    }
                }
            }

    }

    @Test
    fun `constructors type displays race item`() = coroutineTest {
        initSUT()

        runBlockingTest {
            sut.inputs.initialise(2020, 1)
            sut.inputs.displayType(CONSTRUCTOR)
        }

        sut.outputs.list.test {
            assertValue(listOf(
                RaceItem.overviewModel(),
                RaceItem.constructorModel(
                    points = 25.0 + 18.0 + 15.0 + 12.0,
                    driver = listOf(
                        driver1.driver to 25.0,
                        driver2.driver to 18.0,
                        driver4.driver to 15.0,
                        driver3.driver to 12.0,
                    )
                )
            ))
        }
    }

    //endregion

    //region Request

    @Test
    fun `race requests is not made when should sync is false`() = coroutineTest {
        coEvery { mockRaceRepository.shouldSyncRace(any(), any()) } returns false

        initSUT()
        runBlockingTest {
            sut.inputs.initialise(2020, 1)
            sut.inputs.displayType(RACE)
        }

        coVerify(exactly = 0) {
            mockRaceRepository.fetchRaces(any())
        }
    }

    @Test
    fun `race requests is made when should sync is true`() = coroutineTest {
        coEvery { mockRaceRepository.shouldSyncRace(any(), any()) } returns true

        initSUT()
        sut.inputs.initialise(2020, 1)

        sut.outputs.list.test {
            assertListMatchesItem { it is RaceItem.RaceHeader }
        }
        coVerify {
            mockRaceRepository.fetchRaces(any())
        }
    }

    //endregion

    //region Go to driver

    @Test
    fun `go to driver fires go to driver event`() = coroutineTest {
        val input = Driver.model()

        initSUT()
        sut.inputs.clickDriver(input)
        sut.outputs.goToDriver.test {
            assertDataEventValue(input)
        }
    }

    //endregion

    //region Go to constructor

    @Test
    fun `go to constructor fires go to constructor event`() = coroutineTest {
        val input = Constructor.model()

        initSUT()
        sut.inputs.clickConstructor(input)
        sut.outputs.goToConstructor.test {
            assertDataEventValue(input)
        }
    }

    //endregion

    //region Refresh

    @Test
    fun `refresh calls overview and race repos and show loading false when done`() = coroutineTest {
        initSUT()
        sut.inputs.initialise(2020, 1)

        runBlockingTest {
            sut.inputs.refresh()
        }

        coVerify {
            mockRaceRepository.fetchRaces(any())
        }
        sut.outputs.showLoading.test {
            assertValue(false)
        }
    }

    //endregion

}