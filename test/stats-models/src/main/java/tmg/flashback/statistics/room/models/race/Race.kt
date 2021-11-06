package tmg.flashback.statistics.room.models.race

import tmg.flashback.statistics.room.models.circuit.Circuit
import tmg.flashback.statistics.room.models.circuit.model

fun Race.Companion.model(
    raceInfo: RaceInfo = RaceInfo.model(),
    circuit: Circuit = Circuit.model(),
    qualifying: List<QualifyingDriverResult> = listOf(
        QualifyingDriverResult.model()
    ),
    race: List<RaceDriverResult> = listOf(
        RaceDriverResult.model()
    )
): Race = Race(
    raceInfo = raceInfo,
    circuit = circuit,
    qualifying = qualifying,
    race = race
)