package tmg.flashback.rss

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.ui.navigation.Screen

internal class RssNavigationComponentTest {

    @Test
    fun `settings rss`() {
        assertEquals("settings/rss", Screen.Settings.RSS.route)
    }

    @Test
    fun `settings rss configure`() {
        assertEquals("settings/rss/configure", Screen.Settings.RSSConfigure.route)
    }

    @Test
    fun rss() {
        assertEquals("rss", Screen.RSS.route)
    }

}