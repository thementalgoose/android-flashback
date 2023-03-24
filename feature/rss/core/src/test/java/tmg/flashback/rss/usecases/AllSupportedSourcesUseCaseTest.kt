package tmg.flashback.rss.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.repo.model.SupportedSource
import tmg.flashback.rss.repo.model.model

internal class AllSupportedSourcesUseCaseTest {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)

    private lateinit var underTest: AllSupportedSourcesUseCase

    private fun initUnderTest() {
        underTest = AllSupportedSourcesUseCase(
            rssRepository = mockRssRepository
        )
    }

    @Test
    fun `sources are mapped into supported sources with toggle on`() {
        every { mockRssRepository.supportedSources } returns listOf(SupportedSource.model())
        every { mockRssRepository.enabled } returns true

        initUnderTest()
        assertEquals(listOf(SupportedArticleSource.model()), underTest.getSources())
    }

    @Test
    fun `sources are mapped into empty with toggle off`() {
        every { mockRssRepository.supportedSources } returns listOf(SupportedSource.model())
        every { mockRssRepository.enabled } returns false

        initUnderTest()
        assertEquals(emptyList<SupportedArticleSource>(), underTest.getSources())
    }
}