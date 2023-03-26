package tmg.flashback

import org.junit.jupiter.api.Assertions
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
import tmg.flashback.results.Calendar
import tmg.flashback.results.Constructors
import tmg.flashback.results.Drivers
import tmg.flashback.results.with
import tmg.flashback.rss.contract.RSS
import tmg.flashback.rss.contract.RSSConfigure
import tmg.flashback.search.contract.Search
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.WeekendInfo
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
        assertEquals("drivers/{driverId}?driverName={driverName}", Screen.Driver.route)
        assertEquals(
            "drivers/id?driverName=name",
            Screen.Driver.with("id", "name").route
        )
    }

    @Test
    fun `driver season`() {
        assertEquals(
            "drivers/{driverId}/{season}?driverName={driverName}",
            Screen.DriverSeason.route
        )
        assertEquals(
            "drivers/id/2022?driverName=name",
            Screen.DriverSeason.with("id", "name", 2022).route
        )
    }

    @Test
    fun constructor() {
        assertEquals(
            "constructors/{constructorId}?constructorName={constructorName}",
            Screen.Constructor.route
        )
        assertEquals(
            "constructors/id?constructorName=name",
            Screen.Constructor.with("id", "name").route
        )
    }

    @Test
    fun `constructor season`() {
        assertEquals(
            "constructors/{constructorId}/{season}?constructorName={constructorName}",
            Screen.ConstructorSeason.route
        )
        assertEquals(
            "constructors/id/2022?constructorName=name",
            Screen.ConstructorSeason.with("id", "name", 2022).route
        )
    }

    @Test
    fun circuit() {
        assertEquals(
            "circuit/{circuitId}?circuitName={circuitName}",
            Screen.Circuit.route
        )
        assertEquals(
            "circuit/id?circuitName=name",
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
            "weekend/{season}/{round}?" +
                    "raceName={raceName}" + "&" +
                    "circuitId={circuitId}" + "&" +
                    "circuitName={circuitName}" + "&" +
                    "country={country}" + "&" +
                    "countryISO={countryISO}" + "&" +
                    "date={date}", Screen.Weekend.route
        )
        assertEquals(
            "weekend/2020/1?" +
                    "raceName=raceName" + "&" +
                    "circuitId=circuitId" + "&" +
                    "circuitName=circuitName" + "&" +
                    "country=country" + "&" +
                    "countryISO=countryISO" + "&" +
                    "date=date", Screen.Weekend.with(
                WeekendInfo(
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