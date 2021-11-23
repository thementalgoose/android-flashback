package tmg.flashback.ui.settings

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.testutils.assertExpectedOrder
import tmg.flashback.testutils.findPref
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAllViewModelTest: BaseTest() {

    private var mockRssController: RSSController = mockk(relaxed = true)

    private lateinit var sut: SettingsAllViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockRssController.enabled } returns true
    }

    private fun initSUT() {
        sut = SettingsAllViewModel(mockRssController)
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
                Pair(R.string.settings_all_about, R.string.settings_all_about_subtitle)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking appearance fires open appearance event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_appearance))
        sut.outputs.openAppearance.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking statistics fires open statistics event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_home))
        sut.outputs.openHome.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking rss fires open rss event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_rss))
        sut.outputs.openRss.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking notifications fires open notifications event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_notifications))
        sut.outputs.openNotifications.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking support fires open support event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_support))
        sut.outputs.openSupport.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking about fires open appearance event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_all_about))
        sut.outputs.openAbout.test {
            assertEventFired()
        }
    }
}