package tmg.flashback.rss.repo

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.rss.repo.json.SupportedSourceJson
import tmg.flashback.rss.repo.json.SupportedSourcesJson
import tmg.flashback.rss.repo.model.SupportedSource

internal class RSSRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private val mockConfigManager: ConfigManager = mockk(relaxed = true)

    private lateinit var sut: RSSRepository

    private fun initSUT() {
        sut = RSSRepository(mockPreferenceManager, mockConfigManager)
    }

    //region enabled

    @Test
    fun `get rss enabled feature reads value from config`() {
        every { mockConfigManager.getBoolean(keyRss) } returns true
        initSUT()
        assertTrue(sut.enabled)
        verify {
            mockConfigManager.getBoolean(keyRss)
        }
    }

    @Test
    fun `get rss enabled feature is lazy loaded`() {
        every { mockConfigManager.getBoolean(keyRss) } returns true
        initSUT()
        assertTrue(sut.enabled)
        verify(exactly = 1) {
            mockConfigManager.getBoolean(keyRss)
        }
        every { mockConfigManager.getBoolean(keyRss) } returns false
        assertTrue(sut.enabled)
        verify(exactly = 1) {
            mockConfigManager.getBoolean(keyRss)
        }
    }

    //endregion

    //region addCustom

    @Test
    fun `get rss add custom enabled feature reads value from config`() {
        every { mockConfigManager.getBoolean(keyRssAddCustom) } returns true
        initSUT()
        assertTrue(sut.addCustom)
        verify {
            mockConfigManager.getBoolean(keyRssAddCustom)
        }
    }

    @Test
    fun `get rss add custom enabled feature is lazy loaded`() {
        every { mockConfigManager.getBoolean(keyRssAddCustom) } returns true
        initSUT()
        assertTrue(sut.addCustom)
        verify(exactly = 1) {
            mockConfigManager.getBoolean(keyRssAddCustom)
        }
        every { mockConfigManager.getBoolean(keyRssAddCustom) } returns false
        assertTrue(sut.addCustom)
        verify(exactly = 1) {
            mockConfigManager.getBoolean(keyRssAddCustom)
        }
    }

    //endregion

    //region supportedSources

    @Test
    fun `getting supported sources returns empty list if manager sources null`() {
        every { mockConfigManager.getJson(keyRssSupportedSources, SupportedSourcesJson.serializer()) } returns null
        initSUT()
        assertEquals(emptyList<SupportedSource>(), sut.supportedSources)
        verify {
            mockConfigManager.getJson(keyRssSupportedSources, SupportedSourcesJson.serializer())
        }
    }

    @Test
    fun `getting supported sources returns empty list if model sources is null`() {
        every { mockConfigManager.getJson(keyRssSupportedSources, SupportedSourcesJson.serializer()) } returns SupportedSourcesJson(sources = null)
        initSUT()
        assertEquals(emptyList<SupportedSource>(), sut.supportedSources)
        verify {
            mockConfigManager.getJson(keyRssSupportedSources, SupportedSourcesJson.serializer())
        }
    }

    @Test
    fun `getting supported sources returns empty list if model sources is empty`() {
        every { mockConfigManager.getJson(keyRssSupportedSources, SupportedSourcesJson.serializer()) } returns SupportedSourcesJson(sources = emptyList())
        initSUT()
        assertEquals(emptyList<SupportedSource>(), sut.supportedSources)
        verify {
            mockConfigManager.getJson(keyRssSupportedSources, SupportedSourcesJson.serializer())
        }
    }

    @Test
    fun `getting supported sources returns valid list when json content`() {
        every { mockConfigManager.getJson(keyRssSupportedSources, SupportedSourcesJson.serializer()) } returns SupportedSourcesJson(sources = listOf(SupportedSourceJson(
                rssLink = "rssLink",
                source = "source",
                sourceShort = "sourceShort",
                colour = "color",
                textColour = "color",
                title = "title",
                contactLink = "contact"
        )))
        initSUT()
        assertEquals("rssLink", sut.supportedSources.first().rssLink)
        verify {
            mockConfigManager.getJson(keyRssSupportedSources, SupportedSourcesJson.serializer())
        }
    }

    //endregion

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

    companion object {

        // Config
        private const val keyRss: String = "rss"
        private const val keyRssAddCustom: String = "rss_add_custom"
        private const val keyRssSupportedSources: String = "rss_supported_sources"

        // Prefs
        private const val keyRssList: String = "RSS_LIST"
        private const val keyRssShowDescription: String = "NEWS_SHOW_DESCRIPTIONS"
    }
}