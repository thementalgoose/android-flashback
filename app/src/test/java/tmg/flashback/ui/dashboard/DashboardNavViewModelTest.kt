package tmg.flashback.ui.dashboard

import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Navigator
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.usecases.DashboardSyncUseCase
import tmg.testutils.BaseTest
import tmg.testutils.junit.toSealedClass

internal class DashboardNavViewModelTest: BaseTest() {

    private val mockRssRepository: RssRepository = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockCrashlyticsManager: CrashlyticsManager = mockk(relaxed = true)
    private val mockDashboardSyncUseCase: DashboardSyncUseCase = mockk(relaxed = true)
    private val mockDebugNavigationComponent: DebugNavigationComponent = mockk(relaxed = true)

    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockNavController: NavHostController = mockk(relaxed = true)

    private lateinit var underTest: DashboardNavViewModel

    private fun initUnderTest() {
        underTest = DashboardNavViewModel(
            rssRepository = mockRssRepository,
            navigator = mockNavigator,
            applicationNavigationComponent = mockApplicationNavigationComponent,
            crashlyticsManager = mockCrashlyticsManager,
            dashboardSyncUseCase = mockDashboardSyncUseCase,
            debugNavigationComponent = mockDebugNavigationComponent,
            ioDispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun `initial load of vm runs dashboard use case`() {
        every { mockNavigator.navController } returns mockNavController
        initUnderTest()
        coVerify {
            mockDashboardSyncUseCase.sync()
        }
        verify {
            mockCrashlyticsManager.log(any())
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
    fun `menu item gets updated when route is updated`(route: String, menuItemName: String?) = runTest(testDispatcher) {
        val menuItem = menuItemName?.toSealedClass(MenuItem::class)

        initUnderTest()
        underTest.onDestinationChanged(mockNavController, mockNavDestination(route), null)
        underTest.currentlySelectedItem.test {
            if (menuItem == null) {
                // Only the default nav menu is emitted
                assertEquals(MenuItem.Calendar, awaitItem())
            } else {
                assertEquals(menuItem, awaitItem())
            }
        }
    }

    @ParameterizedTest(name = "Route {0} means menu shown is {1}")
    @CsvSource(
        "results/races,true",
        "results/drivers,true",
        "results/constructors,true",
        "settings,true",
        "rss,true",
        "search,true",
        "drivers/,false",
        "settings/rss,false",
        "constructors/,false",
        "circuits/,false"
    )
    fun `menu is shown when route is updated`(route: String, showMenu: Boolean) = runTest(testDispatcher) {

        initUnderTest()

        underTest.onDestinationChanged(mockNavController, mockNavDestination(route), null)
        underTest.showMenu.test {
            assertEquals(showMenu, awaitItem())
        }
    }

    @ParameterizedTest(name = "Route {0} means showing bottom menu bar is {1}")
    @CsvSource(
        "results/races,true",
        "results/drivers,true",
        "results/constructors,true",
        "settings,false",
        "settings/rss,false",
        "rss,false",
        "search,false",
        "drivers/,false",
        "constructors/,false",
        "circuits/,false"
    )
    fun `menu bottom bar when route is updated`(route: String, showBottomBar: Boolean) = runTest(testDispatcher) {

        initUnderTest()
        underTest.onDestinationChanged(mockNavController, mockNavDestination(route), null)
        underTest.showBottomBar.test {
            assertEquals(showBottomBar, awaitItem())
        }
    }

    @ParameterizedTest(name = "Menu item {0} shows result {1}")
    @CsvSource(
        "Calendar,results/races",
        "Constructors,results/constructors",
        "Drivers,results/drivers",
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
    fun `app feature list list is populated`() = runTest(testDispatcher) {
        initUnderTest()

        underTest.appFeatureItemsList.test {
            val item = awaitItem()
            assertTrue(item.any { it == MenuItem.Search })
            assertTrue(item.any { it == MenuItem.Settings })
            assertTrue(item.any { it == MenuItem.Contact })
        }
    }

    @Test
    fun `season results list list is populated`() = runTest(testDispatcher) {
        initUnderTest()

        val featureListLiveData = underTest.seasonScreenItemsList.test {
            val item = awaitItem()
            assertTrue(item.any { it == MenuItem.Calendar })
            assertTrue(item.any { it == MenuItem.Constructors })
            assertTrue(item.any { it == MenuItem.Drivers })
        }
    }

    @Test
    fun `app feature list contains rss if rss is enabled`() = runTest(testDispatcher) {
        every { mockRssRepository.enabled } returns true
        initUnderTest()

        underTest.appFeatureItemsList.test {
            val item = awaitItem()
            assertTrue(item.any { it == MenuItem.RSS })
        }
    }

    @Test
    fun `debug items come from debug nav component`() = runTest(testDispatcher) {
        val list: List<DebugMenuItem> = listOf(mockk())
        every { mockDebugNavigationComponent.getDebugMenuItems() } returns list

        initUnderTest()
        underTest.outputs.debugMenuItems.test {
            assertEquals(list, awaitItem())
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

    private fun mockNavDestination(withRoute: String): NavDestination = mockk(relaxed = true) {
        every { route } returns withRoute
    }
}