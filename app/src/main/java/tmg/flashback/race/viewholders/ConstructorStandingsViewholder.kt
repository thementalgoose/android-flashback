package tmg.flashback.race.viewholders

import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_constructor_driver.view.*
import kotlinx.android.synthetic.main.view_race_constructor.view.layoutDriver1
import kotlinx.android.synthetic.main.view_race_constructor.view.layoutDriver2
import kotlinx.android.synthetic.main.view_race_constructor.view.layoutDriver3
import kotlinx.android.synthetic.main.view_race_constructor.view.lpvProgress
import kotlinx.android.synthetic.main.view_race_constructor.view.tvTitle
import tmg.flashback.R
import tmg.flashback.race.RaceModel
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.models.stats.Driver
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.show
import kotlin.math.roundToInt

class ConstructorStandingsViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(model: RaceModel.ConstructorStandings, maxPointsByAnyTeam: Int) {
        itemView.apply {
            tvTitle.text = model.constructor.name

            layoutDriver1.show(model.driver.isNotEmpty())
            layoutDriver2.show(model.driver.size >= 2)
            layoutDriver3.show(model.driver.size >= 3)

            if (model.driver.isNotEmpty()) {
                setDriver(layoutDriver1, model.driver[0].first, model.driver[0].second, model.constructor.color)
            }
            if (model.driver.size >= 2) {
                setDriver(layoutDriver2, model.driver[1].first, model.driver[1].second, model.constructor.color)
            }
            if (model.driver.size >= 3) {
                setDriver(layoutDriver3, model.driver[2].first, model.driver[2].second, model.constructor.color)
            }

            lpvProgress.progressColour = model.constructor.color
            lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.f1TextSecondary)
            val progress = model.points.toFloat() / maxPointsByAnyTeam.toFloat()

            when (model.barAnimation) {
                BarAnimation.NONE -> {
                    lpvProgress.setProgress(progress) {
                        when {
                            progress.isNaN() -> "0"
                            progress != 0.0f -> ((it / progress) * model.points).roundToInt().toString()
                            else -> "0"
                        }
                    }
                }
                else -> {
                    lpvProgress.timeLimit = model.barAnimation.millis
                    lpvProgress.animateProgress(progress) {
                        when {
                            progress.isNaN() -> "0"
                            progress != 0.0f -> ((it / progress) * model.points).roundToInt().toString()
                            else -> "0"
                        }
                    }
                }
            }
        }
    }

    private fun setDriver(layout: View, driver: Driver, points: Int, @ColorInt constructorColor: Int) {
        layout.tvName.text = driver.name
        layout.tvNumber.text = layout.context.resources.getQuantityString(R.plurals.race_points, points, points)
        layout.imgFlag.setImageResource(layout.context.getFlagResourceAlpha3(driver.nationalityISO))
    }
}