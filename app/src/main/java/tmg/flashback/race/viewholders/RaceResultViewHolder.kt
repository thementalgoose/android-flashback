package tmg.flashback.race.viewholders

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.view_race_race_result.view.*
import tmg.flashback.R
import tmg.flashback.extensions.iconRes
import tmg.flashback.race.RaceAdapterModel
import tmg.flashback.repo.enums.isStatusFinished
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.flashback.utils.position
import tmg.utilities.extensions.toEmptyIfZero
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import tmg.utilities.utils.ColorUtils.Companion.darken
import kotlin.math.abs

class RaceResultViewHolder(
        val driverClicked: (driverId: String, driverName: String) -> Unit,
        view: View
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    init {
        itemView.layoutTime.setOnClickListener(this)
        itemView.clickTarget.setOnClickListener(this)
    }

    private lateinit var driverId: String
    private lateinit var driverName: String
    private var status: String = ""

    fun bind(model: RaceAdapterModel.Single) {

        driverId = model.driver.id
        driverName = model.driver.name

        itemView.apply {
            tvPosition.text = model.race?.pos.toString()
            layoutDriver.tvName.text = model.driver.name
            layoutDriver.tvNumber.gone()
            layoutDriver.imgFlag.gone()
            tvDriverNumber.text = model.driver.number.toString()
            tvDriverNumber.colorHighlight = darken(model.driver.constructor.color)
            imgDriverFlag.setImageResource(context.getFlagResourceAlpha3(model.driver.nationalityISO))
            tvConstructor.text = model.driver.constructor.name

            constructorColor.setBackgroundColor(model.driver.constructor.color)

            tvPoints.text = model.race?.points?.toEmptyIfZero() ?: ""

            tvStartedAbsolute.text = model.race?.gridPos?.position() ?: ""
            val diff = (model.race?.gridPos ?: 0) - (model.race?.pos ?: 0)
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

            tvTime.text = model.race?.result.toString()
            status = model.race?.status ?: ""
            when {
                model.race?.result?.noTime == false -> {
                    tvTime.text = context.getString(R.string.race_time_delta, model.race.result)
                }
                model.race?.status?.isStatusFinished() == true -> {
                    tvTime.text = model.race.status
                }
                else -> {
                    tvTime.text = context.getString(R.string.race_status_retired)
                    model.race?.status?.let {
                        imgStatus.setImageResource(it.iconRes)
                    }
                }
            }

            if (model.race?.status?.isStatusFinished() == true) {
                imgStatus.gone()
            } else {
                imgStatus.visible()
            }

            if (model.race?.fastestLap == true) {
                imgFastestLap.visible()
            } else {
                imgFastestLap.gone()
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            itemView.layoutTime -> {
                if (status.isNotEmpty() && status != "Finished") {
                    Toast.makeText(itemView.context, getString(R.string.race_dnf_cause, status), Toast.LENGTH_SHORT).show()
                }
            }
            itemView.clickTarget -> {
                driverClicked(driverId, driverName)
            }
        }
    }
}
