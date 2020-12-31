package tmg.flashback.dashboard.season.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_season_constructor.view.*
import tmg.flashback.R
import tmg.flashback.dashboard.season.SeasonItem
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.shared.driverlist.DriverListAdapter
import tmg.flashback.utils.getColor
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class ConstructorViewHolder(
        private val constructorClicked: (constructor: SeasonItem.Constructor) -> Unit,
        itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
    }

    private lateinit var constructor: SeasonItem.Constructor

    var adapter = DriverListAdapter()

    init {
        itemView.driverList.adapter = adapter
        itemView.driverList.layoutManager = LinearLayoutManager(itemView.context)
    }

    fun bind(item: SeasonItem.Constructor) {

        constructor = item

        val maxPoints = item.maxPointsInSeason

        itemView.tvTitle.text = item.constructor.name

        itemView.lpvProgress.backgroundColour = context.theme.getColor(R.attr.f1BackgroundPrimary)
        itemView.lpvProgress.progressColour = item.constructor.color
        itemView.lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.f1TextSecondary)

        when (item.barAnimation) {
            BarAnimation.NONE -> {
                itemView.lpvProgress.setProgress(item.points.toFloat() / maxPoints.toFloat()) { (it * maxPoints.toFloat()).toInt().coerceIn(0, item.points).toString() }
            }
            else -> {
                itemView.lpvProgress.timeLimit = item.barAnimation.millis
                itemView.lpvProgress.animateProgress(item.points.toFloat() / maxPoints.toFloat()) { (it * maxPoints.toFloat()).toInt().coerceIn(0, item.points).toString() }
            }
        }

        val driverPoints = item.driver.sumBy { it.second }
        itemView.penalty.show(item.points != driverPoints)
        if (item.points < driverPoints) {
            itemView.penalty.text = getString(R.string.home_constructor_penalty, driverPoints - item.points)
        }

        adapter.list = item.driver
    }

    override fun onClick(p0: View?) {
        constructorClicked(constructor)
    }
}