package tmg.flashback.overviews.viewholders

import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_driver_overview_stat.view.*
import tmg.flashback.overviews.driver.summary.DriverSummaryItem
import tmg.flashback.overviews.driver.season.DriverSeasonItem
import tmg.flashback.utils.getColor
import tmg.utilities.extensions.views.context

class StatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: DriverSummaryItem.Stat) {
        itemView.icon.imageTintList = ColorStateList.valueOf(context.theme.getColor(item.tint))
        itemView.icon.setImageResource(item.icon)
        itemView.label.setText(item.label)
        itemView.value.text = item.value
    }

    fun bind(item: DriverSeasonItem.Stat) {
        itemView.icon.imageTintList = ColorStateList.valueOf(context.theme.getColor(item.tint))
        itemView.icon.setImageResource(item.icon)
        itemView.label.setText(item.label)
        itemView.value.text = item.value
    }
}