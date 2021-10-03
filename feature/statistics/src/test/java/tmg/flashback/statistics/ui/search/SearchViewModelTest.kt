package tmg.flashback.statistics.ui.search

import io.mockk.mockk
import org.junit.jupiter.api.Test
import tmg.flashback.data.db.stats.SearchRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.test

internal class SearchViewModelTest: BaseTest() {

    private val mockSearchRepository: SearchRepository = mockk(relaxed = true)

    private lateinit var sut: SearchViewModel

    private fun initSUT() {
        sut = SearchViewModel(mockSearchRepository)
    }

    @Test
    fun `open category fires open category event with input category`() {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.CONSTRUCTOR)
        sut.inputs.openCategory()
        sut.outputs.openCategoryPicker.test {
            assertDataEventValue(SearchCategory.CONSTRUCTOR)
        }
    }

    @Test
    fun `open category fires open category event with null default`() {
        initSUT()
        sut.inputs.openCategory()
        sut.outputs.openCategoryPicker.test {
            assertDataEventValue(null)
        }
    }
}