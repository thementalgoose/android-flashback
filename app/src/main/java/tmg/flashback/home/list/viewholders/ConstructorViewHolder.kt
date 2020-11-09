package tmg.flashback.home.list.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_constructor_driver.view.*
import kotlinx.android.synthetic.main.view_home_constructor.view.*
import tmg.flashback.R
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.models.stats.Driver
import tmg.flashback.shared.driverlist.DriverListAdapter
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class ConstructorViewHolder(
        private val constructorClicked: (constructorId: String, constructorName: String) -> Unit,
        itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
    }

    private lateinit var constructorId: String
    private lateinit var constructorName: String

    var adapter = DriverListAdapter()

    init {
        itemView.driverList.adapter = adapter
        itemView.driverList.layoutManager = LinearLayoutManager(itemView.context)
    }

    fun bind(item: HomeItem.Constructor) {

        constructorId = item.constructorId
        constructorName = item.constructor.name

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
        constructorClicked(constructorId, constructorName)
    }
}