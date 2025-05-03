package tmg.flashback

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.navigation.Screen
import tmg.flashback.navigation.route

class ScreenPathTest {

    @Test
    fun `screen calendar`() {
        assertEquals("results/races", Screen.Races.route)
    }

    @Test
    fun `screen constructors`() {
        assertEquals("results/constructors", Screen.ConstructorStandings.route)
    }

    @Test
    fun `screen drivers`() {
        assertEquals("results/drivers", Screen.DriverStandings.route)
    }

    @Test
    fun driver() {
        assertEquals("driver/{data}", Screen.Driver.route)
    }

    @Test
    fun constructor() {
        assertEquals(
            "constructor/{data}",
            Screen.Constructor.route
        )
    }

    @Test
    fun circuit() {
        assertEquals(
            "circuit/{data}",
            Screen.Circuit.route
        )
    }

    @Test
    fun search() {
        assertEquals("search", Screen.Search.route)
    }

    @Test
    fun rss() {
        assertEquals("rss", Screen.Rss.route)
    }
}