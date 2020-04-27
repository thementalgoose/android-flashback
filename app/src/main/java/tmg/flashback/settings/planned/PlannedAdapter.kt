package tmg.flashback.settings.planned

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R

class PlannedAdapter: RecyclerView.Adapter<PlannedViewHolder>() {

    var list: List<PlannedItems> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannedViewHolder {
        return PlannedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_planned, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PlannedViewHolder, position: Int) {
        holder.bind(list[position])
    }

}