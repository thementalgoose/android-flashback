package tmg.flashback.overviews.viewholders

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_overview_stat.view.*
import tmg.flashback.overviews.constructor.summary.ConstructorSummaryItem
import tmg.flashback.overviews.driver.summary.DriverSummaryItem
import tmg.flashback.overviews.driver.season.DriverSeasonItem
import tmg.flashback.utils.getColor
import tmg.utilities.extensions.views.context

class StatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: DriverSummaryItem.Stat) {
        this.bindItem(item.tint, item.icon, item.label, item.value)
    }

    fun bind(item: DriverSeasonItem.Stat) {
        this.bindItem(item.tint, item.icon, item.label, item.value)
    }

    fun bind(item: ConstructorSummaryItem.Stat) {
        this.bindItem(item.tint, item.icon, item.label, item.value)
    }

    private fun bindItem(
            @AttrRes tint: Int,
            @DrawableRes drawable: Int,
            @StringRes label: Int,
            value: String
    ) {
        itemView.icon.imageTintList = ColorStateList.valueOf(context.theme.getColor(tint))
        itemView.icon.setImageResource(drawable)
        itemView.label.setText(label)
        itemView.value.text = value
    }
}