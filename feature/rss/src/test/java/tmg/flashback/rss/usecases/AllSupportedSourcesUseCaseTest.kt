package tmg.flashback.rss.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
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
    fun `sources are mapped into supported sources`() {
        every { mockRssRepository.supportedSources } returns listOf(SupportedSource.model())

        initUnderTest()
        assertEquals(listOf(SupportedArticleSource.model()), underTest.getSources())
    }
}