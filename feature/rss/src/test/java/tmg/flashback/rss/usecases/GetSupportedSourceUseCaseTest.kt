package tmg.flashback.rss.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.converters.toArticleSource
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.repo.model.SupportedSource
import tmg.flashback.rss.repo.model.model

internal class GetSupportedSourceUseCaseTest {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)

    private lateinit var underTest: GetSupportedSourceUseCase

    private fun initUnderTest() {
        underTest = GetSupportedSourceUseCase(
            rssRepository = mockRssRepository
        )
    }

    @Test
    fun `get by rss link with valid link`() {
        val rssLink = "rss_link"
        every { mockRssRepository.supportedSources } returns listOf(
            SupportedSource.model(rssLink = rssLink)
        )

        initUnderTest()
        val expected = SupportedArticleSource.model(rssLink = rssLink)

        assertEquals(expected, underTest.getByRssLink(rssLink = rssLink))
    }

    @Test
    fun `get by rss link with invalid link returns null`() {
        val rssLink = "rss_link"
        every { mockRssRepository.supportedSources } returns listOf(
            SupportedSource.model(rssLink = "unknown_rss_link")
        )

        initUnderTest()

        assertNull(underTest.getByRssLink(rssLink = rssLink))
    }

    @Test
    fun `get by link with valid link returns valid item`() {
        val model = SupportedSource.model(rssLink = "https://www.google.com")
        every { mockRssRepository.supportedSources } returns listOf(model)

        initUnderTest()

        assertEquals(model.toArticleSource(), underTest.getByLink("https://www.google.com/help/me.xml"))
    }

    @ParameterizedTest
    @CsvSource(
        "bbc.co.uk,https://www.bbc.co.uk",
        "f1i.com,https://en.f1i.com"
    )
    fun `get by link fallback url works`(override: String, host: String) {
        val model = SupportedSource.model(
            source = override,
            rssLink = "$host/rss.xml"
        )
        every { mockRssRepository.supportedSources } returns listOf(model)

        initUnderTest()

        assertEquals(model.toArticleSource(), underTest.getByLink("$host/help/me.xml"))
    }

    @Test
    fun `get by link with invalid link returns valid item`() {
        every { mockRssRepository.supportedSources } returns listOf(
            SupportedSource.model(rssLink = "https://www.google.com")
        )

        initUnderTest()

        assertNull(underTest.getByLink(link = "https://help.com/test"))
    }
}