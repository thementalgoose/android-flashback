package tmg.flashback.weekend.presentation.race

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.RaceResult

sealed class RaceModel(
    val id: String
) {
    object NotAvailableYet: RaceModel("not_available_yet")

    object NotAvailable: RaceModel("not_available")

    object Loading: RaceModel("loading")

    data class ConstructorResult(
        val constructor: Constructor,
        val position: Int?,
        val points: Double,
        val drivers: List<Pair<Driver, Double>>,
        val maxTeamPoints: Double,
        val highestDriverPosition: Int
    ): RaceModel(id = "constructor-${constructor.id}") {
        companion object
    }

    data class DriverPodium(
        val p1: RaceResult,
        val p2: RaceResult,
        val p3: RaceResult
    ): RaceModel(id = "podium") {
        companion object
    }

    data class DriverResult(
        val result: RaceResult
    ): RaceModel(id = "driver-${result.entry.driver.id}") {
        companion object
    }
}