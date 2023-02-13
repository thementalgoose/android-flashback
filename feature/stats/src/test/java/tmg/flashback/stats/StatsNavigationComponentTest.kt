package tmg.flashback.stats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.ui.navigation.Screen

internal class StatsNavigationComponentTest {

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
        assertEquals("drivers/id?driverName=name", Screen.Driver.with("id", "name").route)
    }

    @Test
    fun `driver season`() {
        assertEquals("drivers/{driverId}/{season}?driverName={driverName}", Screen.Driver.route)
        assertEquals("drivers/id/2022?driverName=name", Screen.DriverSeason.with("id", "name", 2022).route)
    }

    @Test
    fun constructor() {
        assertEquals("constructors/{constructorId}?constructorName={constructorName}", Screen.Constructor.route)
        assertEquals("constructors/id?constructorName=name", Screen.Constructor.with("id", "name").route)
    }

    @Test
    fun `constructor season`() {
        assertEquals("constructors/{constructorId}/{season}?driverName={constructorName}", Screen.ConstructorSeason.route)
        assertEquals("constructors/id/2022?constructorName=name", Screen.ConstructorSeason.with("id", "name", 2022).route)
    }

    @Test
    fun circuit() {
        assertEquals("circuit/{circuitId}?circuitName={circuitName}", Screen.Circuit.route)
        assertEquals("circuit/id?circuitName=name", Screen.Circuit.with("id", "name").route)
    }

    @Test
    fun search() {
        assertEquals("search", Screen.Search.route)
    }

    @Test
    fun weekend() {
        TODO()
    }
}