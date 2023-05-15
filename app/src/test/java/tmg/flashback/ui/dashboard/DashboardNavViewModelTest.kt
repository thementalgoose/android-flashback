package tmg.flashback.ui.dashboard

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.results.Calendar
import tmg.flashback.results.Constructors
import tmg.flashback.results.Drivers
import tmg.flashback.results.usecases.DefaultSeasonUseCase
import tmg.flashback.results.with
import tmg.flashback.ui.settings.All
import tmg.flashback.usecases.DashboardSyncUseCase
import tmg.flashback.usecases.GetSeasonsUseCase
import tmg.flashback.usecases.IsFirst
import tmg.flashback.usecases.IsLast
import tmg.testutils.BaseTest
import tmg.testutils.junit.toSealedClass
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class DashboardNavViewModelTest: BaseTest() {

    private val mockRssRepository: RssRepository = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockGetSeasonUseCase: GetSeasonsUseCase = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockCrashManager: CrashManager = mockk(relaxed = true)
    private val mockDashboardSyncUseCase: DashboardSyncUseCase = mockk(relaxed = true)
    private val mockDebugNavigationComponent: DebugNavigationComponent = mockk(relaxed = true)

    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockNavController: NavHostController = mockk(relaxed = true)

    private lateinit var underTest: DashboardNavViewModel

    private fun initUnderTest() {
        underTest = DashboardNavViewModel(
            rssRepository = mockRssRepository,
            defaultSeasonUseCase = mockDefaultSeasonUseCase,
            navigator = mockNavigator,
            getSeasonUseCase = mockGetSeasonUseCase,
            applicationNavigationComponent = mockApplicationNavigationComponent,
            crashManager = mockCrashManager,
            dashboardSyncUseCase = mockDashboardSyncUseCase,
            debugNavigationComponent = mockDebugNavigationComponent,
            ioDispatcher = Dispatchers.Unconfined
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2019
    }

    @Test
    fun `initial load of vm runs dashboard use case`() {
        every { mockNavigator.navController } returns mockNavController
        initUnderTest()

        verify {
            mockNavigator.navController
            mockNavController.addOnDestinationChangedListener(any())
        }
        coVerify {
            mockDashboardSyncUseCase.sync()
        }
        verify {
            mockCrashManager.log(any())
        }
    }

    @ParameterizedTest(name = "Route {0} means currently selected menu item is updated to {1}")
    @CsvSource(
        "results/calendar/2022,Calendar",
        "results/drivers/2022,Drivers",
        "results/constructors/2022,Constructors",
        "settings,Settings",
        "settings/rss,Settings",
        "rss,RSS",
        "search,Search",
        "drivers/,",
        "constructors/,",
        "circuits/,"
    )
    fun `menu item gets updated when route is updated`(route: String, menuItemName: String?) {
        val menuItem = menuItemName?.toSealedClass(MenuItem::class)

        initUnderTest()
        underTest.onDestinationChanged(mockNavController, NavDestination(route), null)
        underTest.currentlySelectedItem.test {
            if (menuItem == null) {
                assertEmittedCount(0)
            } else {
                assertValue(menuItem)
            }
        }
    }

    @ParameterizedTest(name = "Route {0} means menu shown is {1}")
    @CsvSource(
        "results/calendar/2022,true",
        "results/drivers/2022,true",
        "results/constructors/2022,true",
        "settings,true",
        "rss,true",
        "search,true",
        "drivers/,false",
        "settings/rss,false",
        "constructors/,false",
        "circuits/,false"
    )
    fun `menu is shown when route is updated`(route: String, showMenu: Boolean) {

        initUnderTest()

        underTest.onDestinationChanged(mockNavController, NavDestination(route), null)
        underTest.showMenu.test {
            assertValue(showMenu)
        }
    }

    @ParameterizedTest(name = "Route {0} means showing bottom menu bar is {1}")
    @CsvSource(
        "results/calendar/2022,true",
        "results/drivers/2022,true",
        "results/constructors/2022,true",
        "settings,false",
        "settings/rss,false",
        "rss,false",
        "search,false",
        "drivers/,false",
        "constructors/,false",
        "circuits/,false"
    )
    fun `menu bottom bar when route is updated`(route: String, showBottomBar: Boolean) {

        initUnderTest()
        underTest.onDestinationChanged(mockNavController, NavDestination(route), null)
        underTest.showBottomBar.test {
            assertValue(showBottomBar)
        }
    }

    @ParameterizedTest(name = "Menu item {0} shows result {1}")
    @CsvSource(
        "Calendar,results/calendar/2019",
        "Constructors,results/constructors/2019",
        "Drivers,results/drivers/2019",
        "RSS,rss",
        "Settings,settings",
        "Search,search"

    )
    fun `clicking item opens item`(menuItemName: String, route: String) {
        val menuItem = menuItemName.toSealedClass(MenuItem::class)

        initUnderTest()
        underTest.inputs.clickItem(menuItem)

        val destination = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(destination))
        }
        assertEquals(route, destination.captured.route)
    }

    @Test
    fun `default season is read from default season use case`() {
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2023
        initUnderTest()

        assertEquals(2023, underTest.outputs.defaultSeason)
    }

    @Test
    fun `app feature list list is populated`() {
        initUnderTest()

        val featureListLiveData = underTest.appFeatureItemsList.testObserve()
        featureListLiveData.assertListMatchesItem { it == MenuItem.Search }
        featureListLiveData.assertListMatchesItem { it == MenuItem.Settings }
        featureListLiveData.assertListMatchesItem { it == MenuItem.Contact }
    }

    @Test
    fun `season results list list is populated`() {
        initUnderTest()

        val featureListLiveData = underTest.seasonScreenItemsList.testObserve()
        featureListLiveData.assertListMatchesItem { it == MenuItem.Calendar }
        featureListLiveData.assertListMatchesItem { it == MenuItem.Constructors }
        featureListLiveData.assertListMatchesItem { it == MenuItem.Drivers }
    }

    @Test
    fun `app feature list contains rss if rss is enabled`() {
        every { mockRssRepository.enabled } returns true
        initUnderTest()

        val featureListLiveData = underTest.appFeatureItemsList.testObserve()
        featureListLiveData.assertListMatchesItem { it == MenuItem.RSS }
    }

    @Test
    fun `clicking a season updates currently selected season`() {
        initUnderTest()
        underTest.clickSeason(2020)
        underTest.currentlySelectedSeason.test {
            assertValue(2020)
        }
    }

    @Nested
    inner class ClickSeason {

        @Test
        fun `clicking a season updates season if currently selected is menu for calendar`() {

            initUnderTest()
            underTest.onDestinationChanged(mockNavController, NavDestination(Screen.Calendar.with(2019).route), null)
            underTest.currentlySelectedItem.testObserve()
            underTest.clickSeason(2020)

            val destination = slot<NavigationDestination>()
            verify {
                mockNavigator.navigate(capture(destination))
            }
            assertEquals(Screen.Calendar.with(2020).route, destination.captured.route)
        }

        @Test
        fun `clicking a season updates season if currently selected is menu for constructors`() {

            initUnderTest()
            underTest.currentlySelectedItem.testObserve()
            underTest.onDestinationChanged(mockNavController, NavDestination(Screen.Constructors.with(2019).route), null)
            underTest.clickSeason(2020)

            val destination = slot<NavigationDestination>()
            verify {
                mockNavigator.navigate(capture(destination))
            }
            assertEquals(Screen.Constructors.with(2020).route, destination.captured.route)
        }

        @Test
        fun `clicking a season updates season if currently selected is menu for drivers`() {
            initUnderTest()
            underTest.currentlySelectedItem.testObserve()
            underTest.clickSeason(2020)

            val destination = slot<NavigationDestination>()
            verify {
                mockNavigator.navigate(capture(destination))
            }
            assertEquals(Screen.Drivers.with(2020).route, destination.captured.route)
        }

        @Test
        fun `clicking a season does nothing if settings is already selected`() {

            initUnderTest()
            underTest.onDestinationChanged(mockNavController, NavDestination(Screen.Settings.All.route), null)
            underTest.currentlySelectedItem.testObserve()
            underTest.clickSeason(2020)

            verify(exactly = 0) {
                mockNavigator.navigate(any())
            }
        }
    }

    @Test
    fun `get all seasons returns season list`() {
        every { mockGetSeasonUseCase.get() } returns mapOf(
            2019 to Pair(IsFirst(true), IsLast(false)),
            2020 to Pair(IsFirst(true), IsLast(false)),
            2021 to Pair(IsFirst(false), IsLast(false))
        )

        initUnderTest()
        underTest.onDestinationChanged(mockNavController, NavDestination(Screen.Settings.All.route), null)
        val list = underTest.outputs.seasonsItemsList.testObserve()
        list.assertListMatchesItem(atIndex = 0) { it.id == "2019" && it.isSelected }
        list.assertListMatchesItem(atIndex = 0) { it.id == "2020" && !it.isSelected }
        list.assertListMatchesItem(atIndex = 0) { it.id == "2021" && !it.isSelected }

        underTest.inputs.clickSeason(2020)

        list.assertListMatchesItem(atIndex = 1) { it.id == "2019" && !it.isSelected }
        list.assertListMatchesItem(atIndex = 1) { it.id == "2020" && it.isSelected }
        list.assertListMatchesItem(atIndex = 1) { it.id == "2021" && !it.isSelected }
    }

    @Test
    fun `debug items come from debug nav component`() {
        val list: List<DebugMenuItem> = listOf(mockk())
        every { mockDebugNavigationComponent.getDebugMenuItems() } returns list

        initUnderTest()
        underTest.outputs.debugMenuItems.test {
            assertValue(list)
        }
    }

    @Test
    fun `debug item click forwards call to debug nav component`() {
        val item = DebugMenuItem(0, 0, "id")

        initUnderTest()
        underTest.inputs.clickDebug(item)
        verify {
            mockDebugNavigationComponent.navigateTo("id")
        }
    }
}