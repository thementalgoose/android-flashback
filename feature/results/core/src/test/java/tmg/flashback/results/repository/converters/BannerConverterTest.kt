package tmg.flashback.results.repository.converters

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.results.repository.json.BannerItemJson
import tmg.flashback.results.repository.models.Banner

internal class BannerConverterTest {

    private val mockCrashManager: CrashManager = mockk(relaxed = true)

    @Test
    fun `null message results in null banner object`() {
        val json = BannerItemJson(
            msg = null,
            url = null,
            highlight = false,
            season = null
        )

        assertNull(json.convert(mockCrashManager))
    }

    @Test
    fun `null url but message populated results in banner object`() {
        val json = BannerItemJson(
            msg = "hey",
            url = null,
            highlight = true,
            season = null
        )
        val expected = Banner(
            message = "hey",
            url = null, // URLUtil stubbed out
            highlight = true,
            season = null
        )

        assertEquals(expected, json.convert(mockCrashManager))
    }

    @Test
    fun `empty message results in null banner object`() {
        val json = BannerItemJson(
            msg = "",
            url = "hey",
            highlight = false,
            season = null
        )

        assertNull(json.convert(mockCrashManager))
    }

    @Test
    fun `invalid url returns null url model`() {
        val json = BannerItemJson(
            msg = "test message",
            url = "hey",
            highlight = false,
            season = null
        )

        assertNull(json.convert(mockCrashManager)!!.url)
        verify {
            mockCrashManager.logException(any())
        }
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
            url = null, // URLUtil stubbed out
            highlight = true,
            season = null
        )

        assertEquals(expected, json.convert(mockCrashManager))
    }
}