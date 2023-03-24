package tmg.flashback.stats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.navigation.Screen

internal class StatsNavigationComponentTest {

    @Test
    fun `screen calendar`() {
        assertEquals("results/calendar/{season}", tmg.flashback.navigation.Screen.Calendar.route)
        assertEquals("results/calendar/2022", tmg.flashback.navigation.Screen.Calendar.with(2022).route)
    }

    @Test
    fun `screen constructors`() {
        assertEquals("results/constructors/{season}", tmg.flashback.navigation.Screen.Constructors.route)
        assertEquals("results/constructors/2022", tmg.flashback.navigation.Screen.Constructors.with(2022).route)
    }

    @Test
    fun `screen drivers`() {
        assertEquals("results/drivers/{season}", tmg.flashback.navigation.Screen.Drivers.route)
        assertEquals("results/drivers/2022", tmg.flashback.navigation.Screen.Drivers.with(2022).route)
    }

    @Test
    fun driver() {
        assertEquals("drivers/{driverId}?driverName={driverName}", tmg.flashback.navigation.Screen.Driver.route)
        assertEquals("drivers/id?driverName=name", tmg.flashback.navigation.Screen.Driver.with("id", "name").route)
    }

    @Test
    fun `driver season`() {
        assertEquals("drivers/{driverId}/{season}?driverName={driverName}", tmg.flashback.navigation.Screen.DriverSeason.route)
        assertEquals("drivers/id/2022?driverName=name", tmg.flashback.navigation.Screen.DriverSeason.with("id", "name", 2022).route)
    }

    @Test
    fun constructor() {
        assertEquals("constructors/{constructorId}?constructorName={constructorName}", tmg.flashback.navigation.Screen.Constructor.route)
        assertEquals("constructors/id?constructorName=name", tmg.flashback.navigation.Screen.Constructor.with("id", "name").route)
    }

    @Test
    fun `constructor season`() {
        assertEquals("constructors/{constructorId}/{season}?constructorName={constructorName}", tmg.flashback.navigation.Screen.ConstructorSeason.route)
        assertEquals("constructors/id/2022?constructorName=name", tmg.flashback.navigation.Screen.ConstructorSeason.with("id", "name", 2022).route)
    }

    @Test
    fun circuit() {
        assertEquals("circuit/{circuitId}?circuitName={circuitName}", tmg.flashback.navigation.Screen.Circuit.route)
        assertEquals("circuit/id?circuitName=name", tmg.flashback.navigation.Screen.Circuit.with("id", "name").route)
    }

    @Test
    fun search() {
        assertEquals("search", tmg.flashback.navigation.Screen.Search.route)
    }

    @Test
    fun weekend() {
        assertEquals("weekend/{season}/{round}?" +
                "raceName={raceName}" + "&" +
                "circuitId={circuitId}" + "&" +
                "circuitName={circuitName}" + "&" +
                "country={country}" + "&" +
                "countryISO={countryISO}" + "&" +
                "date={date}", tmg.flashback.navigation.Screen.Weekend.route)
        assertEquals("weekend/2020/1?" +
                "raceName=raceName" + "&" +
                "circuitId=circuitId" + "&" +
                "circuitName=circuitName" + "&" +
                "country=country" + "&" +
                "countryISO=countryISO" + "&" +
                "date=date", tmg.flashback.navigation.Screen.Weekend.with(WeekendInfo(
                    season = 2020,
                    round = 1,
                    raceName = "raceName",
                    circuitId = "circuitId",
                    circuitName = "circuitName",
                    country = "country",
                    countryISO = "countryISO",
                    dateString = "date",
                )).route)
    }
}