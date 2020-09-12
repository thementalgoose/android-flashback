package tmg.flashback.driver.overview.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_driver_overview_stat.view.*
import tmg.flashback.driver.overview.DriverOverviewItem
import tmg.flashback.driver.season.DriverSeasonItem

class StatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: DriverOverviewItem.Stat) {
        itemView.icon.setImageResource(item.icon)
        itemView.label.setText(item.label)
        itemView.value.text = item.value
    }

    fun bind(item: DriverSeasonItem.Stat) {
        itemView.icon.setImageResource(item.icon)
        itemView.label.setText(item.label)
        itemView.value.text = item.value
    }
}