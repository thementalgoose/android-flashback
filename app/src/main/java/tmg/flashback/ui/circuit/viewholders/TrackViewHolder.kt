package tmg.flashback.ui.circuit.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_circuit_info_track.view.*
import tmg.flashback.ui.circuit.CircuitItem

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: CircuitItem.TrackImage) {
        itemView.circuit.setImageResource(item.trackLayout.icon)
    }
}