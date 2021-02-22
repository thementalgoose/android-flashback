package tmg.flashback.statistics.ui.shared.constructorlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_driver_constructors.view.*
import tmg.flashback.data.models.stats.SlimConstructor

class ConstructorListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(item: SlimConstructor) {
        itemView.constructorColor.setBackgroundColor(item.color)
        itemView.constructor.text = item.name
    }
}