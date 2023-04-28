package tmg.flashback.weekend.ui.sprint

import tmg.flashback.formula1.model.SprintRaceResult

sealed class SprintModel(
    val id: String
) {

    object NotAvailableYet: SprintModel("not_available_yet")

    object NotAvailable: SprintModel("not_available")

    object Loading: SprintModel("loading")

    data class Result(
        val result: SprintRaceResult
    ): SprintModel(
        id = result.driver.driver.id
    ) {
        companion object
    }
}