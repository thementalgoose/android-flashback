package tmg.flashback.statistics.ui.dashboard.search

import tmg.flashback.statistics.testutils.BaseTest

internal class SearchViewModelTest: BaseTest() {

    lateinit var sut: SearchViewModel

    private fun initSUT() {
        sut = SearchViewModel()
    }
}