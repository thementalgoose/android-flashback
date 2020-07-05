package tmg.flashback.circuit.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.circuit.list.viewholders.CircuitInfoViewHolder
import tmg.flashback.circuit.list.viewholders.CircuitRaceViewHolder
import tmg.flashback.circuit.list.viewholders.CircuitTrackViewHolder
import tmg.flashback.home.list.HomeItem
import tmg.flashback.shared.viewholders.*
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
            R.layout.view_circuit_info -> CircuitInfoViewHolder(
                clickShowOnMap,
                clickWikipedia,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_circuit_race -> CircuitRaceViewHolder(
                clickRace,
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
            R.layout.view_circuit_track -> CircuitTrackViewHolder(
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
            is CircuitItem.CircuitInfo -> (holder as CircuitInfoViewHolder).bind(item)
            is CircuitItem.Race -> (holder as CircuitRaceViewHolder).bind(item)
            is CircuitItem.TrackImage -> (holder as CircuitTrackViewHolder).bind(item)
            is CircuitItem.Unavailable -> (holder as DataUnavailableViewHolder).bind(item.type)
        }
    }

    override fun getItemViewType(position: Int) = list[position].layoutId

    override fun getItemCount(): Int = list.size
}