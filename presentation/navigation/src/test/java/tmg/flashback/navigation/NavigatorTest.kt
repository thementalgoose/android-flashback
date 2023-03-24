package tmg.flashback.navigation

import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.asStateFlow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.crash_reporting.manager.CrashManager

internal class NavigatorTest {

    private val mockCrashManager: CrashManager = mockk(relaxed = true)

    private lateinit var underTest: Navigator

    private fun initUnderTest() {
        underTest = Navigator(
            crashManager = mockCrashManager
        )
    }

    @Test
    fun `navigating to a destination logs destination to crash manager`() {
        val destination = NavigationDestination(route = "route")

        initUnderTest()
        val dest = underTest.destination.asStateFlow()

        underTest.navigate(destination)

        assertEquals(destination, dest.value)
        verify {
            mockCrashManager.log(any())
        }
    }
}