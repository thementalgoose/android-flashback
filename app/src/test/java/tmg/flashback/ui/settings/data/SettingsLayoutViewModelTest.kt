package tmg.flashback.ui.settings.data

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.results.repository.HomeRepository
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.data.SettingsLayoutViewModel
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsLayoutViewModelTest: BaseTest() {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var underTest: SettingsLayoutViewModel

    private fun initUnderTest() {
        underTest = SettingsLayoutViewModel(
            homeRepository = mockHomeRepository
        )
    }

    @Test
    fun `collapse list is true when pref is true`() {
        every { mockHomeRepository.collapseList } returns true

        initUnderTest()
        underTest.outputs.collapsedListEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `collapse list is false when pref is false`() {
        every { mockHomeRepository.collapseList } returns false

        initUnderTest()
        underTest.outputs.collapsedListEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `click show collapse list updates pref and updates value`() {
        every { mockHomeRepository.collapseList } returns false

        initUnderTest()
        val observer = underTest.outputs.collapsedListEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Data.collapseList(true))

        verify {
            mockHomeRepository.collapseList = true
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `empty weeks in schedule is true when pref is true`() {
        every { mockHomeRepository.emptyWeeksInSchedule } returns true

        initUnderTest()
        underTest.outputs.emptyWeeksInSchedule.test {
            assertValue(true)
        }
    }

    @Test
    fun `empty weeks in schedule is false when pref is false`() {
        every { mockHomeRepository.emptyWeeksInSchedule } returns false

        initUnderTest()
        underTest.outputs.emptyWeeksInSchedule.test {
            assertValue(false)
        }
    }

    @Test
    fun `click show empty weeks in schedule updates pref and updates value`() {
        every { mockHomeRepository.emptyWeeksInSchedule } returns false

        initUnderTest()
        val observer = underTest.outputs.emptyWeeksInSchedule.testObserve()
        underTest.inputs.prefClicked(Settings.Data.showEmptyWeeksInSchedule(true))

        verify {
            mockHomeRepository.emptyWeeksInSchedule = true
        }
        observer.assertEmittedCount(2)
    }
}