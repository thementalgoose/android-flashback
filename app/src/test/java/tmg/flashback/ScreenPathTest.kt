package tmg.flashback

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.with
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.navigation.Screen
import tmg.flashback.results.Calendar
import tmg.flashback.results.Constructor
import tmg.flashback.results.ConstructorSeason
import tmg.flashback.results.Constructors
import tmg.flashback.results.Drivers
import tmg.flashback.results.Search
import tmg.flashback.results.Weekend
import tmg.flashback.results.ui.weekend.WeekendInfo
import tmg.flashback.results.with

class ScreenPathTest {


    @Test
    fun `screen calendar`() {
        Assertions.assertEquals("results/calendar/{season}", Screen.Calendar.route)
        Assertions.assertEquals("results/calendar/2022", Screen.Calendar.with(2022).route)
    }

    @Test
    fun `screen constructors`() {
        Assertions.assertEquals("results/constructors/{season}", Screen.Constructors.route)
        Assertions.assertEquals("results/constructors/2022", Screen.Constructors.with(2022).route)
    }

    @Test
    fun `screen drivers`() {
        Assertions.assertEquals("results/drivers/{season}", Screen.Drivers.route)
        Assertions.assertEquals("results/drivers/2022", Screen.Drivers.with(2022).route)
    }

    @Test
    fun driver() {
        Assertions.assertEquals("drivers/{driverId}?driverName={driverName}", Screen.Driver.route)
        Assertions.assertEquals(
            "drivers/id?driverName=name",
            Screen.Driver.with("id", "name").route
        )
    }

    @Test
    fun `driver season`() {
        Assertions.assertEquals(
            "drivers/{driverId}/{season}?driverName={driverName}",
            Screen.DriverSeason.route
        )
        Assertions.assertEquals(
            "drivers/id/2022?driverName=name",
            Screen.DriverSeason.with("id", "name", 2022).route
        )
    }

    @Test
    fun constructor() {
        Assertions.assertEquals(
            "constructors/{constructorId}?constructorName={constructorName}",
            Screen.Constructor.route
        )
        Assertions.assertEquals(
            "constructors/id?constructorName=name",
            Screen.Constructor.with("id", "name").route
        )
    }

    @Test
    fun `constructor season`() {
        Assertions.assertEquals(
            "constructors/{constructorId}/{season}?constructorName={constructorName}",
            Screen.ConstructorSeason.route
        )
        Assertions.assertEquals(
            "constructors/id/2022?constructorName=name",
            Screen.ConstructorSeason.with("id", "name", 2022).route
        )
    }

    @Test
    fun circuit() {
        Assertions.assertEquals(
            "circuit/{circuitId}?circuitName={circuitName}",
            Screen.Circuit.route
        )
        Assertions.assertEquals(
            "circuit/id?circuitName=name",
            Screen.Circuit.with("id", "name").route
        )
    }

    @Test
    fun search() {
        Assertions.assertEquals("search", Screen.Search.route)
    }

    @Test
    fun weekend() {
        Assertions.assertEquals(
            "weekend/{season}/{round}?" +
                    "raceName={raceName}" + "&" +
                    "circuitId={circuitId}" + "&" +
                    "circuitName={circuitName}" + "&" +
                    "country={country}" + "&" +
                    "countryISO={countryISO}" + "&" +
                    "date={date}", Screen.Weekend.route
        )
        Assertions.assertEquals(
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
}