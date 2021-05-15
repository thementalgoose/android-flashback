package tmg.flashback.rss.repo

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.core.prefs.manager.PreferenceManager

internal class RSSRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: RSSRepository

    private fun initSUT() {
        sut = RSSRepository(mockPreferenceManager)
    }

    //region rssUrls

    @Test
    fun `get rss urls reads value from preferences repository`() {
        every { mockPreferenceManager.getSet(keyRssList, emptySet()) } returns mutableSetOf("test")
        initSUT()

        assertEquals(mutableSetOf("test"), sut.rssUrls)
        verify {
            mockPreferenceManager.getSet(keyRssList, emptySet())
        }
    }

    @Test
    fun `get rss urls saves value to shared prefs repository`() {
        initSUT()

        sut.rssUrls = setOf("hey")
        verify {
            mockPreferenceManager.save(keyRssList, setOf("hey"))
        }
    }

    //endregion

    //region inAppEnableJavascript

    @Test
    fun `in app enable javascript reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyInAppEnableJavascript, true) } returns true
        initSUT()

        assertTrue(sut.inAppEnableJavascript)
        verify {
            mockPreferenceManager.getBoolean(keyInAppEnableJavascript, true)
        }
    }

    @Test
    fun `in app enable javascript saves value to shared prefs repository`() {
        initSUT()

        sut.inAppEnableJavascript = true
        verify {
            mockPreferenceManager.save(keyInAppEnableJavascript, true)
        }
    }

    //endregion

    //region rssShowDescription

    @Test
    fun `rss show description reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyRssShowDescription, true) } returns true
        initSUT()

        assertTrue(sut.rssShowDescription)
        verify {
            mockPreferenceManager.getBoolean(keyRssShowDescription, true)
        }
    }

    @Test
    fun `rss show description saves value to shared prefs repository`() {
        initSUT()

        sut.rssShowDescription = true
        verify {
            mockPreferenceManager.save(keyRssShowDescription, true)
        }
    }

    //endregion

    //region newsOpenInExternalBrowser

    @Test
    fun `news open in external browser reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyNewsOpenInExternalBrowser, false) } returns true
        initSUT()

        assertTrue(sut.newsOpenInExternalBrowser)
        verify {
            mockPreferenceManager.getBoolean(keyNewsOpenInExternalBrowser, false)
        }
    }

    @Test
    fun `news open in external browser saves value to shared prefs repository`() {
        initSUT()

        sut.newsOpenInExternalBrowser = true
        verify {
            mockPreferenceManager.save(keyNewsOpenInExternalBrowser, true)
        }
    }

    //endregion

    companion object {
        private const val keyRssList: String = "RSS_LIST"
        private const val keyRssShowDescription: String = "NEWS_SHOW_DESCRIPTIONS"
        private const val keyInAppEnableJavascript: String = "IN_APP_ENABLE_JAVASCRIPT"
        private const val keyNewsOpenInExternalBrowser: String = "NEWS_OPEN_IN_EXTERNAL_BROWSER"
    }
}