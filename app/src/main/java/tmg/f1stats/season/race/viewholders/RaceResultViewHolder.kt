package tmg.f1stats.season.race.viewholders

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.view_race_result.view.*
import tmg.f1stats.R
import tmg.f1stats.extensions.iconRes
import tmg.f1stats.extensions.stringRes
import tmg.f1stats.extensions.toEmptyIfZero
import tmg.f1stats.season.race.RaceModel
import tmg.f1stats.utils.getFlagResourceAlpha3
import tmg.f1stats.utils.position
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import kotlin.math.abs

class RaceResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(model: RaceModel.Single) {
        itemView.apply {
            tvPosition.text = model.racePos.toString()
            layoutDriver.tvName.text = model.driver.name
            layoutDriver.tvNumber.text = model.driver.number.toString()
            layoutDriver.tvNumber.colorHighlight = model.driver.constructor.color
            layoutDriver.imgFlag.setImageResource(context.getFlagResourceAlpha3(model.driver.nationalityISO))
            tvConstructor.text = model.driver.constructor.name

            tvPoints.text = model.racePoints.toEmptyIfZero()

            tvStartedAbsolute.text = model.gridPos.position()
            val diff = model.gridPos - model.racePos
            tvStartedRelative.text = abs(diff).toString()
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

            tvTime.text = model.raceResult.toString()
            if (!model.raceResult.noTime) {
                tvTime.text = context.getString(R.string.race_time_delta, model.raceResult)
            }
            else {
                tvTime.setText(model.status.stringRes)
            }
            imgStatus.setImageResource(model.status.iconRes)

            if (model.fastestLap) {
                imgFastestLap.visible()
            } else {
                imgFastestLap.gone()
            }
        }
    }
}
