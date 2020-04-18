package tmg.flashback.season.race.viewholders

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_podium.view.*
import kotlinx.android.synthetic.main.layout_podium.view.imgFastestLap
import kotlinx.android.synthetic.main.layout_podium.view.imgStarted
import kotlinx.android.synthetic.main.layout_podium.view.tvConstructor
import kotlinx.android.synthetic.main.layout_podium.view.tvPosition
import kotlinx.android.synthetic.main.layout_podium.view.tvStartedAbsolute
import kotlinx.android.synthetic.main.layout_podium.view.tvStartedRelative
import kotlinx.android.synthetic.main.layout_podium.view.tvTime
import kotlinx.android.synthetic.main.view_race_podium.view.*
import tmg.flashback.R
import tmg.flashback.extensions.stringRes
import tmg.flashback.season.race.RaceModel
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.flashback.utils.podium
import tmg.flashback.utils.positionStarted
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import kotlin.math.abs

class RacePodiumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(first: RaceModel.Single, second: RaceModel.Single, third: RaceModel.Single) {
        bind(first, itemView.layoutFirst)
        bind(second, itemView.layoutSecond)
        bind(third, itemView.layoutThird)

        itemView.tvPoints1.text = itemView.context.getString(R.string.round_podium_points, first.racePoints)
        itemView.tvPoints2.text = itemView.context.getString(R.string.round_podium_points, second.racePoints)
        itemView.tvPoints3.text = itemView.context.getString(R.string.round_podium_points, third.racePoints)
    }

    private fun bind(model: RaceModel.Single, layout: View) {
        layout.apply {

            tvPosition.text = model.racePos.podium()
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
                    imgStarted.setColorFilter(context.theme.getColor(R.attr.f1TextPrimary))
                    tvStartedRelative.setTextColor(context.theme.getColor(R.attr.f1TextPrimary))
                }
                diff > 0 -> { // Gained
                    imgStarted.setImageResource(R.drawable.ic_pos_up)
                    imgStarted.setColorFilter(Color.GREEN)
                    tvStartedRelative.setTextColor(Color.GREEN)
                }
                else -> { // Lost
                    imgStarted.setImageResource(R.drawable.ic_pos_down)
                    imgStarted.setColorFilter(Color.RED)
                    tvStartedRelative.setTextColor(Color.RED)
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
                    tvTime.setText(model.status.stringRes)
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