package tmg.flashback.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.asStateFlow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.crash_reporting.manager.CrashManager

internal class NavigatorTest {

    private val mockCrashManager: CrashManager = mockk(relaxed = true)

    private val mockNavController: NavHostController = mockk(relaxed = true)

    private lateinit var underTest: Navigator

    private fun initUnderTest() {
        underTest = Navigator(
            crashManager = mockCrashManager
        )
        underTest.navController = mockNavController
    }

    @Test
    fun `navigating to a destination logs destination to crash manager`() {
        val destination = NavigationDestination(route = "route")

        initUnderTest()
        underTest.navigate(destination)

        val dest = slot<String>()
        verify {
            mockNavController.navigate(capture(dest), any<NavOptionsBuilder.() -> Unit>())
            mockCrashManager.log(any())
        }
        assertEquals(destination.route, dest.captured)
    }
}