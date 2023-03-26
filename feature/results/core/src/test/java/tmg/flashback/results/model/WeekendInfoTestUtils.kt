package tmg.flashback.results.model

import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.weekend.contract.model.WeekendInfo


fun WeekendInfo.Companion.from(raceInfo: RaceInfo): WeekendInfo {
    return WeekendInfo(
        season = raceInfo.season,
        round = raceInfo.round,
        raceName = raceInfo.name,
        circuitId = raceInfo.circuit.id,
        circuitName = raceInfo.circuit.name,
        country = raceInfo.circuit.country,
        countryISO = raceInfo.circuit.countryISO,
        date = raceInfo.date,
    )
}