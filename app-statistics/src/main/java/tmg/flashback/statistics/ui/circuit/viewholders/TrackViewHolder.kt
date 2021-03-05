package tmg.flashback.statistics.ui.circuit.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewCircuitInfoTrackBinding
import tmg.flashback.statistics.ui.circuit.CircuitItem

class TrackViewHolder(
    private val binding: ViewCircuitInfoTrackBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CircuitItem.TrackImage) {
        binding.circuit.setImageResource(item.trackLayout.icon)
    }
}