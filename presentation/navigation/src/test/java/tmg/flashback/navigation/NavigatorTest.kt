package tmg.flashback.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.crashlytics.manager.CrashlyticsManager

internal class NavigatorTest {

    private val mockCrashlyticsManager: CrashlyticsManager = mockk(relaxed = true)

    private val mockNavController: NavHostController = mockk(relaxed = true)

    private lateinit var underTest: Navigator

    private fun initUnderTest() {
        underTest = Navigator(
            crashlyticsManager = mockCrashlyticsManager
        )
        underTest.navController = mockNavController
    }

    @Test
    fun `navigating to a destination logs destination to crash manager`() {
        val destination = NavigationDestination(route = "route")

        initUnderTest()
        underTest.navigate(destination)

        val dest = slot<NavigationDestination>()
        verify {
            mockNavController.navigate(capture(dest), null, null)
            mockCrashlyticsManager.log(any())
        }
        assertEquals("route", dest.captured.route)
    }
}