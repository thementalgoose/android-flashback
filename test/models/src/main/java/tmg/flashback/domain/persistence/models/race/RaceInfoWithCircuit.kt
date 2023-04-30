package tmg.flashback.domain.persistence.models.race

import tmg.flashback.domain.persistence.models.circuit.Circuit
import tmg.flashback.domain.persistence.models.circuit.model

fun RaceInfoWithCircuit.Companion.model(
    raceInfo: RaceInfo = RaceInfo.model(),
    circuit: Circuit = Circuit.model()
): RaceInfoWithCircuit = RaceInfoWithCircuit(
    raceInfo = raceInfo,
    circuit = circuit
)