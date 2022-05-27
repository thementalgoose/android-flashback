package tmg.flashback.stats.ui.weekend.race

import tmg.flashback.formula1.model.RaceRaceResult

sealed class RaceModel(
    val id: String
) {
    object NotAvailableYet: RaceModel("not_available_yet")

    object NotAvailable: RaceModel("not_available")

    object Loading: RaceModel("loading")

    data class Podium(
        val p1: RaceRaceResult,
        val p2: RaceRaceResult,
        val p3: RaceRaceResult
    ): RaceModel(id = "podium") {
        companion object
    }

    data class Result(
        val result: RaceRaceResult
    ): RaceModel(id = result.driver.driver.id) {
        companion object
    }
}