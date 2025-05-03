package tmg.flashback.season.model

import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.weekend.navigation.ScreenWeekendData

fun RaceInfo.toScreenWeekendData(): ScreenWeekendData {
    return ScreenWeekendData(
        season = this.season,
        round = this.round,
        raceName = this.name,
        circuitId = this.circuit.id,
        circuitName = this.circuit.name,
        country = this.circuit.country,
        countryISO = this.circuit.countryISO,
        date = this.date,
    )
}