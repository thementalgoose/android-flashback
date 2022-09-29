package tmg.flashback.rss.repo.converters

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.repo.model.SupportedSource
import tmg.flashback.rss.repo.model.model

class SupportedArticleSourceConverterTest {

    @Test
    fun `mapping supported source to supported article source maps correctly`() {
        assertEquals(
            SupportedArticleSource.model(),
            SupportedSource.model().toArticleSource()
        )
    }
}