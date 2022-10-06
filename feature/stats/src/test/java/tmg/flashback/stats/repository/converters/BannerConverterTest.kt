package tmg.flashback.stats.repository.converters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import tmg.flashback.stats.repository.json.BannerItemJson
import tmg.flashback.stats.repository.models.Banner

internal class BannerConverterTest {

    @Test
    fun `null message results in null banner object`() {
        val json = BannerItemJson(
            msg = null,
            url = null,
            highlight = false,
            season = null
        )

        assertNull(json.convert())
    }

    @Test
    fun `empty message results in null banner object`() {
        val json = BannerItemJson(
            msg = "",
            url = "hey",
            highlight = false,
            season = null
        )

        assertNull(json.convert())
    }

    @Test
    fun `invalid url returns null url model`() {
        val json = BannerItemJson(
            msg = "test message",
            url = "hey",
            highlight = false,
            season = null
        )

        assertNull(json.convert()!!.url)
    }

    @Test
    fun `valid banner results in banner object created`() {
        val json = BannerItemJson(
            msg = "hey",
            url = "https://www.google.com",
            highlight = true,
            season = null
        )
        val expected = Banner(
            message = "hey",
            url = "https://www.google.com",
            highlight = true,
            season = null
        )

        assertEquals(expected, json.convert())
    }
}