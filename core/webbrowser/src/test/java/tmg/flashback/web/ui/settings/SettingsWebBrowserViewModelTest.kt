package tmg.flashback.web.ui.settings

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.web.R
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.flashback.web.testutils.assertExpectedOrder
import tmg.flashback.web.testutils.findSwitch
import tmg.testutils.BaseTest

internal class SettingsWebBrowserViewModelTest: BaseTest() {

    private val mockWebBrowserRepository: WebBrowserRepository = mockk(relaxed = true)

    private lateinit var sut: SettingsWebBrowserViewModel

    private fun initSUT() {
        sut = SettingsWebBrowserViewModel(mockWebBrowserRepository)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockWebBrowserRepository.openInExternal } returns true
        every { mockWebBrowserRepository.enableJavascript } returns true
    }

    @Test
    fun `initial model list is expected`() = coroutineTest {
        initSUT()
        val expected = listOf(
            Pair(R.string.settings_web_browser_title, null),
            Pair(R.string.settings_web_browser_in_app_title, R.string.settings_web_browser_in_app_description),
            Pair(R.string.settings_web_browser_javascript_title, R.string.settings_web_browser_javascript_description)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking pref for open in external updates repository`() = coroutineTest {
        every { mockWebBrowserRepository.openInExternal } returns true
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_web_browser_in_app_title), true)
        verify {
            mockWebBrowserRepository.openInExternal = true
        }
    }

    @Test
    fun `clicking pref for enable javascript updates repository`() = coroutineTest {
        every { mockWebBrowserRepository.enableJavascript } returns true
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_web_browser_javascript_title), true)
        verify {
            mockWebBrowserRepository.enableJavascript = true
        }
    }
}