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
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class ConstructorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var adapter = ConstructorViewHolderDriverAdapter()

    init {
        itemView.driverList.adapter = adapter
        itemView.driverList.layoutManager = LinearLayoutManager(itemView.context)
    }

    fun bind(item: HomeItem.Constructor) {

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
}

class ConstructorViewHolderDriverAdapter: RecyclerView.Adapter<ConstructorViewHolderDriverAdapter.ViewHolder>() {

    var list: List<Pair<Driver, Int>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_constructor_driver, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: Pair<Driver, Int>) {
            val (driver, points) = item
            itemView.tvName.text = driver.name
            itemView.imgFlag.setImageResource(itemView.context.getFlagResourceAlpha3(driver.nationalityISO))
            itemView.tvNumber.text = itemView.context.resources.getQuantityString(R.plurals.race_points, points, points)
        }
    }
}