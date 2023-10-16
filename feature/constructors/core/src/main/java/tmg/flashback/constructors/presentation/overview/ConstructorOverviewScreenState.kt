package tmg.flashback.constructors.presentation.overview

import tmg.flashback.formula1.model.Constructor

data class ConstructorOverviewScreenState(
    val constructorId: String = "",
    val constructorName: String = "",
    val constructor: Constructor? = null,
    val list: List<ConstructorOverviewModel> = emptyList(),
    val isLoading: Boolean = false,
    val networkError: Boolean = false,
    val selectedSeason: Int? = null
)