package tmg.flashback.domain.persistence.models.overview

import tmg.flashback.domain.persistence.models.circuit.Circuit
import tmg.flashback.domain.persistence.models.circuit.model

fun OverviewWithCircuit.Companion.model(
    overview: Overview = Overview.model(),
    circuit: Circuit = Circuit.model(),
    schedule: List<Schedule> = listOf(
        Schedule.model()
    )
): OverviewWithCircuit = OverviewWithCircuit(
    overview = overview,
    circuit = circuit,
    schedule = schedule
)