package tmg.flashback.search.presentation

data class SearchScreenState(
    val category: SearchScreenStateCategory? = null,
    val searchTerm: String = "",
    val showAdvert: Boolean = false,
)

enum class SearchScreenStateCategory {
    DRIVERS,
    CONSTRUCTORS,
    CIRCUITS;
}