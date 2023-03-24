package tmg.flashback.rss

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.navigation.Screen

internal class RssNavigationComponentTest {

    @Test
    fun `settings rss`() {
        assertEquals("settings/rss", tmg.flashback.navigation.Screen.Settings.RSS.route)
    }

    @Test
    fun `settings rss configure`() {
        assertEquals("settings/rss/configure", tmg.flashback.navigation.Screen.Settings.RSSConfigure.route)
    }

    @Test
    fun rss() {
        assertEquals("rss", tmg.flashback.navigation.Screen.RSS.route)
    }

}