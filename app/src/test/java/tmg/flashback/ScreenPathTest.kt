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
        assertEquals(
            """driver/{"driverId":"id","driverName":"name"}""",
            Screen.Driver("id", "name").route
        )
    }

    @Test
    fun constructor() {
        assertEquals(
            "constructors/{data}",
            Screen.Constructor.route
        )
        assertEquals(
            """constructors/{"constructorId":"id","constructorName":"name"}""",
            Screen.Constructor("id", "name").route
        )
    }

    @Test
    fun circuit() {
        assertEquals(
            "circuit/{data}",
            Screen.Circuit.route
        )
        assertEquals(
            """circuit/{"circuitId":"id","circuitName":"name"}""",
            Screen.Circuit("id", "name").route
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