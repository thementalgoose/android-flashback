package tmg.flashback.statistics.ui.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewRaceQualifyingHeaderBinding
import tmg.flashback.statistics.ui.race.RaceAdapterCallback
import tmg.flashback.statistics.ui.race.RaceAdapterType
import tmg.flashback.statistics.ui.race.DisplayPrefs
import tmg.utilities.extensions.views.show

class QualifyingHeaderViewHolder(
    private val binding: ViewRaceQualifyingHeaderBinding,
    private val updateAdapterType: RaceAdapterCallback
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    init {
        binding.vOther.setOnClickListener(this)
        binding.tvQ1.setOnClickListener(this)
        binding.tvQ2.setOnClickListener(this)
        binding.tvQ3.setOnClickListener(this)
    }

    private lateinit var displayPrefs: DisplayPrefs

    fun bind(displayPrefs: DisplayPrefs, type: RaceAdapterType) {
        this.displayPrefs = displayPrefs
        when {
            !displayPrefs.q2 && !displayPrefs.q3 -> {
                binding.tvQ1.show(false, isGone = false)
                binding.tvQ2.show(false, isGone = false)
                binding.tvQ3.show(true, isGone = false)
                binding.tvQ1.setBackgroundResource(0)
                binding.tvQ2.setBackgroundResource(0)
                binding.tvQ3.setBackgroundResource(R.drawable.background_qualifying_header)
                binding.tvQ3.text = itemView.context.getString(R.string.qualifying_q1)
            }
            displayPrefs.q1 && displayPrefs.q2 && !displayPrefs.q3 -> {
                binding.tvQ1.show(false, isGone = false)
                binding.tvQ2.show(true, isGone = false)
                binding.tvQ3.show(true, isGone = false)
                binding.tvQ1.setBackgroundResource(0)
                binding.tvQ2.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_header else 0)
                binding.tvQ3.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_header else 0)
                binding.tvQ2.text = itemView.context.getString(R.string.qualifying_q1)
                binding.tvQ3.text = itemView.context.getString(R.string.qualifying_q2)
            }
            else -> {
                binding.tvQ1.show(true, isGone = false)
                binding.tvQ2.show(true, isGone = false)
                binding.tvQ3.show(true, isGone = false)
                binding.tvQ1.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_header else 0)
                binding.tvQ2.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_header else 0)
                binding.tvQ3.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS) R.drawable.background_qualifying_header else 0)
                binding.tvQ1.text = itemView.context.getString(R.string.qualifying_q1)
                binding.tvQ2.text = itemView.context.getString(R.string.qualifying_q2)
                binding.tvQ3.text = itemView.context.getString(R.string.qualifying_q3)
            }
        }
    }

    override fun onClick(p0: View?) {
        if (displayPrefs.q1 && displayPrefs.q2 && !displayPrefs.q3) {
            when (p0) {
                binding.vOther -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
                binding.tvQ2 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
                binding.tvQ3 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
            }
        }
        else {
            when (p0) {
                binding.vOther -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
                binding.tvQ1 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
                binding.tvQ2 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
                binding.tvQ3 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
            }
        }
    }
}