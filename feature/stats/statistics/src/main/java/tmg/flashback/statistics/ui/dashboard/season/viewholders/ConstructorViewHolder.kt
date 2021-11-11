package tmg.flashback.statistics.ui.dashboard.season.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.ui.model.AnimationSpeed
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.flashback.statistics.ui.shared.driverlist.DriverListAdapter
import tmg.flashback.ui.extensions.getColor
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.statistics.databinding.ViewDashboardSeasonConstructorBinding
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show
import kotlin.math.roundToInt

class ConstructorViewHolder(
    private val constructorClicked: (constructor: SeasonItem.Constructor) -> Unit,
    private val binding: ViewDashboardSeasonConstructorBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.container.setOnClickListener(this)
    }

    private lateinit var constructor: SeasonItem.Constructor

    var adapter = DriverListAdapter()

    init {
        binding.driverList.adapter = adapter
        binding.driverList.layoutManager = LinearLayoutManager(itemView.context)
    }

    fun bind(item: SeasonItem.Constructor) {

        constructor = item

        val maxPoints = item.maxPointsInSeason

        binding.tvTitle.text = item.constructor.name

        binding.lpvProgress.backgroundColour = context.theme.getColor(R.attr.backgroundPrimary)
        binding.lpvProgress.progressColour = item.constructor.color
        binding.lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.contentSecondary)

        var maxProgress = item.points.toFloat() / item.maxPointsInSeason.toFloat()
        if (maxProgress.isNaN()) {
            maxProgress = 0.0f
        }

        when (item.barAnimation) {
            AnimationSpeed.NONE -> {
                binding.lpvProgress.setProgress(maxProgress) { item.points.pointsDisplay() }
            }
            else -> {
                binding.lpvProgress.timeLimit = item.barAnimation.millis
                binding.lpvProgress.animateProgress(maxProgress) {
                    when (it) {
                        maxProgress -> item.points.pointsDisplay()
                        else -> (it * maxPoints.toFloat()).coerceIn(0.0f, item.points.toFloat()).roundToInt().toString()
                    }
                }
            }
        }

        val driverPoints = item.driver.sumOf { it.second }
        if (item.points < driverPoints) {
            binding.penalty.show(true)
            binding.penalty.text = getString(R.string.home_constructor_penalty, (driverPoints - item.points).pointsDisplay())
        } else {
            binding.penalty.show(false)
        }

        adapter.list = item.driver
    }

    override fun onClick(p0: View?) {
        constructorClicked(constructor)
    }
}
