package tmg.flashback.statistics.ui.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.RaceQualifyingType
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewRaceQualifyingHeaderBinding
import tmg.flashback.statistics.ui.race.DisplayPrefs
import tmg.flashback.statistics.ui.race.RaceDisplayType
import tmg.flashback.statistics.ui.race.RaceItem
import tmg.utilities.extensions.views.show

class QualifyingHeaderViewHolder(
    private val orderBy: (raceQualifyingType: RaceQualifyingType) -> Unit,
    private val binding: ViewRaceQualifyingHeaderBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    init {
        binding.vOther.setOnClickListener(this)
        binding.tvQ1.setOnClickListener(this)
        binding.tvQ2.setOnClickListener(this)
        binding.tvQ3.setOnClickListener(this)
    }

    fun bind(model: RaceItem.QualifyingHeader) {
        when {
            model.showQ1 && !model.showQ2 && !model.showQ3 -> {
                binding.tvQ1.show(false, isGone = false)
                binding.tvQ2.show(false, isGone = false)
                binding.tvQ3.show(true, isGone = false)
                binding.tvQ1.setBackgroundResource(0)
                binding.tvQ2.setBackgroundResource(0)
                binding.tvQ3.setBackgroundResource(0)
                binding.tvQ3.text = itemView.context.getString(R.string.qualifying_q1)
            }
            model.showQ1 && model.showQ2 && !model.showQ3 -> {
                binding.tvQ1.show(false, isGone = false)
                binding.tvQ2.show(true, isGone = false)
                binding.tvQ3.show(true, isGone = false)
                binding.tvQ1.setBackgroundResource(0)
                binding.tvQ2.setBackgroundResource(0)
                binding.tvQ3.setBackgroundResource(0)
                binding.tvQ2.text = itemView.context.getString(R.string.qualifying_q1)
                binding.tvQ3.text = itemView.context.getString(R.string.qualifying_q2)
            }
            else -> {
                binding.tvQ1.show(true, isGone = false)
                binding.tvQ2.show(true, isGone = false)
                binding.tvQ3.show(true, isGone = false)
                binding.tvQ1.setBackgroundResource(0)
                binding.tvQ2.setBackgroundResource(0)
                binding.tvQ3.setBackgroundResource(0)
                binding.tvQ1.text = itemView.context.getString(R.string.qualifying_q1)
                binding.tvQ2.text = itemView.context.getString(R.string.qualifying_q2)
                binding.tvQ3.text = itemView.context.getString(R.string.qualifying_q3)
            }
        }
    }

    override fun onClick(p0: View?) {

    }
}