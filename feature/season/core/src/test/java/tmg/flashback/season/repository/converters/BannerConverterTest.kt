package tmg.flashback.season.repository.converters

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.season.repository.json.BannerItemJson
import tmg.flashback.results.repository.models.Banner

internal class BannerConverterTest {

    private val mockCrashlyticsManager: CrashlyticsManager = mockk(relaxed = true)

    @Test
    fun `null message results in null banner object`() {
        val json = BannerItemJson(
            msg = null,
            url = null,
            highlight = false,
            season = null
        )

        assertNull(json.convert(mockCrashlyticsManager))
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

        assertEquals(expected, json.convert(mockCrashlyticsManager))
    }

    @Test
    fun `empty message results in null banner object`() {
        val json = BannerItemJson(
            msg = "",
            url = "hey",
            highlight = false,
            season = null
        )

        assertNull(json.convert(mockCrashlyticsManager))
    }

    @Test
    fun `invalid url returns null url model`() {
        val json = BannerItemJson(
            msg = "test message",
            url = "hey",
            highlight = false,
            season = null
        )

        assertNull(json.convert(mockCrashlyticsManager)!!.url)
        verify {
            mockCrashlyticsManager.logException(any())
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

        assertEquals(expected, json.convert(mockCrashlyticsManager))
    }
}