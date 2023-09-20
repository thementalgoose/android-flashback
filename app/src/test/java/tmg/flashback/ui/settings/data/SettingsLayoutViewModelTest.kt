package tmg.flashback.ui.settings.data

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.results.repository.HomeRepository
import tmg.flashback.ui.settings.Settings
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
}