package tmg.flashback.ui.overviews.driver.season.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_driver_season.view.*
import kotlinx.android.synthetic.main.view_driver_season.view.lpvProgress
import tmg.flashback.R
import tmg.flashback.ui.overviews.driver.season.DriverSeasonItem
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.isStatusFinished
import tmg.flashback.ui.utils.getColor
import tmg.flashback.ui.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class RaceViewHolder(
    private val itemClicked: (result: DriverSeasonItem.Result) -> Unit,

    itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
    }

    private lateinit var result: DriverSeasonItem.Result

    fun bind(item: DriverSeasonItem.Result) {

        this.result = item

        itemView.raceName.text = item.raceName
        itemView.country.setImageResource(context.getFlagResourceAlpha3(item.raceCountryISO))
        itemView.circuitName.text = item.circuitName

        if (item.qualified == 0) {
            itemView.qualified.text = getString(R.string.qualifying_no)
        } else {
            itemView.qualified.text = item.qualified.ordinalAbbreviation
        }

        if (item.raceStatus.isStatusFinished()) {
            itemView.finished.text = item.finished?.ordinalAbbreviation
            itemView.raceStatus.text = ""
        } else {
            itemView.finished.text = getString(R.string.race_dnf)
            itemView.raceStatus.text = getString(R.string.race_cause, item.raceStatus)
        }

        if (item.showConstructorLabel) {
            itemView.constructorLabel.show(true)
            itemView.constructorLabel.text =
                getString(R.string.driver_overview_constructor, item.constructor.name)
        } else {
            itemView.constructorLabel.show(false)
        }

        itemView.lpvProgress.backgroundColour =
            itemView.context.theme.getColor(R.attr.f1BackgroundPrimary)
        itemView.lpvProgress.progressColour = item.constructor.color
        itemView.lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.f1TextSecondary)

        var maxProgress = item.points.toFloat() / item.maxPoints.toFloat()
        if (maxProgress.isNaN()) {
            maxProgress = 0.0f
        }

        when (item.barAnimation) {
            BarAnimation.NONE -> {
                itemView.lpvProgress.setProgress(maxProgress) { item.points.toString() }
            }
            else -> {
                itemView.lpvProgress.timeLimit = item.barAnimation.millis

                itemView.lpvProgress.animateProgress(maxProgress) {
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
        itemView.penalty.show(false)
//        }
    }

    override fun onClick(v: View?) {
        itemClicked(result)
    }
}