package tmg.flashback.statistics.ui.circuit

import androidx.annotation.LayoutRes
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

sealed class CircuitItem(
    @LayoutRes val layoutId: Int
) {
    data class CircuitInfo(
        val circuit: CircuitHistory
    ): CircuitItem(R.layout.view_circuit_info_header)

    data class Race(
        val name: String,
        val date: LocalDate,
        val time: LocalTime?,
        val season: Int,
        val round: Int,
        val circuitName: String,
        val country: String,
        val countryISO: String
    ): CircuitItem(R.layout.view_circuit_info_race)

    data class TrackImage(
        val trackLayout: TrackLayout
    ): CircuitItem(R.layout.view_circuit_info_track)

    data class ErrorItem(
        val item: SyncDataItem
    ): CircuitItem(item.layoutId)

    companion object
}

fun MutableList<CircuitItem>.addError(syncDataItem: SyncDataItem) {
    this.add(CircuitItem.ErrorItem(syncDataItem))
}