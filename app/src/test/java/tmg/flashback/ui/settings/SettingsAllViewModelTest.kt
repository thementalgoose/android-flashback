package tmg.flashback.statistics.ui.settings

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.core.controllers.FeatureController
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertDataEventValue
import tmg.flashback.testutils.test

internal class SettingsAllViewModelTest: BaseTest() {

    private var mockFeatureController: FeatureController = mockk(relaxed = true)

    private lateinit var sut: SettingsAllViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockFeatureController.rssEnabled } returns true
    }

    private fun initSUT() {
        sut = SettingsAllViewModel(mockFeatureController)
    }

    @Test
    fun `init loads all categories with rss feature enabled`() {
        initSUT()
        sut.outputs.categories.test {
            assertValue(Category.values().toList())
        }
    }

    @Test
    fun `init loads all categories with rss feature disabled`() {
        every { mockFeatureController.rssEnabled } returns false
        initSUT()
        sut.outputs.categories.test {
            assertValue(
                Category
                    .values()
                    .filter { it != Category.RSS }
                    .toList()
            )
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