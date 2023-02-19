package tmg.flashback.ui.navigation

import androidx.lifecycle.asLiveData
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import org.junit.jupiter.api.Test
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.testutils.livedata.test

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
        val destination = NavigationDestination(
            route = "route"
        )

        initUnderTest()

        underTest.navigate(destination)

        underTest.destination
            .asStateFlow()
            .asLiveData(Dispatchers.Unconfined)
            .test {
                assertValue(destination)
            }
        verify {
            mockCrashManager.log("Navigate to ${destination.route}")
        }
    }
}