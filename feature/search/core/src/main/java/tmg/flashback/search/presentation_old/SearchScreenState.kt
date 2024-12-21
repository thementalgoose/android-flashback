package tmg.flashback.search.presentation_old

import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.OverviewRace

data class SearchScreenState(
    val drivers: List<Driver> = emptyList(),
    val constructors: List<Constructor> = emptyList(),
    val circuits: List<Circuit> = emptyList(),
    val races: List<OverviewRace> = emptyList(),
    val searchTerm: String = "",
    val isLoading: Boolean = false,
    val showAdvert: Boolean = false,
    val selected: OverviewRace? = null
)