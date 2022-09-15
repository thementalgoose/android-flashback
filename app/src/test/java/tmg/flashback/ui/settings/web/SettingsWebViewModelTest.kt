package tmg.flashback.ui.settings.web

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.ui.settings.Settings
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsWebViewModelTest: BaseTest() {

    private val mockWebBrowserRepository: WebBrowserRepository = mockk(relaxed = true)

    private lateinit var underTest: SettingsWebViewModel

    private fun initUnderTest() {
        underTest = SettingsWebViewModel(
            webBrowserRepository = mockWebBrowserRepository
        )
    }

    @Test
    fun `browser is enabled when open in external enabled`() {
        every { mockWebBrowserRepository.openInExternal } returns true

        initUnderTest()
        underTest.outputs.enable.test {
            assertValue(true)
        }
    }

    @Test
    fun `browser is disabled when open in external disabled`() {
        every { mockWebBrowserRepository.openInExternal } returns false

        initUnderTest()
        underTest.outputs.enable.test {
            assertValue(false)
        }
    }

    @Test
    fun `javascript is enabled when enable javascript is true`() {
        every { mockWebBrowserRepository.enableJavascript } returns true

        initUnderTest()
        underTest.outputs.enableJavascript.test {
            assertValue(true)
        }
    }

    @Test
    fun `javascript is disabled when enable javascript is false`() {
        every { mockWebBrowserRepository.enableJavascript } returns false

        initUnderTest()
        underTest.outputs.enableJavascript.test {
            assertValue(false)
        }
    }

    @Test
    fun `clicking enable updates pref and updates value`() {
        every { mockWebBrowserRepository.openInExternal } returns false

        initUnderTest()
        val observer = underTest.outputs.enable.testObserve()
        underTest.inputs.prefClicked(Settings.Web.enable(true))

        verify {
            mockWebBrowserRepository.openInExternal = true
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `clicking enable javascript updates pref and updates value`() {
        every { mockWebBrowserRepository.enableJavascript } returns false

        initUnderTest()
        val observer = underTest.outputs.enableJavascript.testObserve()
        underTest.inputs.prefClicked(Settings.Web.javascript(true))

        verify {
            mockWebBrowserRepository.enableJavascript = true
        }
        observer.assertEmittedCount(2)
    }
}