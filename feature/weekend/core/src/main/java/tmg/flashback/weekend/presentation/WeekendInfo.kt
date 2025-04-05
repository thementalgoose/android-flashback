package tmg.flashback.weekend.presentation

import java.time.LocalDate
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.weekend.contract.model.ScreenWeekendData

fun RaceInfo.toWeekendInfo(): ScreenWeekendData {
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

internal val fakeWeekendInfo: ScreenWeekendData = ScreenWeekendData(
    season = 2020,
    round = 1,
    raceName = "Testing Grand Prix",
    circuitId = "silverstone",
    circuitName = "Silverstone",
    country = "Country",
    countryISO = "GB",
    date = LocalDate.of(2020, 1, 1)
)