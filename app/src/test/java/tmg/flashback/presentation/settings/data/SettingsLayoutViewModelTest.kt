package tmg.flashback.presentation.settings.data

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.season.repository.HomeRepository
import tmg.flashback.presentation.settings.Settings
import tmg.testutils.BaseTest

internal class SettingsLayoutViewModelTest: BaseTest() {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var underTest: SettingsLayoutViewModel

    private fun initUnderTest() {
        underTest = SettingsLayoutViewModel(
            homeRepository = mockHomeRepository
        )
    }

    @Test
    fun `collapse list is true when pref is true`() = runTest(testDispatcher) {
        every { mockHomeRepository.collapseList } returns true

        initUnderTest()
        underTest.outputs.collapsedListEnabled.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `collapse list is false when pref is false`() = runTest(testDispatcher) {
        every { mockHomeRepository.collapseList } returns false

        initUnderTest()
        underTest.outputs.collapsedListEnabled.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click show collapse list updates pref and updates value`() = runTest(testDispatcher) {
        every { mockHomeRepository.collapseList } returns false

        initUnderTest()
        underTest.outputs.collapsedListEnabled.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Data.collapseList(true))

        verify {
            mockHomeRepository.collapseList = true
        }
        underTest.outputs.collapsedListEnabled.test { awaitItem() }
    }

    @Test
    fun `empty weeks in schedule is true when pref is true`() = runTest(testDispatcher) {
        every { mockHomeRepository.emptyWeeksInSchedule } returns true

        initUnderTest()
        underTest.outputs.emptyWeeksInSchedule.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `empty weeks in schedule is false when pref is false`() = runTest(testDispatcher) {
        every { mockHomeRepository.emptyWeeksInSchedule } returns false

        initUnderTest()
        underTest.outputs.emptyWeeksInSchedule.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click show empty weeks in schedule updates pref and updates value`() = runTest(testDispatcher) {
        every { mockHomeRepository.emptyWeeksInSchedule } returns false

        initUnderTest()
        underTest.outputs.emptyWeeksInSchedule.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Data.showEmptyWeeksInSchedule(true))

        verify {
            mockHomeRepository.emptyWeeksInSchedule = true
        }
        underTest.outputs.emptyWeeksInSchedule.test { awaitItem() }
    }

    @Test
    fun `recent highlights in schedule is true when pref is true`() = runTest(testDispatcher) {
        every { mockHomeRepository.recentHighlights } returns true

        initUnderTest()
        underTest.outputs.recentHighlights.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `recent highlights in schedule is false when pref is false`() = runTest(testDispatcher) {
        every { mockHomeRepository.recentHighlights } returns false

        initUnderTest()
        underTest.outputs.recentHighlights.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click show recent highlights in schedule updates pref and updates value`() = runTest(testDispatcher) {
        every { mockHomeRepository.recentHighlights } returns false

        initUnderTest()
        underTest.outputs.recentHighlights.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Data.showRecentHighlights(true))

        verify {
            mockHomeRepository.recentHighlights = true
        }
        underTest.outputs.recentHighlights.test { awaitItem() }
    }


    @Test
    fun `remember season change is true when pref is true`() = runTest(testDispatcher) {
        every { mockHomeRepository.rememberSeasonChange } returns true

        initUnderTest()
        underTest.outputs.rememberSeasonChange.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `remember season change is false when pref is false`() = runTest(testDispatcher) {
        every { mockHomeRepository.rememberSeasonChange } returns false

        initUnderTest()
        underTest.outputs.rememberSeasonChange.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click show remember season change updates pref and updates value`() = runTest(testDispatcher) {
        every { mockHomeRepository.rememberSeasonChange } returns false

        initUnderTest()
        underTest.outputs.rememberSeasonChange.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Data.rememberSeasonChange(true))

        verify {
            mockHomeRepository.rememberSeasonChange = true
        }
        underTest.outputs.rememberSeasonChange.test { awaitItem() }
    }
}