package tmg.flashback.ui.settings

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.rss.contract.RSSConfigure
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.settings.appearance.AppearanceNavigationComponent
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class SettingsAllViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private val mockRSSRepository: RssRepository = mockk(relaxed = true)
    private val mockNavigator: Navigator = mockk(relaxed = true)
    private val mockAppearanceNavigationComponent: AppearanceNavigationComponent = mockk(relaxed = true)

    private lateinit var underTest: SettingsAllViewModel

    private fun initUnderTest() {
        underTest = SettingsAllViewModel(
            themeRepository = mockThemeRepository,
            buildConfig = mockBuildConfigManager,
            adsRepository = mockAdsRepository,
            rssRepository = mockRSSRepository,
            navigator = mockNavigator,
            appearanceNavigationComponent = mockAppearanceNavigationComponent,
        )
    }

    @Test
    fun `theme is disabled if theme picker is disabled`() {
        every { mockThemeRepository.enableThemePicker } returns false
        every { mockBuildConfigManager.isMonetThemeSupported } returns true

        initUnderTest()
        underTest.outputs.isThemeEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `theme is disabled if less than android 12`() {
        every { mockThemeRepository.enableThemePicker } returns true
        every { mockBuildConfigManager.isMonetThemeSupported } returns false

        initUnderTest()
        underTest.outputs.isThemeEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `theme is enabled if enabled in config and android 12 or more`() {
        every { mockThemeRepository.enableThemePicker } returns true
        every { mockBuildConfigManager.isMonetThemeSupported } returns true

        initUnderTest()
        underTest.outputs.isThemeEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `ads is enabled if user pref enabled`() {
        every { mockAdsRepository.allowUserConfig } returns true

        initUnderTest()
        underTest.outputs.isAdsEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `ads is disabled if user pref disabled`() {
        every { mockAdsRepository.allowUserConfig } returns false

        initUnderTest()
        underTest.outputs.isAdsEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `rss is enabled if rss in config is enabled`() {
        every { mockRSSRepository.enabled } returns true

        initUnderTest()
        underTest.outputs.isRSSEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `rss is disabled if rss in config is disabled`() {
        every { mockRSSRepository.enabled } returns false

        initUnderTest()
        underTest.outputs.isRSSEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `clicking dark mode opens dark mode`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Theme.darkMode)

        verify {
            mockAppearanceNavigationComponent.nightModeDialog()
        }
    }

    @Test
    fun `clicking theme pref opens theme`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Theme.theme)

        verify {
            mockAppearanceNavigationComponent.themeDialog()
        }
    }

    @Test
    fun `clicking layout opens layout`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Layout.home)

        val slot = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(slot))
        }
        assertEquals(Screen.Settings.Home.route, slot.captured.route)
    }

    @Test
    fun `clicking configure opens configure`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.RSS.rss)

        val slot = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(slot))
        }
        assertEquals(Screen.Settings.RSSConfigure.route, slot.captured.route)
    }

    @Test
    fun `clicking in app browser opens in app browser`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Web.inAppBrowser)

        val slot = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(slot))
        }
        assertEquals(Screen.Settings.Web.route, slot.captured.route)
    }

    @Test
    fun `clicking upcoming notifications opens upcoming notifications`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Notifications.notificationUpcoming)

        val slot = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(slot))
        }
        assertEquals(Screen.Settings.NotificationsUpcoming.route, slot.captured.route)
    }

    @Test
    fun `clicking results notifications opens results notifications`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Notifications.notificationResults)

        val slot = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(slot))
        }
        assertEquals(Screen.Settings.NotificationsResults.route, slot.captured.route)
    }

    @Test
    fun `clicking ads opens ads`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Ads.ads)

        val slot = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(slot))
        }
        assertEquals(Screen.Settings.Ads.route, slot.captured.route)
    }

    @Test
    fun `clicking privacy opens privacy`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Other.privacy)

        val slot = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(slot))
        }
        assertEquals(Screen.Settings.Privacy.route, slot.captured.route)
    }

    @Test
    fun `clicking about opens about`() {
        initUnderTest()
        underTest.inputs.itemClicked(Settings.Other.about)

        val slot = slot<NavigationDestination>()
        verify {
            mockNavigator.navigate(capture(slot))
        }
        assertEquals(Screen.Settings.About.route, slot.captured.route)
    }
}