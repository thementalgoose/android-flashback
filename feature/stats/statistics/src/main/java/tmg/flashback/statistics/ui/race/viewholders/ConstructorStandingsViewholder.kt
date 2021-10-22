package tmg.flashback.statistics.ui.race.viewholders

import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt
import tmg.core.ui.model.AnimationSpeed
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.statistics.ui.race.RaceModel
import tmg.core.ui.extensions.getColor
import tmg.flashback.data.models.stats.ConstructorDriver
import tmg.flashback.firebase.extensions.pointsDisplay
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutConstructorDriverBinding
import tmg.flashback.statistics.databinding.ViewRaceConstructorBinding
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.show

class ConstructorStandingsViewholder(
        val constructorClicked: (constructorId: String, constructorName: String) -> Unit,
        private val binding: ViewRaceConstructorBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    lateinit var constructorId: String
    lateinit var constructorName: String

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(model: RaceModel.ConstructorStandings, maxPointsByAnyTeam: Double) {

        constructorId = model.constructor.id
        constructorName = model.constructor.name

        binding.apply {
            tvTitle.text = model.constructor.name

            layoutDriver1.root.show(model.driver.isNotEmpty())
            layoutDriver2.root.show(model.driver.size >= 2)
            layoutDriver3.root.show(model.driver.size >= 3)

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
            lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.contentSecondary)
            var maxPercentage = model.points.toFloat() / maxPointsByAnyTeam.toFloat()
            if (maxPercentage.isNaN()) {
                maxPercentage = 0.05f
            }

            when (model.animationSpeed) {
                AnimationSpeed.NONE -> {
                    lpvProgress.setProgress(maxPercentage) { model.points.pointsDisplay() }
                }
                else -> {
                    lpvProgress.timeLimit = model.animationSpeed.millis
                    lpvProgress.animateProgress(maxPercentage) {
                        when (it) {
                            maxPercentage -> model.points.pointsDisplay()
                            0.0f -> "0"
                            else -> (it * maxPointsByAnyTeam.toFloat())
                                .coerceIn(0.0f, model.points.toFloat())
                                .roundToInt()
                                .toString()
                        }
                    }
                }
            }
        }
    }

    private fun setDriver(layout: LayoutConstructorDriverBinding, driver: ConstructorDriver, points: Double, @ColorInt constructorColor: Int) {
        layout.tvName.text = driver.name
        layout.tvNumber.text = context.resources.getQuantityString(R.plurals.race_points, points.toInt(), points.pointsDisplay())
        layout.imgFlag.setImageResource(context.getFlagResourceAlpha3(driver.nationalityISO))
    }

    override fun onClick(p0: View?) {
        constructorClicked(constructorId, constructorName)
    }
}