package tmg.flashback

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.circuits.contract.with
import tmg.flashback.constructors.contract.with
import tmg.flashback.drivers.contract.with
import tmg.flashback.navigation.Screen
import tmg.flashback.rss.contract.RSS
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.navigation.ScreenWeekendData
import tmg.flashback.weekend.contract.with

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
            Screen.Driver.with("id", "name").route
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
            Screen.Constructor.with("id", "name").route
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
            Screen.Circuit.with("id", "name").route
        )
    }

    @Test
    fun search() {
        assertEquals("search", Screen.Search.route)
    }

    @Test
    fun weekend() {
        assertEquals(
            "weekend/{data}?tab={tab}", Screen.Weekend.route
        )
        assertEquals(
            """weekend/{"season":2020,"round":1,"raceName":"raceName","circuitId":"circuitId","circuitName":"circuitName","country":"country","countryISO":"countryISO","dateString":"date"}?tab=RACE""".trimIndent(),
            Screen.Weekend.with(
                ScreenWeekendData(
                    season = 2020,
                    round = 1,
                    raceName = "raceName",
                    circuitId = "circuitId",
                    circuitName = "circuitName",
                    country = "country",
                    countryISO = "countryISO",
                    dateString = "date",
                )
            ).route
        )
    }

    @Test
    fun rss() {
        assertEquals("rss", Screen.RSS.route)
    }
}