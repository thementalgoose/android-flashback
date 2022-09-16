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
    fun `provided by at top is true when pref is true`() {
        every { mockHomeRepository.dataProvidedByAtTop } returns true

        initUnderTest()
        underTest.outputs.providedByAtTopEnabled.test {
            assertValue(true)
        }
    }

    @Test
    fun `provided by at top is false when pref is false`() {
        every { mockHomeRepository.dataProvidedByAtTop } returns false

        initUnderTest()
        underTest.outputs.providedByAtTopEnabled.test {
            assertValue(false)
        }
    }

    @Test
    fun `click show provided by at top updates pref and updates value`() {
        every { mockHomeRepository.dataProvidedByAtTop } returns false

        initUnderTest()
        val observer = underTest.outputs.providedByAtTopEnabled.testObserve()
        underTest.inputs.prefClicked(Settings.Layout.providedByAtTop(true))

        verify {
            mockHomeRepository.dataProvidedByAtTop = true
        }
        observer.assertEmittedCount(2)
    }
}