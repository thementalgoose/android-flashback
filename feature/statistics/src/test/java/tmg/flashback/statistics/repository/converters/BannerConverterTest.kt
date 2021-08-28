package tmg.flashback.statistics.repository.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.repository.json.BannerJson
import tmg.flashback.statistics.repository.models.Banner

internal class BannerConverterTest {

    @Test
    fun `null message results in null banner object`() {
        val json = BannerJson(
            msg = null,
            url = null
        )

        assertNull(json.convert())
    }

    @Test
    fun `empty message results in null banner object`() {
        val json = BannerJson(
            msg = "",
            url = "hey"
        )

        assertNull(json.convert())
    }

    @Test
    fun `valid banner results in banner object created`() {
        val json = BannerJson(
            msg = "hey",
            url = "hey"
        )

        assertEquals(Banner("hey", "hey"), json.convert())
    }
}