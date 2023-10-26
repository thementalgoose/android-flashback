package tmg.flashback.weekend.presentation.sprint

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.SprintRaceResult

sealed class SprintModel(
    val id: String
) {

    object NotAvailableYet: SprintModel("not_available_yet")

    object NotAvailable: SprintModel("not_available")

    object Loading: SprintModel("loading")

    data class DriverResult(
        val result: SprintRaceResult
    ): SprintModel(
        id = "driver-${result.entry.driver.id}"
    ) {
        companion object
    }

    data class ConstructorResult(
        val constructor: Constructor,
        val position: Int?,
        val points: Double,
        val drivers: List<Pair<Driver, Double>>,
        val maxTeamPoints: Double,
        val highestDriverPosition: Int
    ): SprintModel(
        id = "constructor-${constructor.id}"
    ) {
        companion object
    }
}