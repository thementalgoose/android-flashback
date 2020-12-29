package tmg.flashback.circuit.list

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.R
import tmg.flashback.constants.TrackLayout
import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.shared.sync.SyncDataItem

sealed class CircuitItem(
    @LayoutRes val layoutId: Int
) {
    data class CircuitInfo(
        val circuit: Circuit
    ): CircuitItem(R.layout.view_circuit_info_header)

    data class Race(
        val name: String,
        val date: LocalDate,
        val time: LocalTime?,
        val season: Int,
        val round: Int
    ): CircuitItem(R.layout.view_circuit_info_race)

    data class TrackImage(
        val trackLayout: TrackLayout
    ): CircuitItem(R.layout.view_circuit_info_track)

    data class ErrorItem(
        val item: SyncDataItem
    ): CircuitItem(item.layoutId)
}

fun MutableList<CircuitItem>.addError(syncDataItem: SyncDataItem) {
    this.add(CircuitItem.ErrorItem(syncDataItem))
}