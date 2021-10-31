package tmg.flashback.statistics.ui.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.RoundQualifyingResult
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutQualifyingTimeBinding
import tmg.flashback.statistics.databinding.ViewRaceQualifyingResultBinding
import tmg.flashback.statistics.ui.race.RaceAdapterCallback
import tmg.flashback.statistics.ui.race.RaceAdapterType
import tmg.flashback.statistics.ui.race.RaceModel
import tmg.flashback.statistics.ui.race.DisplayPrefs
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show
import tmg.utilities.utils.ColorUtils.Companion.darken

class QualifyingResultViewHolder(
    private val binding: ViewRaceQualifyingResultBinding,
    private val updateAdapterType: RaceAdapterCallback
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

    init {
        binding.layoutQ1.root.setOnClickListener(this)
        binding.layoutQ2.root.setOnClickListener(this)
        binding.layoutQ3.root.setOnClickListener(this)
        binding.filterQuali.setOnClickListener(this)

        binding.layoutQ1.root.setOnLongClickListener(this)
        binding.layoutQ2.root.setOnLongClickListener(this)
        binding.layoutQ3.root.setOnLongClickListener(this)
        binding.filterQuali.setOnLongClickListener(this)
    }

    private lateinit var displayPrefs: DisplayPrefs

    fun bind(model: RaceModel.Single, type: RaceAdapterType) {
        this.displayPrefs = model.displayPrefs

        binding.apply {

            when (model.qualified) {
                null -> tvPosition.text = "P"
                -1 -> tvPosition.text = "P"
                0 -> tvPosition.text = "P"
                else -> tvPosition.text = model.qualified.toString()
            }
            layoutDriver.tvName.text = model.driver.name
            layoutDriver.tvNumber.text = model.driver.number.toString()
            layoutDriver.tvNumber.colorHighlight = darken(model.driver.constructor.color)
            layoutDriver.imgFlag.setImageResource(R.drawable.gb)
            tvConstructor.text = model.driver.constructor.name
            layoutDriver.tvNumber.text = model.driver.number.toString()
            layoutDriver.tvNumber.colorHighlight = model.driver.constructor.color

            constructorColor.setBackgroundColor(model.driver.constructor.color)

            if (model.race != null && displayPrefs.penalties && (model.qualified != null && model.qualified != model.race.gridPos && model.race.gridPos > model.qualified)) {
                penalty.show(true)
                penalty.text = getString(R.string.qualifying_grid_penalty, model.race.gridPos - model.qualified, model.race.gridPos.ordinalAbbreviation)
            }
            else {
                penalty.show(false)
            }

            layoutDriver.imgFlag.setImageResource(context.getFlagResourceAlpha3(model.driver.nationalityISO))

            applyTo(model.displayPrefs, type) { q1, q2, q3 ->
                bind(model.q1, q1, model.q1Delta, model.displayPrefs)
                bind(model.q2, q2, model.q2Delta, model.displayPrefs)
                bind(model.q3, q3, model.q3Delta, model.displayPrefs)
            }

            if (model.displayPrefs.none) {
                binding.layoutQ3.tvQualifyingTime.text = itemView.context.getString(R.string.race_qualifying_no_data)
            }
        }
    }

    private fun bind(qualifying: RoundQualifyingResult?, layout: LayoutQualifyingTimeBinding?, delta: String?, displayPrefs: DisplayPrefs): Boolean {
        if (layout == null) return false
        val label = qualifying?.time?.toString() ?: ""
        layout.tvQualifyingTime.text = label

        if (displayPrefs.deltas) {
            layout.tvQualifyingDelta.show()
        }
        else {
            layout.tvQualifyingDelta.gone()
        }
        layout.tvQualifyingDelta.text = ""
        if (delta != null && qualifying?.time?.totalMillis != 0) {
            layout.tvQualifyingDelta.text = delta
        }
        return label.isNotEmpty()
    }

    private fun applyTo(displayPrefs: DisplayPrefs, type: RaceAdapterType, callback: (q1: LayoutQualifyingTimeBinding, q2: LayoutQualifyingTimeBinding?, q3: LayoutQualifyingTimeBinding?) -> Unit) {
        if (displayPrefs.q1 && !displayPrefs.q2 && !displayPrefs.q3) {
            callback(binding.layoutQ3, null, null)
            binding.layoutQ1.root.show(false, isGone = false)
            binding.layoutQ2.root.show(false, isGone = false)
            binding.layoutQ3.root.show(true, isGone = false)
            binding.layoutQ1.root.setBackgroundResource(0)
            binding.layoutQ2.root.setBackgroundResource(0)
            binding.layoutQ3.root.setBackgroundResource(R.drawable.background_qualifying_item)
            return
        }
        if (displayPrefs.q1 && displayPrefs.q2 && !displayPrefs.q3) {
            callback(binding.layoutQ3, binding.layoutQ2, null)
            binding.layoutQ1.root.show(false, isGone = false)
            binding.layoutQ2.root.show(true, isGone = false)
            binding.layoutQ3.root.show(true, isGone = false)
            binding.layoutQ1.root.setBackgroundResource(0)
            binding.layoutQ2.root.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_item else 0)
            binding.layoutQ3.root.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_item else 0)
            return
        }
        callback(binding.layoutQ1, binding.layoutQ2, binding.layoutQ3)
        binding.layoutQ1.root.show(true, isGone = false)
        binding.layoutQ2.root.show(true, isGone = false)
        binding.layoutQ3.root.show(true, isGone = false)
        binding.layoutQ1.root.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_item else 0)
        binding.layoutQ2.root.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_item else 0)
        binding.layoutQ3.root.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS) R.drawable.background_qualifying_item else 0)
        return
    }

    //region View.OnClickListener

    override fun onClick(p0: View?) {
        if (displayPrefs.q1 && displayPrefs.q2 && !displayPrefs.q3) {
            when (p0) {
                binding.layoutQ2.root -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
                binding.layoutQ3.root -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
                binding.filterQuali -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
            }
        }
        else {
            when (p0) {
                binding.layoutQ1.root -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
                binding.layoutQ2.root -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
                binding.layoutQ3.root -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
                binding.filterQuali -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
            }
        }
    }

    override fun onLongClick(p0: View?): Boolean {
        updateAdapterType.toggleQualifyingDeltas(!displayPrefs.deltas)
        return true
    }

    //endregion
}