package tmg.flashback.dashboard.search

import org.junit.jupiter.api.Assertions.*
import tmg.flashback.testutils.BaseTest

internal class SearchViewModelTest: BaseTest() {

    lateinit var sut: SearchViewModel

    private fun initSUT() {
        sut = SearchViewModel()
    }
}