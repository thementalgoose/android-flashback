package tmg.flashback.weekend.ui.race

import tmg.flashback.formula1.model.RaceResult

sealed class RaceModel(
    val id: String
) {
    object NotAvailableYet: RaceModel("not_available_yet")

    object NotAvailable: RaceModel("not_available")

    object Loading: RaceModel("loading")

    data class Podium(
        val p1: RaceResult,
        val p2: RaceResult,
        val p3: RaceResult
    ): RaceModel(id = "podium") {
        companion object
    }

    data class Result(
        val result: RaceResult
    ): RaceModel(id = result.driver.driver.id) {
        companion object
    }
}