package tmg.flashback.statistics.ui.search.category

import org.junit.jupiter.api.Test
import tmg.flashback.statistics.ui.search.SearchCategory
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class CategoryViewModelTest: BaseTest() {

    private lateinit var sut: CategoryViewModel

    private fun initSUT() {
        sut = CategoryViewModel()
    }

    @Test
    fun `init list updates category list with null selected value`() {
        initSUT()
        sut.inputs.initList(null)
        sut.outputs.categories.test {
            assertValue(SearchCategory.values().map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), false)
            })
        }
    }

    @Test
    fun `init list updates category list with valid selected value`() {
        initSUT()
        sut.inputs.initList(SearchCategory.DRIVER)
        sut.outputs.categories.test {
            assertValue(SearchCategory.values().map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == SearchCategory.DRIVER)
            })
        }
    }

    @Test
    fun `clicking category launches category selected event`() {
        initSUT()
        sut.inputs.clickCategory(SearchCategory.CONSTRUCTOR)
        sut.outputs.categorySelected.test {
            assertDataEventValue(SearchCategory.CONSTRUCTOR)
        }
    }
}