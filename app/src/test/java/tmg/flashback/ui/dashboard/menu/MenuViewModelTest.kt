package tmg.flashback.ui.dashboard.menu

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.R
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.eastereggs.usecases.IsSnowEnabledUseCase
import tmg.flashback.eastereggs.usecases.IsMenuIconEnabledUseCase
import tmg.flashback.rss.RssNavigationComponent
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.ui.dashboard.compact.menu.MenuItems
import tmg.flashback.ui.dashboard.compact.menu.MenuViewModel
import tmg.flashback.ui.dashboard.expectedMenuItems
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.flashback.usecases.GetSeasonsUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertListDoesNotMatchItem
import tmg.testutils.livedata.assertListMatchesItem
import tmg.testutils.livedata.test

internal class MenuViewModelTest: BaseTest() {

    private val mockGetSeasonsUseCase: GetSeasonsUseCase = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)
    private val mockChangeNightModeUseCase: ChangeNightModeUseCase = mockk(relaxed = true)
    private val mockStyleManager: StyleManager = mockk(relaxed = true)
    private val mockRssNavigationComponent: RssNavigationComponent = mockk(relaxed = true)
    private val mockNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)
    private val mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)
    private val mockDebugNavigationComponent: DebugNavigationComponent = mockk(relaxed = true)
    private val mockIsSnowEnabledUseCase: IsSnowEnabledUseCase = mockk(relaxed = true)
    private val mockIsMenuIconEnabledUseCase: IsMenuIconEnabledUseCase = mockk(relaxed = true)

    private lateinit var underTest: MenuViewModel

    private fun initUnderTest() {
        underTest = MenuViewModel(
            mockGetSeasonsUseCase,
            mockBuildConfigManager,
            mockPermissionManager,
            mockPermissionRepository,
            mockNotificationRepository,
            mockDefaultSeasonUseCase,
            mockChangeNightModeUseCase,
            mockStyleManager,
            mockRssNavigationComponent,
            mockNavigationComponent,
            mockStatsNavigationComponent,
            mockDebugNavigationComponent,
            mockIsSnowEnabledUseCase,
            mockIsMenuIconEnabledUseCase
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockStyleManager.isDayMode } returns true
        every { mockDefaultSeasonUseCase.defaultSeason } returns 2022
        every { mockNotificationRepository.seenNotificationOnboarding } returns false
        every { mockGetSeasonsUseCase.get(2022) } returns expectedMenuItems
        every { mockDebugNavigationComponent.getDebugMenuItems() } returns emptyList()
    }

    @Test
    fun `valentines day easter egg is true when easter egg is active`() {
        every { mockIsMenuIconEnabledUseCase.invoke() } returns MenuIcons.EASTER
        initUnderTest()
        assertEquals(MenuIcons.EASTER, underTest.outputs.overrideMenuKey)
    }


    @Test
    fun `snow easter egg is true when easter egg is active`() {
        every { mockIsSnowEnabledUseCase.invoke() } returns true
        initUnderTest()
        assertTrue(underTest.outputs.isSnowEasterEggEnabled)
    }

    @Test
    fun `initial load shows expected links`() {
        initUnderTest()

        underTest.outputs.links.test {
            assertValue(listOf(
                MenuItems.Button.Search,
                MenuItems.Button.Rss,
                MenuItems.Button.Settings,
                MenuItems.Button.Contact,
                MenuItems.Divider("z"),
                MenuItems.Button.Custom(R.string.debug_menu_debug, R.drawable.debug_list_debug, "debug"),
                MenuItems.Button.Custom(R.string.debug_menu_styleguide, R.drawable.debug_list_styleguide, "styleguide"),
                MenuItems.Button.Custom(R.string.debug_menu_ads_config, R.drawable.debug_list_adverts, "adverts"),
                MenuItems.Divider("a"),
                MenuItems.Toggle.DarkMode(_isEnabled = false),
                MenuItems.Divider("b"),
                MenuItems.Feature.Notifications,
                MenuItems.Divider("c")
            ))
        }
    }

    @Test
    fun `initial load reports app version name`() {
        every { mockBuildConfigManager.versionName } returns "version-name"
        initUnderTest()

        underTest.outputs.appVersion.test {
            assertValue("version-name")
        }
    }

    @Test
    fun `initial load with runtime notifications, not seen and not enabled shows runtime feature`() {
        every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns true
        every { mockNotificationRepository.seenRuntimeNotifications } returns false
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

        initUnderTest()

        underTest.outputs.links.test {
            assertListMatchesItem {
                it is MenuItems.Feature.RuntimeNotifications
            }
            assertListDoesNotMatchItem {
                it is MenuItems.Feature.Notifications
            }
        }
    }

    @Test
    fun `initial load hides runtime notifications if seen before `() {
        every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns true
        every { mockNotificationRepository.seenRuntimeNotifications } returns true
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns false

        initUnderTest()

        underTest.outputs.links.test {
            assertListDoesNotMatchItem {
                it is MenuItems.Feature.RuntimeNotifications
            }
        }
    }

    @Test
    fun `initial load hides runtime notifications if notifications are enabled `() {
        every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns true
        every { mockNotificationRepository.seenRuntimeNotifications } returns false
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true

        initUnderTest()

        underTest.outputs.links.test {
            assertListDoesNotMatchItem {
                it is MenuItems.Feature.RuntimeNotifications
            }
        }
    }

    @Test
    fun `initial load with notifications, not seen and not enabled shows runtime feature`() {
        every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns false
        every { mockNotificationRepository.seenNotificationOnboarding } returns false

        initUnderTest()

        underTest.outputs.links.test {
            assertListMatchesItem {
                it is MenuItems.Feature.Notifications
            }
            assertListDoesNotMatchItem {
                it is MenuItems.Feature.RuntimeNotifications
            }
        }
    }

    @Test
    fun `initial load hides notifications if notifications seen before `() {
        every { mockBuildConfigManager.isRuntimeNotificationsSupported } returns false
        every { mockNotificationRepository.seenNotificationOnboarding } returns true

        initUnderTest()

        underTest.outputs.links.test {
            assertListDoesNotMatchItem {
                it is MenuItems.Feature.Notifications
            }
        }
    }

    @Test
    fun `initial load shows expected links with dark mode enabled`() {
        every { mockStyleManager.isDayMode } returns false
        initUnderTest()

        underTest.outputs.links.test {
            assertValue(listOf(
                MenuItems.Button.Search,
                MenuItems.Button.Rss,
                MenuItems.Button.Settings,
                MenuItems.Button.Contact,
                MenuItems.Divider("z"),
                MenuItems.Button.Custom(R.string.debug_menu_debug, R.drawable.debug_list_debug, "debug"),
                MenuItems.Button.Custom(R.string.debug_menu_styleguide, R.drawable.debug_list_styleguide, "styleguide"),
                MenuItems.Button.Custom(R.string.debug_menu_ads_config, R.drawable.debug_list_adverts, "adverts"),
                MenuItems.Divider("a"),
                MenuItems.Toggle.DarkMode(_isEnabled = true),
                MenuItems.Divider("b"),
                MenuItems.Feature.Notifications,
                MenuItems.Divider("c")
            ))
        }
    }

    @Test
    fun `initial load shows expected seasons from supported seasons`() {
        initUnderTest()

        underTest.outputs.season.test {
            assertValue(expectedMenuItems)
        }
    }



    @Test
    fun `click button contact calls about this app`() {
        initUnderTest()

        underTest.inputs.clickButton(MenuItems.Button.Contact)

        verify {
            mockNavigationComponent.aboutApp()
        }
    }

    @Test
    fun `click button search calls search`() {
        initUnderTest()

        underTest.inputs.clickButton(MenuItems.Button.Search)

        verify {
            mockStatsNavigationComponent.search()
        }
    }

    @Test
    fun `click button rss calls rss`() {
        initUnderTest()

        underTest.inputs.clickButton(MenuItems.Button.Rss)

        verify {
            mockRssNavigationComponent.rss()
        }
    }

    @Test
    fun `click button settings calls settings`() {
        initUnderTest()

        underTest.inputs.clickButton(MenuItems.Button.Settings)

        verify {
            mockNavigationComponent.settings()
        }
    }



    @Test
    fun `click toggle dark mode sets night mode NIGHT if toggle disabled previously`() {
        initUnderTest()

        underTest.inputs.clickToggle(MenuItems.Toggle.DarkMode(_isEnabled = false))

        verify {
            mockChangeNightModeUseCase.setNightMode(NightMode.NIGHT)
        }
    }

    @Test
    fun `click toggle dark mode sets night mode DAY if toggle enabled previously`() {
        initUnderTest()

        underTest.inputs.clickToggle(MenuItems.Toggle.DarkMode(_isEnabled = true))

        verify {
            mockChangeNightModeUseCase.setNightMode(NightMode.DAY)
        }
    }



    @Test
    fun `click feature notifications calls stats navigator`() {
        initUnderTest()

        underTest.inputs.clickFeature(MenuItems.Feature.Notifications)

        verify {
            mockStatsNavigationComponent.featureNotificationOnboarding()
            mockNotificationRepository.seenNotificationOnboarding = true
        }
    }

    @Test
    fun `click feature runtime notifications calls stats navigator`() = coroutineTest {
        initUnderTest()
        every { mockPermissionRepository.isRuntimeNotificationsEnabled } returns true

        val completableDeferred: CompletableDeferred<Boolean> = CompletableDeferred()
        every { mockPermissionManager.requestPermission(any()) } returns completableDeferred

        underTest.inputs.clickFeature(MenuItems.Feature.RuntimeNotifications)
        completableDeferred.complete(true)

        verify {
            mockPermissionManager.requestPermission(any())
            mockNotificationRepository.seenRuntimeNotifications = true
            mockPermissionRepository.isRuntimeNotificationsEnabled
            mockStatsNavigationComponent.featureNotificationOnboarding()
        }
    }
}