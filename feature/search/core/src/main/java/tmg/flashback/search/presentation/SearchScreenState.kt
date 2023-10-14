package tmg.flashback.search.presentation

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
    val selected: SearchScreenSubState? = null
)

sealed class SearchScreenSubState {
    data class Driver(
        val driver: tmg.flashback.formula1.model.Driver,
        val season: Int?
    ): SearchScreenSubState()
    data class Constructor(
        val constructor: tmg.flashback.formula1.model.Constructor,
        val season: Int?
    ): SearchScreenSubState()
    data class Circuit(
        val circuit: tmg.flashback.formula1.model.Circuit
    ): SearchScreenSubState()
    data class Race(
        val races: OverviewRace
    ): SearchScreenSubState()
}