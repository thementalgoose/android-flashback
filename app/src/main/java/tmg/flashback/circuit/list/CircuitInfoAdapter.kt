package tmg.flashback.circuit.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.circuit.list.viewholders.CircuitInfoHeaderViewHolder
import tmg.flashback.circuit.list.viewholders.CircuitInfoRaceViewHolder
import tmg.flashback.circuit.list.viewholders.CircuitInfoTrackViewHolder
import tmg.flashback.shared.viewholders.DataUnavailableViewHolder
import tmg.flashback.shared.viewholders.InternalErrorOccurredViewHolder
import tmg.flashback.shared.viewholders.NoNetworkViewHolder
import tmg.flashback.utils.calculateDiff

class CircuitInfoAdapter(
    val clickShowOnMap: () -> Unit,
    val clickWikipedia: () -> Unit,
    val clickRace: (race: CircuitItem.Race) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<CircuitItem> = emptyList()
        set(value) {
            val result = calculateDiff(field, value)
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_circuit_info_header -> CircuitInfoHeaderViewHolder(
                clickShowOnMap,
                clickWikipedia,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_circuit_info_race -> CircuitInfoRaceViewHolder(
                clickRace,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_circuit_info_track -> CircuitInfoTrackViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_data_unavailable -> DataUnavailableViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_no_network -> NoNetworkViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_shared_internal_error -> InternalErrorOccurredViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            else -> throw Exception("Unsupported layout given to onCreateViewHolder. Address this in CircuitItem")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is CircuitItem.CircuitInfo -> (holder as CircuitInfoHeaderViewHolder).bind(item)
            is CircuitItem.Race -> (holder as CircuitInfoRaceViewHolder).bind(item)
            is CircuitItem.TrackImage -> (holder as CircuitInfoTrackViewHolder).bind(item)
            is CircuitItem.Unavailable -> (holder as DataUnavailableViewHolder).bind(item.type)
        }
    }

    override fun getItemViewType(position: Int) = list[position].layoutId

    override fun getItemCount(): Int = list.size
}