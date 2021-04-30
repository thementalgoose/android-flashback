package tmg.flashback.statistics.ui.overview.driver.season.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonItem
import tmg.flashback.data.enums.isStatusFinished
import tmg.flashback.core.extensions.getColor
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewDriverSeasonBinding
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class RaceViewHolder(
    private val itemClicked: (result: DriverSeasonItem.Result) -> Unit,
    private val binding: ViewDriverSeasonBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.container.setOnClickListener(this)
    }

    private lateinit var result: DriverSeasonItem.Result

    fun bind(item: DriverSeasonItem.Result) {

        this.result = item

        binding.raceName.text = item.raceName
        binding.country.setImageResource(context.getFlagResourceAlpha3(item.raceCountryISO))
        binding.circuitName.text = item.circuitName

        if (item.qualified == 0) {
            binding.qualified.text = getString(R.string.qualifying_no)
        } else {
            binding.qualified.text = item.qualified.ordinalAbbreviation
        }

        if (item.raceStatus.isStatusFinished()) {
            binding.finished.text = item.finished?.ordinalAbbreviation
            binding.raceStatus.text = ""
        } else {
            binding.finished.text = getString(R.string.race_dnf)
            binding.raceStatus.text = getString(R.string.race_cause, item.raceStatus)
        }

        if (item.showConstructorLabel) {
            binding.constructorLabel.show(true)
            binding.constructorLabel.text =
                getString(R.string.driver_overview_constructor, item.constructor.name)
        } else {
            binding.constructorLabel.show(false)
        }

        binding.lpvProgress.backgroundColour =
            context.theme.getColor(R.attr.f1BackgroundPrimary)
        binding.lpvProgress.progressColour = item.constructor.color
        binding.lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.f1TextSecondary)

        var maxProgress = item.points.toFloat() / item.maxPoints.toFloat()
        if (maxProgress.isNaN()) {
            maxProgress = 0.0f
        }

        when (item.animationSpeed) {
            AnimationSpeed.NONE -> {
                binding.lpvProgress.setProgress(maxProgress) { item.points.toString() }
            }
            else -> {
                binding.lpvProgress.timeLimit = item.animationSpeed.millis

                binding.lpvProgress.animateProgress(maxProgress) {
                    when (it) {
                        maxProgress -> item.points.toString()
                        else -> (it * item.maxPoints.toFloat()).toInt().coerceIn(0, item.points)
                            .toString()
                    }
                }
            }
        }


//        if (item.qualified != item.gridPos && item.gridPos > item.qualified) {
//            itemView.penalty.show(true)
//            itemView.penalty.text = getString(R.string.qualifying_grid_penalty, item.gridPos - item.qualified, item.gridPos.ordinalAbbreviation)
//        }
//        else {
        binding.penalty.show(false)
//        }
    }

    override fun onClick(v: View?) {
        itemClicked(result)
    }
}