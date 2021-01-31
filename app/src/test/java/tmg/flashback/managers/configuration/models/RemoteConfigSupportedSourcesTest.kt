package tmg.flashback.managers.configuration.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.core.model.SupportedSource
import tmg.flashback.testutils.BaseTest

internal class RemoteConfigSupportedSourcesTest: BaseTest() {


    @Test
    fun `convert is empty if sources is null`() {
        val model = RemoteConfigSupportedSources(
            sources = null
        )
        assertEquals(emptyList<SupportedSource>(), model.convert())
    }

    @Test
    fun `convert is empty if no valid sources`() {
        val model = RemoteConfigSupportedSources(
            sources = listOf(RemoteConfigSupportedSource(
                null, null, null, null, null, null, null
            ))
        )
        assertEquals(emptyList<SupportedSource>(), model.convert())
    }

    @Test
    fun `invalid source is ignored from empty list`() {
        val model = RemoteConfigSupportedSources(
            sources = listOf(
                RemoteConfigSupportedSource(
                null, null, null, null, null, null, null
                ),
                RemoteConfigSupportedSource(
                    "rsslink", null, "source", "colour", "colour", "title", "contact"
                )
            )
        )
        val expected = SupportedSource(
            rssLink = "rsslink",
            sourceShort = "so",
            source = "source",
            colour = "colour",
            textColour = "colour",
            title = "title",
            contactLink = "contact"
        )
        assertEquals(listOf(expected), model.convert())
    }

    @Test
    fun `consider supported source as null if source is null`() {
        val model = RemoteConfigSupportedSource(
            rssLink = "rsslink",
            sourceShort = null,
            source = null,
            colour = "col",
            textColour = "textcol",
            title = "title",
            contactLink = "contact",
        )
        assertNull(model.convert())
    }

    @Test
    fun `consider supported source as null if title is null`() {
        val model = RemoteConfigSupportedSource(
            rssLink = "rsslink",
            sourceShort = null,
            source = "source",
            colour = "col",
            textColour = "textcol",
            title = null,
            contactLink = "contact",
        )
        assertNull(model.convert())
    }

    @Test
    fun `consider supported source as null if rssLink is null`() {
        val model = RemoteConfigSupportedSource(
            rssLink = null,
            sourceShort = null,
            source = "source",
            colour = "col",
            textColour = "textcol",
            title = "title",
            contactLink = "contact",
        )
        assertNull(model.convert())
    }

    @Test
    fun `consider supported source as null if colour is null`() {
        val model = RemoteConfigSupportedSource(
            rssLink = "rsslink",
            sourceShort = null,
            source = "source",
            colour = null,
            textColour = "textcol",
            title = "title",
            contactLink = "contact",
        )
        assertNull(model.convert())
    }

    @Test
    fun `consider supported source as null if textColour is null`() {
        val model = RemoteConfigSupportedSource(
            rssLink = "rsslink",
            sourceShort = null,
            source = "source",
            colour = "col",
            textColour = null,
            title = "title",
            contactLink = "contact",
        )
        assertNull(model.convert())
    }

}
