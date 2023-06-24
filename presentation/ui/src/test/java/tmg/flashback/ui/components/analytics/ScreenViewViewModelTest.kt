package tmg.flashback.ui.components.analytics

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager

internal class ScreenViewViewModelTest {

    private val mockFirebaseAnalyticsManager: FirebaseAnalyticsManager = mockk(relaxed = true)

    private lateinit var underTest: ScreenViewViewModel

    private fun initUnderTest() {
        underTest = ScreenViewViewModel(
            firebaseAnalyticsManager = mockFirebaseAnalyticsManager
        )
    }

    @Test
    fun `logging screen event sends to manager`() {
        initUnderTest()
        underTest.inputs.viewScreen("name", mapOf("arg" to "arg"))

        verify {
            mockFirebaseAnalyticsManager.viewScreen("name", mapOf("arg" to "arg"))
        }
    }
}