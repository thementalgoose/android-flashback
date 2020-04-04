package tmg.f1stats.season.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.layout_qualifying_time.view.*
import kotlinx.android.synthetic.main.view_qualifying_result.view.*
import tmg.f1stats.R
import tmg.f1stats.repo.models.RoundQualifyingResult
import tmg.f1stats.repo.models.noTime
import tmg.f1stats.season.race.RaceModel
import tmg.utilities.extensions.views.gone

class QualifyingResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(model: RaceModel) {
        itemView.apply {

            tvPosition.text = model.gridPos.toString()
            layoutDriver.tvName.text = model.driver.name
            layoutDriver.tvNumber.text = model.driver.number.toString()
            layoutDriver.tvNumber.colorHighlight = model.driver.constructor.color
            layoutDriver.imgFlag.setImageResource(R.drawable.gb)
            tvConstructor.text = model.driver.constructor.name

            bind(model.q1, layoutQ1)
            bind(model.q2, layoutQ2)
            bind(model.q3, layoutQ3)
        }
    }

    private fun bind(qualifying: RoundQualifyingResult?, layout: View) {
        layout.apply {

            tvQualifyingTime.text = qualifying?.time?.toString() ?: noTime.toString()
            tvQualifyingDelta.gone()
        }
    }
}