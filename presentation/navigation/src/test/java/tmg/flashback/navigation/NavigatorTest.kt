package tmg.flashback.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import io.mockk.every
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
    private val mockInternalNavigationComponent: InternalNavigationComponent = mockk(relaxed = true)

    private val mockNavController: NavHostController = mockk(relaxed = true)

    private lateinit var underTest: Navigator

    private fun initUnderTest() {
        underTest = Navigator(
            internalNavigationComponent = mockInternalNavigationComponent,
            crashlyticsManager = mockCrashlyticsManager
        )
        underTest.navController = mockNavController
    }

    @Test
    fun `navigating to a destination logs destination to crash manager`() {
        val screen = Screen.Search

        every { mockInternalNavigationComponent.getNavigationData(screen) } returns NavigationDestination("route")

        initUnderTest()
        underTest.navigate(screen)

        val dest = slot<String>()
        val lambda = slot<NavOptionsBuilder.() -> Unit>()
        verify {
            mockInternalNavigationComponent.getNavigationData(screen)
            mockNavController.navigate(capture(dest), capture(lambda))
            mockCrashlyticsManager.log(any())
        }
        assertEquals("route", dest.captured)
    }
}