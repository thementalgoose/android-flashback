package tmg.flashback.statistics.ui.race.viewholders

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tmg.flashback.ui.extensions.getColor
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutPodiumBinding
import tmg.flashback.statistics.databinding.ViewRaceRacePodiumBinding
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.ui.util.positionStarted
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import kotlin.math.abs
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.RaceRaceResult

class RacePodiumViewHolder(
        val driverClicked: (driver: Driver) -> Unit,
        private val binding: ViewRaceRacePodiumBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(first: RaceRaceResult, second: RaceRaceResult, third: RaceRaceResult) {
        bind(first, binding.layoutFirst, binding.tvPoint1)
        bind(second, binding.layoutSecond, binding.tvPoints2)
        bind(third, binding.layoutThird, binding.tvPoints3)
    }

    private fun bind(model: RaceRaceResult, layout: LayoutPodiumBinding, pointsLayout: TextView) {
        layout.apply {

            pointsLayout.text = itemView.context.getString(R.string.round_podium_points, model.points.pointsDisplay())
            tvDriver.text = model.driver.driver.name
            tvNumber.text = model.driver.driver.number?.toString() ?: ""
            tvNumber.colorHighlight = model.driver.constructor.color
            tvConstructor.text = model.driver.constructor.name

            constructorColor.setBackgroundColor(model.driver.constructor.color)

            model.driver.driver.photoUrl?.let {
                Glide.with(imgDriver)
                    .load(it)
                    .into(imgDriver)
            }

            imgDriver.setOnClickListener {
                driverClicked(model.driver.driver)
            }

            // Starting Position
            tvStartedAbsolute.text = model.grid?.positionStarted(context) ?: ""
            tvStartedRelative.text = abs((model.finish) - (model.grid ?: 0)).toString()
            val diff = (model.grid ?: 0) - (model.finish)
            when {
                diff == 0 || model.grid == null -> { // Equal
                    imgStarted.setImageResource(R.drawable.ic_pos_neutral)
                    imgStarted.setColorFilter(context.theme.getColor(R.attr.f1DeltaNeutral))
                    tvStartedRelative.setTextColor(context.theme.getColor(R.attr.f1DeltaNeutral))
                }
                diff > 0 -> { // Gained
                    imgStarted.setImageResource(R.drawable.ic_pos_up)
                    imgStarted.setColorFilter(context.theme.getColor(R.attr.f1DeltaNegative))
                    tvStartedRelative.setTextColor(context.theme.getColor(R.attr.f1DeltaNegative))
                }
                else -> { // Lost
                    imgStarted.setImageResource(R.drawable.ic_pos_down)
                    imgStarted.setColorFilter(context.theme.getColor(R.attr.f1DeltaPositive))
                    tvStartedRelative.setTextColor(context.theme.getColor(R.attr.f1DeltaPositive))
                }
            }

            // Nationality
            imgNationality.setImageResource(context.getFlagResourceAlpha3(model.driver.driver.nationalityISO))

            // Time
            if (model.finish == 1) {
                tvTime.text = model.time.toString()
            }
            else {
                if (model.time?.noTime == false) {
                    tvTime.text = context.getString(R.string.race_time_delta, model.time)
                }
                else {
                    tvTime.text = model.status
                }
            }

            if (model.fastestLap?.rank == 1) {
                imgFastestLap.visible()
            } else {
                imgFastestLap.gone()
            }
        }
    }
}