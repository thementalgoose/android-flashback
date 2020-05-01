package tmg.flashback.race.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_podium.view.*
import kotlinx.android.synthetic.main.layout_podium.view.imgFastestLap
import kotlinx.android.synthetic.main.layout_podium.view.imgStarted
import kotlinx.android.synthetic.main.layout_podium.view.tvConstructor
import kotlinx.android.synthetic.main.layout_podium.view.tvStartedAbsolute
import kotlinx.android.synthetic.main.layout_podium.view.tvStartedRelative
import kotlinx.android.synthetic.main.layout_podium.view.tvTime
import kotlinx.android.synthetic.main.view_race_podium.view.*
import tmg.flashback.R
import tmg.flashback.race.RaceAdapterModel
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.flashback.utils.positionStarted
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import kotlin.math.abs

class RacePodiumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(first: RaceAdapterModel.Single, second: RaceAdapterModel.Single, third: RaceAdapterModel.Single) {
        bind(first, itemView.layoutFirst, itemView.tvPoint1)
        bind(second, itemView.layoutSecond, itemView.tvPoints2)
        bind(third, itemView.layoutThird, itemView.tvPoints3)
    }

    private fun bind(model: RaceAdapterModel.Single, layout: View, pointsLayout: TextView) {
        layout.apply {

            pointsLayout.text = itemView.context.getString(R.string.round_podium_points, model.racePoints)
            tvDriver.text = model.driver.name
            tvNumber.text = model.driver.number.toString()
            tvNumber.colorHighlight = model.driver.constructor.color
            tvConstructor.text = model.driver.constructor.name

            model.driver.photoUrl?.let {
                Glide.with(imgDriver)
                    .load(it)
                    .into(imgDriver)
            }

            // Starting Position
            tvStartedAbsolute.text = model.gridPos.positionStarted(context)
            tvStartedRelative.text = abs(model.racePos - model.gridPos).toString()
            val diff = model.gridPos - model.racePos
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
            imgNationality.setImageResource(context.getFlagResourceAlpha3(model.driver.nationalityISO))

            // Time
            if (model.racePos == 1) {
                tvTime.text = model.raceResult.toString()
            }
            else {
                if (!model.raceResult.noTime) {
                    tvTime.text = context.getString(R.string.race_time_delta, model.raceResult)
                }
                else {
                    tvTime.text = model.status
                }
            }

            if (model.fastestLap) {
                imgFastestLap.visible()
            } else {
                imgFastestLap.gone()
            }
        }
    }
}