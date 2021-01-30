package tmg.flashback.ui.race.viewholders

import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_constructor_driver.view.*
import kotlinx.android.synthetic.main.view_race_constructor.view.*
import tmg.flashback.R
import tmg.flashback.data.enums.BarAnimation
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.ui.race.RaceModel
import tmg.flashback.ui.utils.getColor
import tmg.flashback.ui.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.show

class ConstructorStandingsViewholder(
        val constructorClicked: (constructorId: String, constructorName: String) -> Unit,
        itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    lateinit var constructorId: String
    lateinit var constructorName: String

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(model: RaceModel.ConstructorStandings, maxPointsByAnyTeam: Int) {

        constructorId = model.constructor.id
        constructorName = model.constructor.name

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
            var maxPercentage = model.points.toFloat() / maxPointsByAnyTeam.toFloat()
            if (maxPercentage.isNaN()) {
                maxPercentage = 0.05f
            }

            when (model.barAnimation) {
                BarAnimation.NONE -> {
                    lpvProgress.setProgress(maxPercentage) { model.points.toString() }
                }
                else -> {
                    lpvProgress.timeLimit = model.barAnimation.millis
                    lpvProgress.animateProgress(maxPercentage) {
                        when (it) {
                            maxPercentage -> model.points.toString()
                            0.0f -> "0"
                            else -> (it * maxPointsByAnyTeam.toFloat()).toInt().coerceIn(0, model.points).toString()
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

    override fun onClick(p0: View?) {
        constructorClicked(constructorId, constructorName)
    }
}