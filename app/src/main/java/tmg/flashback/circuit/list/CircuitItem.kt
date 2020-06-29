package tmg.flashback.circuit.list

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.R
import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.repo.models.stats.CircuitSummary
import tmg.flashback.shared.viewholders.DataUnavailable

sealed class CircuitItem(
    @LayoutRes val layoutId: Int
) {
    data class CircuitInfo(
        val circuit: Circuit
    ): CircuitItem(R.layout.view_circuit_info)

    data class Race(
        val name: String,
        val date: LocalDate,
        val time: LocalTime?,
        val season: Int,
        val round: Int
    ): CircuitItem(R.layout.view_circuit_race)

    object NoNetwork: CircuitItem(R.layout.view_shared_no_network)

    object InternalError: CircuitItem(R.layout.view_shared_internal_error)

    data class Unavailable(
        val type: DataUnavailable
    ): CircuitItem(R.layout.view_shared_data_unavailable)
}