package tmg.flashback.rss.repo

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.core.device.repository.SharedPreferenceRepository

internal class RSSRepositoryTest {

    private val mockSharedPreferenceRepository: SharedPreferenceRepository = mockk(relaxed = true)

    private lateinit var sut: RSSRepository

    private fun initSUT() {
        sut = RSSRepository(mockSharedPreferenceRepository)
    }

    //region rssUrls

    @Test
    fun `get rss urls reads value from preferences repository`() {
        every { mockSharedPreferenceRepository.getSet(keyRssList, emptySet()) } returns mutableSetOf("test")
        initSUT()

        assertEquals(mutableSetOf("test"), sut.rssUrls)
        verify {
            mockSharedPreferenceRepository.getSet(keyRssList, emptySet())
        }
    }

    @Test
    fun `get rss urls saves value to shared prefs repository`() {
        initSUT()

        sut.rssUrls = setOf("hey")
        verify {
            mockSharedPreferenceRepository.save(keyRssList, setOf("hey"))
        }
    }

    //endregion

    //region inAppEnableJavascript

    @Test
    fun `in app enable javascript reads value from preferences repository`() {
        every { mockSharedPreferenceRepository.getBoolean(keyInAppEnableJavascript, true) } returns true
        initSUT()

        assertTrue(sut.inAppEnableJavascript)
        verify {
            mockSharedPreferenceRepository.getBoolean(keyInAppEnableJavascript, true)
        }
    }

    @Test
    fun `in app enable javascript saves value to shared prefs repository`() {
        initSUT()

        sut.inAppEnableJavascript = true
        verify {
            mockSharedPreferenceRepository.save(keyInAppEnableJavascript, true)
        }
    }

    //endregion

    //region rssShowDescription

    @Test
    fun `rss show description reads value from preferences repository`() {
        every { mockSharedPreferenceRepository.getBoolean(keyRssShowDescription, true) } returns true
        initSUT()

        assertTrue(sut.rssShowDescription)
        verify {
            mockSharedPreferenceRepository.getBoolean(keyRssShowDescription, true)
        }
    }

    @Test
    fun `rss show description saves value to shared prefs repository`() {
        initSUT()

        sut.rssShowDescription = true
        verify {
            mockSharedPreferenceRepository.save(keyRssShowDescription, true)
        }
    }

    //endregion

    //region newsOpenInExternalBrowser

    @Test
    fun `news open in external browser reads value from preferences repository`() {
        every { mockSharedPreferenceRepository.getBoolean(keyNewsOpenInExternalBrowser, true) } returns true
        initSUT()

        assertTrue(sut.newsOpenInExternalBrowser)
        verify {
            mockSharedPreferenceRepository.getBoolean(keyNewsOpenInExternalBrowser, false)
        }
    }

    @Test
    fun `news open in external browser saves value to shared prefs repository`() {
        initSUT()

        sut.newsOpenInExternalBrowser = true
        verify {
            mockSharedPreferenceRepository.save(keyNewsOpenInExternalBrowser, true)
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