package tmg.flashback.ui.settings

import org.junit.jupiter.api.Test
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertDataEventValue
import tmg.flashback.testutils.test

internal class SettingsAllViewModelTest: BaseTest() {

    private lateinit var sut: SettingsAllViewModel

    private fun initSUT() {
        sut = SettingsAllViewModel()
    }

    @Test
    fun `init loads all categories`() {
        initSUT()
        sut.outputs.categories.test {
            assertValue(Category.values().toList())
        }
    }

    @Test
    fun `clicking category opens category event`() {

        initSUT()
        sut.inputs.clickCategory(Category.RSS)
        sut.outputs.navigateToo.test {
            assertDataEventValue(Category.RSS)
        }
    }
}