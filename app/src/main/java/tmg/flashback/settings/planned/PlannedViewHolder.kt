package tmg.flashback.settings.planned

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_planned.view.*

class PlannedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: PlannedItems) {
        itemView.apply {
            sPlanned.isChecked = item.isDone
            tvPlanned.setText(item.title)
        }
    }
}