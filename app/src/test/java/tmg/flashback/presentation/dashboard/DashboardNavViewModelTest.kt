package tmg.flashback.presentation.dashboard

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
import tmg.flashback.sandbox.SandboxNavigationComponent
import tmg.flashback.sandbox.model.SandboxMenuItem
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Navigator
import tmg.flashback.reactiongame.usecases.IsReactionGameEnabledUseCase
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.sandbox.usecases.GetSandboxMenuItemsUseCase
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.CurrentSeasonHolder
import tmg.flashback.usecases.DashboardSyncUseCase
import tmg.testutils.BaseTest
import tmg.testutils.junit.toSealedClass

internal class DashboardNavViewModelTest: BaseTest() {

    private val mockRssRepository: RssRepository = mockk(relaxed = true)
    private val mockIsReactionGameEnabledUseCase: IsReactionGameEnabledUseCase = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockCrashlyticsManager: CrashlyticsManager = mockk(relaxed = true)
    private val mockDashboardSyncUseCase: DashboardSyncUseCase = mockk(relaxed = true)
    private val mockGetSandboxMenuItemsUseCase: GetSandboxMenuItemsUseCase = mockk(relaxed = true)
    private val mockSandboxNavigationComponent: SandboxNavigationComponent = mockk(relaxed = true)
    private val mockCurrentSeasonHolder: CurrentSeasonHolder = mockk(relaxed = true)

    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockNavController: NavHostController = mockk(relaxed = true)

    private lateinit var underTest: DashboardNavViewModel

    private fun initUnderTest() {
        underTest = DashboardNavViewModel(
            rssRepository = mockRssRepository,
            isReactionGameEnabledUseCase = mockIsReactionGameEnabledUseCase,
            navigator = mockNavigator,
            applicationNavigationComponent = mockApplicationNavigationComponent,
            crashlyticsManager = mockCrashlyticsManager,
            dashboardSyncUseCase = mockDashboardSyncUseCase,
            sandboxNavigationComponent = mockSandboxNavigationComponent,
            getSandboxMenuItemsUseCase = mockGetSandboxMenuItemsUseCase,
            currentSeasonHolder = mockCurrentSeasonHolder,
            ioDispatcher = Dispatchers.Unconfined
        )
    }
    @Test
    fun `initial load of vm runs dashboard use case`() {
        every { mockNavigator.navController } returns mockNavController
        initUnderTest()
        coVerify {
            mockDashboardSyncUseCase.sync()
            mockCurrentSeasonHolder.refresh()
        }
        verify {
            mockCrashlyticsManager.log(any())
        }
    }

    @ParameterizedTest(name = "Route {0} means currently selected menu item is updated to {1}")
    @CsvSource(
        "results/calendar,Calendar",
        "results/drivers,Drivers",
        "results/constructors,Constructors",
        "settings,Settings",
        "settings/rss,Settings",
        "rss,RSS",
        "search,Search",
        "reaction,Reaction",
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
        "reaction,true",
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
        "reaction,false",
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
        "Reaction,reaction",
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

    @ParameterizedTest(name = "Navigation in root locks {0} route showMenu to {1} ")
    @CsvSource(
        "results/races",
        "results/constructors",
        "results/drivers",
        "rss",
        "settings",
        "search"
    )
    fun `navigation in root applies properly to route`(route: String) = runTest(testDispatcher) {

        initUnderTest()

        underTest.onDestinationChanged(mockNavController, mockNavDestination(route), null)
        underTest.showMenu.test {
            assertEquals(true, awaitItem())

            underTest.navigationInRoot(route, false)
            assertEquals(false, awaitItem())

            underTest.navigationInRoot(route, true)
            assertEquals(true, awaitItem())
        }

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
    fun `app feature list contains reaction game when enabled`() = runTest(testDispatcher) {
        every { mockIsReactionGameEnabledUseCase.invoke() } returns true
        initUnderTest()

        underTest.appFeatureItemsList.test {
            val item = awaitItem()
            assertTrue(item.any { it == MenuItem.Reaction })
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
        val list: List<SandboxMenuItem> = listOf(mockk())
        every { mockGetSandboxMenuItemsUseCase.invoke() } returns list

        initUnderTest()
        underTest.outputs.sandboxMenuItems.test {
            assertEquals(list, awaitItem())
        }
    }

    @Test
    fun `debug item click forwards call to debug nav component`() {
        val item = SandboxMenuItem(0, 0, "id")

        initUnderTest()
        underTest.inputs.clickSandboxOption(item)
        verify {
            mockSandboxNavigationComponent.navigateTo("id")
        }
    }

    private fun mockNavDestination(withRoute: String): NavDestination = mockk(relaxed = true) {
        every { route } returns withRoute
    }
}