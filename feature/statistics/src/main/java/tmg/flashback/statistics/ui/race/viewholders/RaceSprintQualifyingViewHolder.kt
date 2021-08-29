package tmg.flashback.statistics.ui.race.viewholders

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.ui.race.RaceModel
import tmg.flashback.data.enums.isStatusFinished
import tmg.core.ui.extensions.getColor
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewRaceRaceResultBinding
import tmg.flashback.statistics.extensions.iconRes
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.ui.util.position
import tmg.utilities.extensions.toEmptyIfZero
import tmg.utilities.extensions.views.*
import tmg.utilities.utils.ColorUtils.Companion.darken
import kotlin.math.abs
import tmg.flashback.firebase.extensions.pointsDisplay
import tmg.flashback.statistics.databinding.ViewRaceSprintQualifyingResultBinding

class RaceSprintQualifyingViewHolder(
    val driverClicked: (driverId: String, driverName: String) -> Unit,
    private val binding: ViewRaceSprintQualifyingResultBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.layoutTime.setOnClickListener(this)
        binding.clickTarget.setOnClickListener(this)
    }

    private lateinit var driverId: String
    private lateinit var driverName: String
    private var status: String = ""

    fun bind(model: RaceModel.Single) {

        driverId = model.driver.id
        driverName = model.driver.name

        binding.apply {
            tvPosition.text = model.qSprint?.finish.toString()
            layoutDriver.tvName.text = model.driver.name
            layoutDriver.tvNumber.gone()
            layoutDriver.imgFlag.gone()
            tvDriverNumber.text = model.driver.number.toString()
            tvDriverNumber.colorHighlight = darken(model.driver.constructor.color)
            imgDriverFlag.setImageResource(context.getFlagResourceAlpha3(model.driver.nationalityISO))
            tvConstructor.text = model.driver.constructor.name

            constructorColor.setBackgroundColor(model.driver.constructor.color)

            tvPoints.text = model.qSprint?.points?.pointsDisplay().let {
                if (it == null || it == "0") "" else it
            }

            tvStartedAbsolute.text = model.qSprint?.grid?.position() ?: ""
            val diff = (model.qSprint?.grid ?: 0) - (model.qSprint?.finish ?: 0)
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

            tvTime.text = model.qSprint?.time.toString()
            status = model.qSprint?.status ?: ""
            when {
                model.qSprint?.time?.noTime == false -> {
                    tvTime.text = context.getString(R.string.race_time_delta, model.qSprint.time)
                }
                model.qSprint?.status?.isStatusFinished() == true -> {
                    tvTime.text = model.qSprint.status
                }
                else -> {
                    tvTime.text = context.getString(R.string.race_status_retired)
                    model.qSprint?.status?.let {
                        imgStatus.setImageResource(it.iconRes)
                    }
                }
            }

            if (model.qSprint?.status?.isStatusFinished() == true) {
                imgStatus.gone()
            } else {
                imgStatus.visible()
            }

            // Alpha for DNFs
            if (status.isNotEmpty() && !status.isStatusFinished() && model.displayPrefs.fadeDNF) {
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
                driverClicked(driverId, driverName)
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
