package tmg.flashback.ui.settings.web

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.ui.settings.Settings
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.testutils.BaseTest

internal class SettingsWebViewModelTest: BaseTest() {

    private val mockWebBrowserRepository: WebBrowserRepository = mockk(relaxed = true)

    private lateinit var underTest: SettingsWebViewModel

    private fun initUnderTest() {
        underTest = SettingsWebViewModel(
            webBrowserRepository = mockWebBrowserRepository
        )
    }

    @Test
    fun `browser is enabled when open in external enabled`() = runTest {
        every { mockWebBrowserRepository.openInExternal } returns false

        initUnderTest()
        underTest.outputs.enable.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `browser is disabled when open in external disabled`() = runTest {
        every { mockWebBrowserRepository.openInExternal } returns true

        initUnderTest()
        underTest.outputs.enable.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `javascript is enabled when enable javascript is true`() = runTest {
        every { mockWebBrowserRepository.enableJavascript } returns true

        initUnderTest()
        underTest.outputs.enableJavascript.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `javascript is disabled when enable javascript is false`() = runTest {
        every { mockWebBrowserRepository.enableJavascript } returns false

        initUnderTest()
        underTest.outputs.enableJavascript.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `clicking enable updates pref and updates value`() = runTest {
        every { mockWebBrowserRepository.openInExternal } returns false

        initUnderTest()
        underTest.outputs.enable.test {
            assertEquals(true, awaitItem())
        }
        underTest.inputs.prefClicked(Settings.Web.enable(true))

        verify {
            mockWebBrowserRepository.openInExternal = true
        }
        underTest.outputs.enable.test { awaitItem() }
    }

    @Test
    fun `clicking enable javascript updates pref and updates value`() = runTest {
        every { mockWebBrowserRepository.enableJavascript } returns false

        initUnderTest()
        underTest.outputs.enable.test {
            assertEquals(true, awaitItem())
        }
        underTest.inputs.prefClicked(Settings.Web.javascript(true))

        underTest.outputs.enable.test { awaitItem() }
        verify {
            mockWebBrowserRepository.enableJavascript = true
        }
    }
}