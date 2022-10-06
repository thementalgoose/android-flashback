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
            url = null
        )

        assertNull(json.convert())
    }

    @Test
    fun `empty message results in null banner object`() {
        val json = BannerItemJson(
            msg = "",
            url = "hey"
        )

        assertNull(json.convert())
    }

    @Test
    fun `valid banner results in banner object created`() {
        val json = BannerItemJson(
            msg = "hey",
            url = "hey"
        )

        assertEquals(Banner("hey", "hey"), json.convert())
    }
}