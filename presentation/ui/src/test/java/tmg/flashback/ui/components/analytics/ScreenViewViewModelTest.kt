package tmg.flashback.ui.components.analytics

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.analytics.manager.AnalyticsManager

internal class ScreenViewViewModelTest {

    private val mockAnalyticsManager: AnalyticsManager = mockk(relaxed = true)

    private lateinit var underTest: ScreenViewViewModel

    private fun initUnderTest() {
        underTest = ScreenViewViewModel(
            analyticsManager = mockAnalyticsManager
        )
    }

    @Test
    fun `logging screen event sends to manager`() {
        initUnderTest()
        underTest.inputs.viewScreen("name", mapOf("arg" to "arg"))

        verify {
            mockAnalyticsManager.viewScreen("name", mapOf("arg" to "arg"))
        }
    }
}