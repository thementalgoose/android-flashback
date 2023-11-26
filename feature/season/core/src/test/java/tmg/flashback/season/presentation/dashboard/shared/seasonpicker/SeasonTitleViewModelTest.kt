package tmg.flashback.season.presentation.dashboard.shared.seasonpicker

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SeasonTitleViewModelTest {

    private val mockCurrentSeasonHolder: CurrentSeasonHolder = mockk(relaxed = true)

    private lateinit var underTest: SeasonTitleViewModel

    private fun initUnderTest() {
        underTest = SeasonTitleViewModel(
            currentSeasonHolder = mockCurrentSeasonHolder
        )
    }

    @Test
    fun `current season returns flow from holder`() {
        val flow: StateFlow<List<Int>> = mockk(relaxed = true)
        every { mockCurrentSeasonHolder.supportedSeasonFlow } returns flow

        initUnderTest()
        assertEquals(flow, underTest.outputs.supportedSeasons )
    }
    @Test
    fun `supported seasons returns flow from holder`() {
        val flow: StateFlow<Int> = mockk(relaxed = true)
        every { mockCurrentSeasonHolder.currentSeasonFlow } returns flow

        initUnderTest()
        assertEquals(flow, underTest.outputs.currentSeason)
    }
    @Test
    fun `new season available returns flow from holder`() {
        val flow: StateFlow<Boolean> = mockk(relaxed = true)
        every { mockCurrentSeasonHolder.newSeasonAvailableFlow } returns flow

        initUnderTest()
        assertEquals(flow, underTest.outputs.newSeasonAvailable)
    }

    @Test
    fun `update season calls update in holder`() {
        initUnderTest()
        underTest.inputs.currentSeasonUpdate(2023)
        verify {
            mockCurrentSeasonHolder.updateTo(2023)
        }
    }
}