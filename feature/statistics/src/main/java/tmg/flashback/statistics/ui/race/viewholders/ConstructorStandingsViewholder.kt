package tmg.flashback.statistics.ui.race.viewholders

import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutConstructorDriverBinding
import tmg.flashback.statistics.databinding.ViewRaceConstructorBinding
import tmg.flashback.statistics.ui.race.RaceItem
import tmg.flashback.statistics.ui.util.accessibility.TapToViewConstructorInfoAccessibilityDelegate
import tmg.flashback.ui.extensions.getColor
import tmg.flashback.ui.model.AnimationSpeed
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show
import kotlin.math.roundToInt

class ConstructorStandingsViewholder(
        private val constructorClicked: (constructor: Constructor) -> Unit,
        private val binding: ViewRaceConstructorBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var constructor: Constructor

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(model: RaceItem.Constructor) {

        this.constructor = model.constructor

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
            var maxPercentage = model.points.toFloat() / model.maxTeamPoints.toFloat()
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
                            else -> (it * model.maxTeamPoints.toFloat())
                                .coerceIn(0.0f, model.points.toFloat())
                                .roundToInt()
                                .toString()
                        }
                    }
                }
            }
        }

        // Accessibility
        var contentDescrition = getString(R.string.ab_race_constructor, model.constructor.name, model.points.pointsDisplay())
        for ((driver, points) in model.driver) {
            contentDescrition += getString(R.string.ab_race_constructor_driver, driver.name, points.pointsDisplay())
        }
        binding.container.contentDescription = contentDescrition
        ViewCompat.setAccessibilityDelegate(binding.container, TapToViewConstructorInfoAccessibilityDelegate(model.constructor.name))
    }

    private fun setDriver(layout: LayoutConstructorDriverBinding, driver: Driver, points: Double, @ColorInt constructorColor: Int) {
        layout.tvName.text = driver.name
        layout.tvNumber.text = context.resources.getQuantityString(R.plurals.race_points, points.toInt(), points.pointsDisplay())
        layout.imgFlag.setImageResource(context.getFlagResourceAlpha3(driver.nationalityISO))
    }

    override fun onClick(p0: View?) {
        constructorClicked(constructor)
    }
}