package tmg.flashback.rss.repo.converters

import org.junit.jupiter.api.Test
import tmg.flashback.rss.repo.json.SupportedSourceJson
import tmg.flashback.rss.repo.model.SupportedSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

internal class SupportedSourcesJsonConverterTest {

    @Test
    fun `convert returns model successfully`() {
        assertEquals(expectedSource, inputSource.convert())
    }

    @Test
    fun `convert returns null is source is null`() {
        assertNull(inputSource
            .copy(source = null)
            .convert()
        )
    }

    @Test
    fun `convert returns null is title is null`() {
        assertNull(inputSource
            .copy(title = null)
            .convert()
        )
    }

    @Test
    fun `convert returns null is rssLink is null`() {
        assertNull(inputSource
            .copy(rssLink = null)
            .convert()
        )
    }

    @Test
    fun `convert returns null is colour is null`() {
        assertNull(inputSource
            .copy(colour = null)
            .convert()
        )
    }

    @Test
    fun `convert returns null is textColour is null`() {
        assertNull(inputSource
            .copy(textColour = null)
            .convert()
        )
    }

    private val inputSource = SupportedSourceJson(
        rssLink = "rssLink",
        sourceShort = "sourceShort",
        source = "source",
        colour = "colour",
        textColour = "textColour",
        title = "title"
    )
    private val expectedSource = SupportedSource(
        rssLink = "rssLink",
        sourceShort = "sourceShort",
        source = "source",
        colour = "colour",
        textColour = "textColour",
        title = "title"
    )

}