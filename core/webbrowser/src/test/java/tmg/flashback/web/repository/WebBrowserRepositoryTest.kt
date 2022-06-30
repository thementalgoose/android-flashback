package tmg.flashback.web.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class WebBrowserRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: WebBrowserRepository

    private fun initUnderTest() {
        underTest = WebBrowserRepository(mockPreferenceManager)
    }

    @Test
    fun `open in external reads from preferences`() {
        every { mockPreferenceManager.getBoolean(keyOpenInExternal, true) } returns true
        initUnderTest()
        assertTrue(underTest.openInExternal)
        verify {
            mockPreferenceManager.getBoolean(keyOpenInExternal, true)
        }
    }

    @Test
    fun `open in external writes to preferences`() {
        initUnderTest()
        underTest.openInExternal = true
        verify {
            mockPreferenceManager.save(keyOpenInExternal, true)
        }
    }

    @Test
    fun `enable javascript reads from preferences`() {
        every { mockPreferenceManager.getBoolean(keyEnableJavascript, true) } returns true
        initUnderTest()
        assertTrue(underTest.enableJavascript)
        verify {
            mockPreferenceManager.getBoolean(keyEnableJavascript, true)
        }
    }

    @Test
    fun `enable javascript writes to preferences`() {
        initUnderTest()
        underTest.enableJavascript = true
        verify {
            mockPreferenceManager.save(keyEnableJavascript, true)
        }
    }

    companion object {
        private const val keyOpenInExternal = "WEB_BROWSER_OPEN_IN_EXTERNAL"
        private const val keyEnableJavascript = "WEB_BROWSER_ENABLE_JAVASCRIPT"
    }
}