package tmg.f1stats.season.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.layout_driver.view.tvNumber
import kotlinx.android.synthetic.main.layout_podium.view.*
import kotlinx.android.synthetic.main.layout_qualifying_time.view.*
import kotlinx.android.synthetic.main.view_qualifying_result.view.*
import kotlinx.android.synthetic.main.view_qualifying_result.view.tvConstructor
import kotlinx.android.synthetic.main.view_qualifying_result.view.tvPosition
import tmg.f1stats.R
import tmg.f1stats.repo.models.RoundQualifyingResult
import tmg.f1stats.repo.models.noTime
import tmg.f1stats.season.race.RaceAdapterCallback
import tmg.f1stats.season.race.RaceAdapterType
import tmg.f1stats.season.race.RaceAdapterViewHolderType
import tmg.f1stats.season.race.RaceModel
import tmg.f1stats.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.gone

class QualifyingResultViewHolder(view: View, val updateAdapterType: RaceAdapterCallback) : RecyclerView.ViewHolder(view), View.OnClickListener {

    init {
        itemView.layoutQ1.setOnClickListener(this)
        itemView.layoutQ2.setOnClickListener(this)
        itemView.layoutQ3.setOnClickListener(this)
        itemView.filterQuali.setOnClickListener(this)
    }

    fun bind(model: RaceModel.Single) {
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
        }
    }

    private fun bind(qualifying: RoundQualifyingResult?, layout: View) {
        layout.apply {

            tvQualifyingTime.text = qualifying?.time?.toString() ?: ""
            tvQualifyingDelta.gone()
        }
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