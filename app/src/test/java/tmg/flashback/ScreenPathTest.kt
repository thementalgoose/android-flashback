package tmg.flashback

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.with
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.ConstructorSeason
import tmg.flashback.constructors.contract.with
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.navigation.Screen
import tmg.flashback.season.Calendar
import tmg.flashback.season.Constructors
import tmg.flashback.season.Drivers
import tmg.flashback.season.with
import tmg.flashback.rss.contract.RSS
import tmg.flashback.rss.contract.RSSConfigure
import tmg.flashback.search.contract.Search
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.with

class ScreenPathTest {


    @Test
    fun `screen calendar`() {
        assertEquals("results/calendar/{season}", Screen.Calendar.route)
        assertEquals("results/calendar/2022", Screen.Calendar.with(2022).route)
    }

    @Test
    fun `screen constructors`() {
        assertEquals("results/constructors/{season}", Screen.Constructors.route)
        assertEquals("results/constructors/2022", Screen.Constructors.with(2022).route)
    }

    @Test
    fun `screen drivers`() {
        assertEquals("results/drivers/{season}", Screen.Drivers.route)
        assertEquals("results/drivers/2022", Screen.Drivers.with(2022).route)
    }

    @Test
    fun driver() {
        assertEquals("drivers/{data}", Screen.Driver.route)
        assertEquals(
            """drivers/{"driverId":"id","driverName":"name"}""",
            Screen.Driver.with("id", "name").route
        )
    }

    @Test
    fun `driver season`() {
        assertEquals(
            "drivers-season/{data}",
            Screen.DriverSeason.route
        )
        assertEquals(
            """drivers-season/{"driverId":"id","driverName":"name","season":2022}""",
            Screen.DriverSeason.with("id", "name", 2022).route
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
    fun `constructor season`() {
        assertEquals(
            "constructors-season/{data}",
            Screen.ConstructorSeason.route
        )
        assertEquals(
            """constructors-season/{"constructorId":"id","constructorName":"name","season":2022}""",
            Screen.ConstructorSeason.with("id", "name", 2022).route
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