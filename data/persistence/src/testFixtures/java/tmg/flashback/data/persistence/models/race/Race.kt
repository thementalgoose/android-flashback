package tmg.flashback.data.persistence.models.race

import tmg.flashback.data.persistence.models.circuit.Circuit
import tmg.flashback.data.persistence.models.circuit.model
import tmg.flashback.data.persistence.models.overview.Schedule
import tmg.flashback.data.persistence.models.overview.model

fun Race.Companion.model(
    raceInfo: RaceInfo = RaceInfo.model(),
    circuit: Circuit = Circuit.model(),
    qualifying: List<QualifyingDriverResult> = listOf(
        QualifyingDriverResult.model()
    ),
    race: List<RaceDriverResult> = listOf(
        RaceDriverResult.model()
    ),
    sprintQualifying: List<SprintQualifyingDriverResult> = listOf(
        SprintQualifyingDriverResult.model()
    ),
    sprintRace: List<SprintRaceDriverResult> = listOf(
        SprintRaceDriverResult.model()
    ),
    schedule: List<Schedule> = listOf(
        Schedule.model()
    )
): Race = Race(
    raceInfo = raceInfo,
    circuit = circuit,
    qualifying = qualifying,
    race = race,
    sprintQualifying = sprintQualifying,
    sprintRace = sprintRace,
    schedule = schedule
)