package tmg.flashback.statistics.ui.race.viewholders

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewRaceSprintQualifyingResultBinding
import tmg.flashback.statistics.extensions.iconRes
import tmg.flashback.statistics.ui.race.RaceItem
import tmg.flashback.statistics.ui.util.position
import tmg.flashback.ui.extensions.getColor
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import tmg.utilities.utils.ColorUtils.Companion.darken
import kotlin.math.abs

class RaceSprintQualifyingViewHolder(
    private val driverClicked: (driver: Driver) -> Unit,
    private val binding: ViewRaceSprintQualifyingResultBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.layoutTime.setOnClickListener(this)
        binding.clickTarget.setOnClickListener(this)
    }

    private lateinit var driver: Driver
    private var status: String = ""

    fun bind(model: RaceItem.SprintQualifyingResult) {

        this.driver = model.qSprint.driver.driver
        this.status = model.qSprint.status

        binding.apply {
            tvPosition.text = model.qSprint.finished.toString()
            layoutDriver.tvName.text = driver.name
            layoutDriver.tvNumber.gone()
            layoutDriver.imgFlag.gone()
            tvDriverNumber.text = driver.number?.toString() ?: ""
            tvDriverNumber.colorHighlight = darken(model.qSprint.driver.constructor.color)
            imgDriverFlag.setImageResource(context.getFlagResourceAlpha3(model.qSprint.driver.driver.nationalityISO))
            tvConstructor.text = model.qSprint.driver.constructor.name

            constructorColor.setBackgroundColor(model.qSprint.driver.constructor.color)

            tvPoints.text = model.qSprint.points.pointsDisplay().let {
                if (it == "0") "" else it
            }

            tvStartedAbsolute.text = model.qSprint.gridPos?.position() ?: ""
            val diff = (model.qSprint.gridPos ?: 0) - (model.qSprint.finished)
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

            tvTime.text = model.qSprint.lapTime?.toString() ?: model.qSprint.status
            status = model.qSprint.status
            when {
                model.qSprint.lapTime?.noTime == false -> {
                    tvTime.text = context.getString(R.string.race_time_delta, model.qSprint.lapTime)
                }
                model.qSprint.status.isStatusFinished() -> {
                    tvTime.text = model.qSprint.status
                }
                else -> {
                    tvTime.text = context.getString(R.string.race_status_retired)
                    model.qSprint.status.let {
                        imgStatus.setImageResource(it.iconRes)
                    }
                }
            }

            if (model.qSprint.status.isStatusFinished()) {
                imgStatus.gone()
            } else {
                imgStatus.visible()
            }

            // Alpha for DNFs
            if (status.isNotEmpty() && !status.isStatusFinished()) {
                setAlphaToAllViews(0.55f)
            }
            else {
                setAlphaToAllViews(1.0f)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.layoutTime -> {
                if (status.isNotEmpty() && !status.isStatusFinished()) {
                    Toast.makeText(itemView.context, getString(R.string.race_dnf_cause, status), Toast.LENGTH_SHORT).show()
                }
            }
            binding.clickTarget -> {
                driverClicked(driver)
            }
        }
    }

    private fun setAlphaToAllViews(alpha: Float) {
        binding.tvPosition.alpha = alpha
        binding.layoutDriver.root.alpha = alpha
        binding.tvDriverNumber.alpha = alpha
        binding.imgDriverFlag.alpha = alpha
        binding.tvConstructor.alpha = alpha
        binding.constructorColor.alpha = alpha
        binding.imgStarted.alpha = alpha
        binding.tvPoints.alpha = alpha
        binding.llPosition.alpha = alpha
        binding.llTime.alpha = alpha
    }
}
