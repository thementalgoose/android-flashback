package tmg.flashback.season.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.layout_qualifying_time.view.*
import kotlinx.android.synthetic.main.view_qualifying_result.view.*
import tmg.flashback.R
import tmg.flashback.repo.models.RoundQualifyingResult
import tmg.flashback.season.race.RaceAdapterCallback
import tmg.flashback.season.race.RaceAdapterType
import tmg.flashback.season.race.RaceModel
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.gone

class QualifyingResultViewHolder(view: View, private val updateAdapterType: RaceAdapterCallback) :
    RecyclerView.ViewHolder(view), View.OnClickListener {

    init {
        itemView.layoutQ1.setOnClickListener(this)
        itemView.layoutQ2.setOnClickListener(this)
        itemView.layoutQ3.setOnClickListener(this)
        itemView.filterQuali.setOnClickListener(this)
    }

    fun bind(model: RaceModel.Single, type: RaceAdapterType) {
        itemView.apply {

            tvPosition.text = model.qualified.toString()
            layoutDriver.tvName.text = model.driver.name
            layoutDriver.tvNumber.text = model.driver.number.toString()
            layoutDriver.tvNumber.colorHighlight = model.driver.constructor.color
            layoutDriver.imgFlag.setImageResource(R.drawable.gb)
            tvConstructor.text = model.driver.constructor.name
            tvNumber.text = model.driver.number.toString()
            tvNumber.colorHighlight = model.driver.constructor.color

            imgFlag.setImageResource(context.getFlagResourceAlpha3(model.driver.nationalityISO))

            bind(model.q1, layoutQ1)
            bind(model.q2, layoutQ2)
            bind(model.q3, layoutQ3)

            itemView.layoutQ1.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_item else 0)
            itemView.layoutQ2.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_item else 0)
            itemView.layoutQ3.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS) R.drawable.background_qualifying_item else 0)
        }
    }

    private fun bind(qualifying: RoundQualifyingResult?, layout: View): Boolean {
        val label = qualifying?.time?.toString() ?: ""
        layout.tvQualifyingTime.text = label
        layout.tvQualifyingDelta.gone()
        return label.isNotEmpty()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            itemView.layoutQ1 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
            itemView.layoutQ2 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
            itemView.layoutQ3 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
            itemView.filterQuali -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
        }
    }
}