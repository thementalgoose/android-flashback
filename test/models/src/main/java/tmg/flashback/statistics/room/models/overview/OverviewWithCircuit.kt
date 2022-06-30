package tmg.flashback.statistics.room.models.overview

import tmg.flashback.statistics.room.models.circuit.Circuit
import tmg.flashback.statistics.room.models.circuit.model

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