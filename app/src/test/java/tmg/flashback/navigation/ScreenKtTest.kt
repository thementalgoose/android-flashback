package tmg.flashback.navigation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.navigation.constants.Deeplink

class ScreenKtTest {

    @Test
    fun `deeplink routes line up with screen routes for Search`() {
        assertEquals(Screen.Search.route, Deeplink.Search.route)
    }

    @Test
    fun `deeplink routes line up with screen routes for Rss`() {
        assertEquals(Screen.Rss.route, Deeplink.Rss.route)
    }
}