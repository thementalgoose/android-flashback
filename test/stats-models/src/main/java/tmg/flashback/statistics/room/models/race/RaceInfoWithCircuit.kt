package tmg.flashback.statistics.room.models.race

import tmg.flashback.statistics.room.models.circuit.Circuit
import tmg.flashback.statistics.room.models.overview.model

fun RaceInfoWithCircuit.Companion.model(
    raceInfo: RaceInfo = RaceInfo.model(),
    circuit: Circuit = Circuit.model()
): RaceInfoWithCircuit = RaceInfoWithCircuit(
    raceInfo = raceInfo,
    circuit = circuit
)