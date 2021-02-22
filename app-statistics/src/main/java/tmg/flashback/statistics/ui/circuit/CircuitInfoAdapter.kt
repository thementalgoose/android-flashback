package tmg.flashback.statistics.ui.circuit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.core.ui.shared.calculateDiff
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.circuit.viewholders.HeaderViewHolder
import tmg.flashback.statistics.ui.circuit.viewholders.RaceViewHolder
import tmg.flashback.statistics.ui.circuit.viewholders.TrackViewHolder
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
class CircuitInfoAdapter(
    val clickShowOnMap: () -> Unit,
    val clickWikipedia: () -> Unit,
    val clickRace: (race: CircuitItem.Race) -> Unit
): SyncAdapter<CircuitItem>() {

    override var list: List<CircuitItem> = emptyList()
        set(value) {
            val result = calculateDiff(field, value)
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_circuit_info_header -> HeaderViewHolder(
                clickShowOnMap,
                clickWikipedia,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_circuit_info_race -> RaceViewHolder(
                clickRace,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_circuit_info_track -> TrackViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is CircuitItem.CircuitInfo -> (holder as HeaderViewHolder).bind(item)
            is CircuitItem.Race -> (holder as RaceViewHolder).bind(item)
            is CircuitItem.TrackImage -> (holder as TrackViewHolder).bind(item)
            is CircuitItem.ErrorItem -> bindErrors(holder, item.item)
        }
    }

    override fun viewType(position: Int) = list[position].layoutId
}