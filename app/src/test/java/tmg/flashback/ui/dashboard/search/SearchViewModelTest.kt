package tmg.flashback.ui.dashboard.search

import tmg.flashback.testutils.BaseTest

internal class SearchViewModelTest: BaseTest() {

    lateinit var sut: SearchViewModel

    private fun initSUT() {
        sut = SearchViewModel()
    }
}