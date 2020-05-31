package tmg.flashback.race.viewholders

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.view_race_race_result.view.*
import tmg.flashback.R
import tmg.flashback.extensions.iconRes
import tmg.flashback.extensions.toEmptyIfZero
import tmg.flashback.repo.enums.isStatusFinished
import tmg.flashback.race.RaceAdapterModel
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.flashback.utils.position
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import kotlin.math.abs

class RaceResultViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    init {
        itemView.layoutTime.setOnClickListener(this)
    }

    private var status: String = ""

    fun bind(model: RaceAdapterModel.Single) {
        itemView.apply {
            tvPosition.text = model.racePos.toString()
            layoutDriver.tvName.text = model.driver.name
            layoutDriver.tvNumber.gone()
            layoutDriver.imgFlag.gone()
            tvDriverNumber.text = model.driver.number.toString()
            tvDriverNumber.colorHighlight = model.driver.constructor.color
            imgDriverFlag.setImageResource(context.getFlagResourceAlpha3(model.driver.nationalityISO))
            tvConstructor.text = model.driver.constructor.name

            tvPoints.text = model.racePoints.toEmptyIfZero()

            tvStartedAbsolute.text = model.gridPos.position()
            val diff = model.gridPos - model.racePos
            tvStartedRelative.text = abs(diff).toString()
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

            tvTime.text = model.raceResult.toString()
            status = model.status
            when {
                !model.raceResult.noTime -> {
                    tvTime.text = context.getString(R.string.race_time_delta, model.raceResult)
                }
                model.status.isStatusFinished() -> {
                    tvTime.text = model.status
                }
                else -> {
                    tvTime.text = context.getString(R.string.race_status_retired)
                    imgStatus.setImageResource(model.status.iconRes)
                }
            }

            if (model.status.isStatusFinished()) {
                imgStatus.gone()
            } else {
                imgStatus.visible()
            }

            if (model.fastestLap) {
                imgFastestLap.visible()
            } else {
                imgFastestLap.gone()
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            itemView.layoutTime -> {
                if (status.isNotEmpty()) {
                    Toast.makeText(itemView.context, status, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
