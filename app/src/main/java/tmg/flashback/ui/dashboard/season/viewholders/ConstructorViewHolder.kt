package tmg.flashback.ui.dashboard.season.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_season_constructor.view.*
import tmg.flashback.R
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.ui.dashboard.season.SeasonItem
import tmg.flashback.ui.shared.driverlist.DriverListAdapter
import tmg.flashback.ui.utils.getColor
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

        var maxProgress = item.points.toFloat() / item.maxPointsInSeason.toFloat()
        if (maxProgress.isNaN()) {
            maxProgress = 0.0f
        }

        when (item.barAnimation) {
            AnimationSpeed.NONE -> {
                itemView.lpvProgress.setProgress(maxProgress) { item.points.toString() }
            }
            else -> {
                itemView.lpvProgress.timeLimit = item.barAnimation.millis
                itemView.lpvProgress.animateProgress(maxProgress) {
                    when (it) {
                        maxProgress -> item.points.toString()
                        else -> (it * maxPoints.toFloat()).toInt().coerceIn(0, item.points).toString()
                    }
                }
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