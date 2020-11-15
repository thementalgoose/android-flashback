package tmg.flashback.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.layout_qualifying_time.view.*
import kotlinx.android.synthetic.main.view_race_qualifying_result.view.*
import tmg.flashback.R
import tmg.flashback.repo.models.stats.RoundQualifyingResult
import tmg.flashback.race.RaceAdapterCallback
import tmg.flashback.race.RaceAdapterType
import tmg.flashback.race.RaceModel
import tmg.flashback.race.ShowQualifying
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show
import tmg.utilities.utils.ColorUtils.Companion.darken

class QualifyingResultViewHolder(view: View, private val updateAdapterType: RaceAdapterCallback) :
    RecyclerView.ViewHolder(view), View.OnClickListener {

    init {
        itemView.layoutQ1.setOnClickListener(this)
        itemView.layoutQ2.setOnClickListener(this)
        itemView.layoutQ3.setOnClickListener(this)
        itemView.filterQuali.setOnClickListener(this)
    }

    private lateinit var showQualifying: ShowQualifying

    fun bind(model: RaceModel.Single, type: RaceAdapterType) {
        this.showQualifying = model.showQualifying

        itemView.apply {

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
            tvNumber.text = model.driver.number.toString()
            tvNumber.colorHighlight = model.driver.constructor.color

            constructorColor.setBackgroundColor(model.driver.constructor.color)

            if (model.race != null && showQualifying.penalties && (model.qualified != null && model.qualified != model.race.gridPos && model.race.gridPos > model.qualified)) {
                penalty.show(true)
                penalty.text = getString(R.string.qualifying_grid_penalty, model.race.gridPos - model.qualified, model.race.gridPos.ordinalAbbreviation)
            }
            else {
                penalty.show(false)
            }

            imgFlag.setImageResource(context.getFlagResourceAlpha3(model.driver.nationalityISO))

            applyTo(model.showQualifying, type) { q1, q2, q3 ->
                bind(model.q1, q1, model.q1Delta, model.showQualifying)
                bind(model.q2, q2, model.q2Delta, model.showQualifying)
                bind(model.q3, q3, model.q3Delta, model.showQualifying)
            }

            if (model.showQualifying.none) {
                itemView.layoutQ3.tvQualifyingTime.text = itemView.context.getString(R.string.race_qualifying_no_data)
            }
        }
    }

    private fun bind(qualifying: RoundQualifyingResult?, layout: View?, delta: String?, showQualifying: ShowQualifying): Boolean {
        if (layout == null) return false
        val label = qualifying?.time?.toString() ?: ""
        layout.tvQualifyingTime.text = label

        if (showQualifying.deltas) {
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

    private fun applyTo(showQualifying: ShowQualifying, type: RaceAdapterType, callback: (q1: View, q2: View?, q3: View?) -> Unit) {
        if (showQualifying.q1 && !showQualifying.q2 && !showQualifying.q3) {
            callback(itemView.layoutQ3, null, null)
            itemView.layoutQ1.show(false, isGone = false)
            itemView.layoutQ2.show(false, isGone = false)
            itemView.layoutQ3.show(true, isGone = false)
            itemView.layoutQ1.setBackgroundResource(0)
            itemView.layoutQ2.setBackgroundResource(0)
            itemView.layoutQ3.setBackgroundResource(R.drawable.background_qualifying_item)
            return
        }
        if (showQualifying.q1 && showQualifying.q2 && !showQualifying.q3) {
            callback(itemView.layoutQ3, itemView.layoutQ2, null)
            itemView.layoutQ1.show(false, isGone = false)
            itemView.layoutQ2.show(true, isGone = false)
            itemView.layoutQ3.show(true, isGone = false)
            itemView.layoutQ1.setBackgroundResource(0)
            itemView.layoutQ2.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_item else 0)
            itemView.layoutQ3.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_item else 0)
            return
        }
        callback(itemView.layoutQ1, itemView.layoutQ2, itemView.layoutQ3)
        itemView.layoutQ1.show(true, isGone = false)
        itemView.layoutQ2.show(true, isGone = false)
        itemView.layoutQ3.show(true, isGone = false)
        itemView.layoutQ1.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_item else 0)
        itemView.layoutQ2.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_item else 0)
        itemView.layoutQ3.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS) R.drawable.background_qualifying_item else 0)
        return
    }

    //region View.OnClickListener

    override fun onClick(p0: View?) {
        if (showQualifying.q1 && showQualifying.q2 && !showQualifying.q3) {
            when (p0) {
                itemView.layoutQ2 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
                itemView.layoutQ3 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
                itemView.filterQuali -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
            }
        }
        else {
            when (p0) {
                itemView.layoutQ1 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
                itemView.layoutQ2 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
                itemView.layoutQ3 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
                itemView.filterQuali -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
            }
        }
    }

    //endregion
}