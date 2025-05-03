package tmg.flashback.search.presentation.drivers

import tmg.flashback.formula1.model.Driver

data class SearchDriverScreenState(
    val all: List<Driver> = emptyList(),
    val filtered: List<Driver> = emptyList(),
    val searchTerm: String = "",
    val isLoading: Boolean = false
)