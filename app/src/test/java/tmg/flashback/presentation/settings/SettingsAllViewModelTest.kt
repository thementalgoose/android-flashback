package tmg.flashback.presentation.settings

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.device.AppPermissions
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.PermissionRepository
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.repository.ThemeRepository
import tmg.testutils.BaseTest

internal class SettingsAllViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockRSSRepository: RssRepository = mockk(relaxed = true)
    private val mockPermissionRepository: PermissionRepository = mockk(relaxed = true)
    private val mockPermissionManager: PermissionManager = mockk(relaxed = true)
    private val mockApplicationNavigationComponent: ApplicationNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: SettingsAllViewModel

    private fun initUnderTest() {
        underTest = SettingsAllViewModel(
            themeRepository = mockThemeRepository,
            buildConfig = mockBuildConfigManager,
            adsRepository = mockAdsRepository,
            rssRepository = mockRSSRepository,
            permissionRepository = mockPermissionRepository,
            permissionManager = mockPermissionManager,
            applicationNavigationComponent = mockApplicationNavigationComponent,
        )
    }

    @Test
    fun `theme is disabled if less than android 12`() = runTest(testDispatcher) {
        every { mockBuildConfigManager.isMonetThemeSupported } returns false

        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(false, awaitItem().themeEnabled)
        }
    }

    @Test
    fun `theme is enabled if enabled in config and android 12 or more`() = runTest(testDispatcher) {
        every { mockBuildConfigManager.isMonetThemeSupported } returns true

        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(true, awaitItem().themeEnabled)
        }
    }

    @Test
    fun `ads is enabled if user pref enabled`() = runTest(testDispatcher) {
        every { mockAdsRepository.allowUserConfig } returns true

        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(true, awaitItem().adsEnabled)
        }
    }

    @Test
    fun `ads is disabled if user pref disabled`() = runTest(testDispatcher) {
        every { mockAdsRepository.allowUserConfig } returns false

        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(false, awaitItem().adsEnabled)
        }
    }

    @Test
    fun `rss is enabled if rss in config is enabled`() = runTest(testDispatcher) {
        every { mockRSSRepository.enabled } returns true

        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(true, awaitItem().rssEnabled)
        }
    }

    @Test
    fun `rss is disabled if rss in config is disabled`() = runTest(testDispatcher) {
        every { mockRSSRepository.enabled } returns false

        initUnderTest()
        underTest.outputs.uiState.test {
            assertEquals(false, awaitItem().rssEnabled)
        }
    }

    @Test
    fun `clicking dark mode opens dark mode`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Theme.darkMode)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.DARK_MODE, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }

    @Test
    fun `clicking theme pref opens theme`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Theme.theme)


        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.THEME, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }

    @Test
    fun `clicking layout opens layout`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Data.layout)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.LAYOUT, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }

    @Test
    fun `clicking weather opens weather`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Data.weather)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.WEATHER, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }

    @Test
    fun `clicking configure opens configure`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.RSS.rss)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.RSS_CONFIGURE, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }

    @Test
    fun `clicking in app browser opens in app browser`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Web.inAppBrowser)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.WEB_BROWSER, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }


    @Test
    fun `clicking upcoming notifications opens upcoming notifications period`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Notifications.notificationUpcomingNotice)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.NOTIFICATIONS_TIMER, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }



    @Test
    fun `clicking results available notifications runs permission dialog and opens settings`() = runTest {
        val deferrable = CompletableDeferred<Map<String, Boolean>>()
        every { mockPermissionManager.requestPermission(any()) } returns deferrable
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Notifications.notificationResults)
        verify {
            mockPermissionManager.requestPermission(AppPermissions.RuntimeNotifications)
        }

        deferrable.complete(mapOf())
        verify {
            mockApplicationNavigationComponent.appSettingsNotifications()
        }
    }


    @Test
    fun `clicking upcoming available notifications runs permission dialog and opens settings`() = runTest {
        val deferrable = CompletableDeferred<Map<String, Boolean>>()
        every { mockPermissionManager.requestPermission(any()) } returns deferrable
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Notifications.notificationUpcoming)
        verify {
            mockPermissionManager.requestPermission(AppPermissions.RuntimeNotifications)
        }

        deferrable.complete(mapOf())
        verify {
            mockApplicationNavigationComponent.appSettingsNotifications()
        }
    }

    @Test
    fun `clicking widgets opens widgets`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Widgets.widgets)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.WIDGETS, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }

    @Test
    fun `clicking ads opens ads`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Ads.ads)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.ADS, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }

    @Test
    fun `clicking privacy opens privacy`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Other.privacy)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.PRIVACY, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }

    @Test
    fun `clicking about opens about`() = runTest {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Other.about)

        underTest.outputs.uiState.test {
            assertEquals(SettingsAllViewModel.SettingsScreen.ABOUT, awaitItem().selectedSubScreen)

            underTest.inputs.back()
            assertEquals(null, awaitItem().selectedSubScreen)
        }
    }
}