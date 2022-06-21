package tmg.flashback.ui.settings

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.RssNavigationComponent
import tmg.flashback.ads.AdsNavigationComponent
import tmg.flashback.ads.repository.AdsRepository
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.settings.SettingsNavigationComponent
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.testutils.assertExpectedOrder
import tmg.flashback.testutils.findPref
import tmg.testutils.BaseTest

internal class SettingsAllViewModelTest: BaseTest() {

    private var mockRssController: RSSController = mockk(relaxed = true)
    private var mockAdsRepository: AdsRepository = mockk(relaxed = true)
    private var mockRssNavigationController: RssNavigationComponent = mockk(relaxed = true)
    private var mockSettingsNavigationComponent: SettingsNavigationComponent = mockk(relaxed = true)
    private var mockStatsNavigationComponent: StatsNavigationComponent = mockk(relaxed = true)
    private var mockAdsNavigationComponent: AdsNavigationComponent = mockk(relaxed = true)

    private lateinit var sut: SettingsAllViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockRssController.enabled } returns true
        every { mockAdsRepository.allowUserConfig } returns true
    }

    private fun initSUT() {
        sut = SettingsAllViewModel(
            mockRssController,
            mockAdsRepository,
            mockRssNavigationController,
            mockSettingsNavigationComponent,
            mockStatsNavigationComponent,
            mockAdsNavigationComponent
        )
    }

    @Test
    fun `init loads all categories with rss feature enabled`() {
        initSUT()
        val expected = listOf(
                Pair(R.string.settings_title, null),
                Pair(R.string.settings_all_appearance, R.string.settings_all_appearance_subtitle),
                Pair(R.string.settings_all_home, R.string.settings_all_home_subtitle),
                Pair(R.string.settings_all_rss, R.string.settings_all_rss_subtitle),
                Pair(R.string.settings_all_notifications, R.string.settings_all_notifications_subtitle),
                Pair(R.string.settings_all_support, R.string.settings_all_support_subtitle),
                Pair(R.string.settings_all_ads, R.string.settings_all_ads_subtitle),
                Pair(R.string.settings_all_about, R.string.settings_all_about_subtitle)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `init loads all categories with rss feature disabled`() {
        every { mockRssController.enabled } returns false
        initSUT()
        val expected = listOf(
            Pair(R.string.settings_title, null),
            Pair(R.string.settings_all_appearance, R.string.settings_all_appearance_subtitle),
            Pair(R.string.settings_all_home, R.string.settings_all_home_subtitle),
            Pair(R.string.settings_all_notifications, R.string.settings_all_notifications_subtitle),
            Pair(R.string.settings_all_support, R.string.settings_all_support_subtitle),
            Pair(R.string.settings_all_ads, R.string.settings_all_ads_subtitle),
            Pair(R.string.settings_all_about, R.string.settings_all_about_subtitle)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `init loads all categories with ads feature disabled`() {
        every { mockAdsRepository.allowUserConfig } returns false
        initSUT()
        val expected = listOf(
            Pair(R.string.settings_title, null),
            Pair(R.string.settings_all_appearance, R.string.settings_all_appearance_subtitle),
            Pair(R.string.settings_all_home, R.string.settings_all_home_subtitle),
            Pair(R.string.settings_all_rss, R.string.settings_all_rss_subtitle),
            Pair(R.string.settings_all_notifications, R.string.settings_all_notifications_subtitle),
            Pair(R.string.settings_all_support, R.string.settings_all_support_subtitle),
            Pair(R.string.settings_all_about, R.string.settings_all_about_subtitle)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking appearance fires open appearance event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_appearance))
        verify {
            mockSettingsNavigationComponent.settingsAppearance()
        }
    }

    @Test
    fun `clicking statistics fires open statistics event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_home))
        verify {
            mockStatsNavigationComponent.settingsHome()
        }
    }

    @Test
    fun `clicking rss fires open rss event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_rss))
        verify {
            mockRssNavigationController.settingsRSS()
        }
    }

    @Test
    fun `clicking notifications fires open notifications event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_notifications))
        verify {
            mockStatsNavigationComponent.settingsNotifications()
        }
    }

    @Test
    fun `clicking support fires open support event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_support))
        verify {
            mockSettingsNavigationComponent.settingsSupport()
        }
    }

    @Test
    fun `clicking ads fires open ads event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_ads))
        verify {
            mockAdsNavigationComponent.settingsAds()
        }
    }

    @Test
    fun `clicking about fires open appearance event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_about))

        verify {
            mockSettingsNavigationComponent.settingsAbout()
        }
    }
}