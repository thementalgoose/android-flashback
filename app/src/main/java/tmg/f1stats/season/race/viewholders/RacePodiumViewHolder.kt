package tmg.f1stats.season.race.viewholders

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jwang123.flagkit.FlagKit
import kotlinx.android.synthetic.main.layout_podium.view.*
import kotlinx.android.synthetic.main.layout_podium.view.imgFastestLap
import kotlinx.android.synthetic.main.layout_podium.view.imgStarted
import kotlinx.android.synthetic.main.layout_podium.view.tvConstructor
import kotlinx.android.synthetic.main.layout_podium.view.tvPosition
import kotlinx.android.synthetic.main.layout_podium.view.tvStartedAbsolute
import kotlinx.android.synthetic.main.layout_podium.view.tvStartedRelative
import kotlinx.android.synthetic.main.layout_podium.view.tvTime
import kotlinx.android.synthetic.main.view_race_podium.view.*
import tmg.f1stats.R
import tmg.f1stats.season.race.SeasonRaceModel
import tmg.f1stats.utils.getFlagResourceAlpha3
import tmg.f1stats.utils.podium
import tmg.f1stats.utils.positionStarted
import tmg.f1stats.utils.toAlpha2ISO
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.gone
import kotlin.math.abs

class RacePodiumViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bind(first: SeasonRaceModel, second: SeasonRaceModel, third: SeasonRaceModel) {
        bind(first, itemView.layoutFirst)
        bind(second, itemView.layoutSecond)
        bind(third, itemView.layoutThird)

        itemView.tvPoints1.text = itemView.context.getString(R.string.round_podium_points, first.racePoints)
        itemView.tvPoints2.text = itemView.context.getString(R.string.round_podium_points, second.racePoints)
        itemView.tvPoints3.text = itemView.context.getString(R.string.round_podium_points, third.racePoints)
    }

    private fun bind(model: SeasonRaceModel, layout: View) {
        layout.apply {

            tvPosition.text = model.racePos.podium()
            tvDriver.text = model.driver.name
            tvNumber.text = model.driver.number.toString()
            tvNumber.colorHighlight = model.driver.constructor.color
            tvConstructor.text = model.driver.constructor.name

            // Starting Position
            tvStartedAbsolute.text = model.gridPos.positionStarted(context)
            tvStartedRelative.text = abs(model.racePos - model.gridPos).toString()
            val diff = model.gridPos - model.racePos
            when {
                diff == 0 -> { // Equal
                    imgStarted.setImageResource(R.drawable.ic_pos_neutral)
                    imgStarted.setColorFilter(Color.BLACK)
                    tvStartedRelative.setTextColor(Color.BLACK)
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
            tvTime.text = model.raceResult.toString()
            imgFastestLap.gone()
        }
    }
}