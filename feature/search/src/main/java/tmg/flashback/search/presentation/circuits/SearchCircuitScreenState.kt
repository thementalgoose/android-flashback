package tmg.flashback.search.presentation.circuits

import tmg.flashback.formula1.model.Circuit

data class SearchCircuitScreenState(
    val all: List<Circuit> = emptyList(),
    val filtered: List<Circuit> = emptyList(),
    val searchTerm: String = "",
    val isLoading: Boolean = false
)