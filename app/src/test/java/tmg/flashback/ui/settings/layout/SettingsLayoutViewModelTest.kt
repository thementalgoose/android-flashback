package tmg.flashback.ui.settings.layout

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.ui.settings.Settings
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
        underTest.inputs.prefClicked(Settings.Layout.collapseList(true))

        verify {
            mockHomeRepository.dataProvidedByAtTop = true
        }
        observer.assertEmittedCount(2)
    }
}