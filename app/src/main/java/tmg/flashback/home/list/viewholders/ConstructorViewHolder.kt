package tmg.flashback.home.list.viewholders

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_constructor_driver.view.*
import kotlinx.android.synthetic.main.view_home_constructor.view.*
import tmg.flashback.R
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.models.stats.Driver
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.show

class ConstructorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: HomeItem.Constructor) {

        val maxPoints = item.maxPointsInSeason

        itemView.tvTitle.text = item.constructor.name

        itemView.layoutDriver1.show(item.driver.isNotEmpty())
        itemView.layoutDriver2.show(item.driver.size >= 2)
        itemView.layoutDriver3.show(item.driver.size >= 3)
        itemView.layoutDriver4.show(item.driver.size >= 4)

        item.driver.getOrNull(0)?.let { setDriver(itemView.layoutDriver1, it) }
        item.driver.getOrNull(1)?.let { setDriver(itemView.layoutDriver2, it) }
        item.driver.getOrNull(2)?.let { setDriver(itemView.layoutDriver3, it) }
        item.driver.getOrNull(3)?.let { setDriver(itemView.layoutDriver4, it) }

        itemView.lpvProgress.backgroundColour = context.theme.getColor(R.attr.f1BackgroundPrimary)
        itemView.lpvProgress.progressColour = item.constructor.color
        itemView.lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.f1TextSecondary)

        itemView.lpvProgress.animateProgress(item.points.toFloat() / maxPoints.toFloat()) { (it * maxPoints.toFloat()).toInt().coerceIn(0, item.points).toString() }
    }

    fun setDriver(layout: View, driverResult: Pair<Driver, Int>) {
        layout.tvName.text = driverResult.first.name
        layout.imgFlag.setImageResource(itemView.context.getFlagResourceAlpha3(driverResult.first.nationalityISO))
        layout.tvNumber.text = itemView.context.resources.getQuantityString(R.plurals.race_points, driverResult.second, driverResult.second)
    }
}