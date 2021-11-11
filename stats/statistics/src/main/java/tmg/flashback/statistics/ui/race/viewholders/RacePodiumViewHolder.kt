package tmg.flashback.statistics.ui.race.viewholders

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tmg.flashback.statistics.ui.race.RaceModel
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

class RacePodiumViewHolder(
        val driverClicked: (driverId: String, driverName: String) -> Unit,
        private val binding: ViewRaceRacePodiumBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(first: RaceModel.Single, second: RaceModel.Single, third: RaceModel.Single) {
        bind(first, binding.layoutFirst, binding.tvPoint1)
        bind(second, binding.layoutSecond, binding.tvPoints2)
        bind(third, binding.layoutThird, binding.tvPoints3)
    }

    private fun bind(model: RaceModel.Single, layout: LayoutPodiumBinding, pointsLayout: TextView) {
        layout.apply {

            pointsLayout.text = itemView.context.getString(R.string.round_podium_points, model.race?.points?.pointsDisplay() ?: "")
            tvDriver.text = model.driver.driver.name
            tvNumber.text = model.driver.driver.number.toString()
            tvNumber.colorHighlight = model.driver.constructor.color
            tvConstructor.text = model.driver.constructor.name

            constructorColor.setBackgroundColor(model.driver.constructor.color)

            model.driver.driver.photoUrl?.let {
                Glide.with(imgDriver)
                    .load(it)
                    .into(imgDriver)
            }

            imgDriver.setOnClickListener {
                driverClicked(model.driver.driver.id, model.driver.driver.name)
            }

            // Starting Position
            tvStartedAbsolute.text = model.race?.gridPos?.positionStarted(context)
            tvStartedRelative.text = abs((model.race?.pos ?: 0) - (model.race?.gridPos ?: 0)).toString()
            val diff = (model.race?.gridPos ?: 0) - (model.race?.pos ?: 0)
            when {
                diff == 0 -> { // Equal
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
            if (model.race?.pos == 1) {
                tvTime.text = model.race.result.toString()
            }
            else {
                if (model.race?.result?.noTime == false) {
                    tvTime.text = context.getString(R.string.race_time_delta, model.race.result)
                }
                else {
                    tvTime.text = model.race?.status ?: ""
                }
            }

            if (model.race?.fastestLap == true) {
                imgFastestLap.visible()
            } else {
                imgFastestLap.gone()
            }
        }
    }
}