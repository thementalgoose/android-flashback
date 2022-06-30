package tmg.flashback.stats.ui.weekend.constructor

import tmg.flashback.formula1.model.Driver

sealed class ConstructorModel(
    val id: String
) {
    object NotAvailableYet: ConstructorModel("not_available_yet")

    object NotAvailable: ConstructorModel("not_available")

    object Loading: ConstructorModel("loading")

    data class Constructor(
        val constructor: tmg.flashback.formula1.model.Constructor,
        val position: Int?,
        val points: Double,
        val drivers: List<Pair<Driver, Double>>,
        val maxTeamPoints: Double
    ): ConstructorModel(id = constructor.id) {
        companion object
    }
}