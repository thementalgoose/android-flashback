package tmg.flashback.circuit.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_circuit_track.view.*
import tmg.flashback.circuit.list.CircuitItem

class CircuitInfoTrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: CircuitItem.TrackImage) {
        itemView.circuit.setImageResource(item.trackLayout.icon)
    }
}