package tmg.flashback.ui.race.viewholders

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
import kotlinx.android.synthetic.main.view_race_race_podium.view.*
import tmg.flashback.R
import tmg.flashback.ui.race.RaceModel
import tmg.flashback.ui.utils.getColor
import tmg.flashback.ui.utils.getFlagResourceAlpha3
import tmg.flashback.ui.utils.positionStarted
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import kotlin.math.abs

class RacePodiumViewHolder(
        val driverClicked: (driverId: String, driverName: String) -> Unit,
        view: View
) : RecyclerView.ViewHolder(view) {

    fun bind(first: RaceModel.Single, second: RaceModel.Single, third: RaceModel.Single) {
        bind(first, itemView.layoutFirst, itemView.tvPoint1)
        bind(second, itemView.layoutSecond, itemView.tvPoints2)
        bind(third, itemView.layoutThird, itemView.tvPoints3)
    }

    private fun bind(model: RaceModel.Single, layout: View, pointsLayout: TextView) {
        layout.apply {

            pointsLayout.text = itemView.context.getString(R.string.round_podium_points, model.race?.points)
            tvDriver.text = model.driver.name
            tvNumber.text = model.driver.number.toString()
            tvNumber.colorHighlight = model.driver.constructor.color
            tvConstructor.text = model.driver.constructor.name

            constructorColor.setBackgroundColor(model.driver.constructor.color)

            model.driver.photoUrl?.let {
                Glide.with(imgDriver)
                    .load(it)
                    .into(imgDriver)
            }

            imgDriver.setOnClickListener {
                driverClicked(model.driver.id, model.driver.name)
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
            imgNationality.setImageResource(context.getFlagResourceAlpha3(model.driver.nationalityISO))

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