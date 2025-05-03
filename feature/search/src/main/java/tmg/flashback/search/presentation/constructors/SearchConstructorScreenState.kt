package tmg.flashback.search.presentation.constructors

import tmg.flashback.formula1.model.Constructor

data class SearchConstructorScreenState(
    val all: List<Constructor> = emptyList(),
    val filtered: List<Constructor> = emptyList(),
    val searchTerm: String = "",
    val isLoading: Boolean = false
)