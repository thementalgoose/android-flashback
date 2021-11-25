package tmg.flashback.statistics.ui.circuit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.Location
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewCircuitInfoHeaderBinding
import tmg.flashback.statistics.databinding.ViewCircuitInfoRaceBinding
import tmg.flashback.statistics.databinding.ViewCircuitInfoTrackBinding
import tmg.flashback.statistics.ui.circuit.viewholders.HeaderViewHolder
import tmg.flashback.statistics.ui.circuit.viewholders.RaceViewHolder
import tmg.flashback.statistics.ui.circuit.viewholders.TrackViewHolder
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.utilities.difflist.calculateDiff

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
class CircuitInfoAdapter(
    val clickShowOnMap: (location: Location, name: String) -> Unit,
    val clickLink: (link: String) -> Unit,
    val clickRace: (race: CircuitItem.Race) -> Unit
): SyncAdapter<CircuitItem>() {

    override var list: List<CircuitItem> = emptyList()
        set(initialValue) {
            val value = initialValue.addDataProvidedByItem()
            val result = calculateDiff(field, value)
            field = value
            result.dispatchUpdatesTo(this)
        }

    override val providedByAtTopIndex: Int = 1

    override fun dataProvidedItem(syncDataItem: SyncDataItem) = CircuitItem.ErrorItem(syncDataItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_circuit_info_header -> HeaderViewHolder(
                clickShowOnMap,
                clickLink,
                ViewCircuitInfoHeaderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_circuit_info_race -> RaceViewHolder(
                clickRace,
                ViewCircuitInfoRaceBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_circuit_info_track -> TrackViewHolder(
                ViewCircuitInfoTrackBinding.inflate(layoutInflater, parent, false)
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