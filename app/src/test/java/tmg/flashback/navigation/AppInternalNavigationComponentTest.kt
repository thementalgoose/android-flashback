package tmg.flashback.navigation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AppInternalNavigationComponentTest {

    private lateinit var underTest: AppInternalNavigationComponent

    @BeforeEach
    fun setUp() {
        underTest = AppInternalNavigationComponent()
    }

    @Test
    fun `get navigation data for circuit screen has formatted route`() {
        val data = Screen.Circuit("circuitId", "circuitName")
        val expectedRoute = """circuit/{"circuitId":"circuitId","circuitName":"circuitName"}"""

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
    }

    @Test
    fun `get navigation data for constructor screen has formatted route`() {
        val data = Screen.Constructor("constructorId", "constructorName")
        val expectedRoute = """constructor/{"constructorId":"constructorId","constructorName":"constructorName"}"""

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
    }

    @Test
    fun `get navigation data for driver screen has formatted route`() {
        val data = Screen.Driver("driverId", "driverName")
        val expectedRoute = """driver/{"driverId":"driverId","driverName":"driverName"}"""

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
    }

    @Test
    fun `get navigation data for constructor standings screen has formatted route`() {
        val data = Screen.ConstructorStandings
        val expectedRoute = "results/constructors"

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
        assertEquals(true, result.launchSingleTop)
        assertEquals(Screen.Races.route, result.popUpTo)
    }

    @Test
    fun `get navigation data for driver standings screen has formatted route`() {
        val data = Screen.DriverStandings
        val expectedRoute = "results/drivers"

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
        assertEquals(true, result.launchSingleTop)
        assertEquals(Screen.Races.route, result.popUpTo)
    }

    @Test
    fun `get navigation data for races screen has formatted route`() {
        val data = Screen.Races
        val expectedRoute = "results/races"

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
        assertEquals(true, result.launchSingleTop)
    }

    @Test
    fun `get navigation data for privacy policy screen has formatted route`() {
        val data = Screen.PrivacyPolicy
        val expectedRoute = "privacy_policy"

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
    }

    @Test
    fun `get navigation data for rss screen has formatted route`() {
        val data = Screen.Rss
        val expectedRoute = "rss"

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
        assertEquals(true, result.launchSingleTop)
        assertEquals(Screen.Races.route, result.popUpTo)
    }

    @Test
    fun `get navigation data for settings screen has formatted route`() {
        val data = Screen.Settings
        val expectedRoute = "settings"

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
        assertEquals(true, result.launchSingleTop)
        assertEquals(Screen.Races.route, result.popUpTo)
    }

    @Test
    fun `get navigation data for reaction game screen has formatted route`() {
        val data = Screen.ReactionGame
        val expectedRoute = "reaction"

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
        assertEquals(true, result.launchSingleTop)
    }

    @Test
    fun `get navigation data for search screen has formatted route`() {
        val data = Screen.Search
        val expectedRoute = "search"

        val result = underTest.getNavigationData(data)

        assertEquals(expectedRoute, result.route)
        assertEquals(true, result.launchSingleTop)
        assertEquals(Screen.Races.route, result.popUpTo)
    }
}