package tmg.flashback.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_race_qualifying_header.view.*
import tmg.flashback.R
import tmg.flashback.race.RaceAdapterCallback
import tmg.flashback.race.RaceAdapterType
import tmg.flashback.race.DisplayPrefs
import tmg.utilities.extensions.views.show

class QualifyingHeaderViewHolder(view: View, private val updateAdapterType: RaceAdapterCallback) : RecyclerView.ViewHolder(view), View.OnClickListener {
    init {
        itemView.vOther.setOnClickListener(this)
        itemView.tvQ1.setOnClickListener(this)
        itemView.tvQ2.setOnClickListener(this)
        itemView.tvQ3.setOnClickListener(this)
    }

    private lateinit var displayPrefs: DisplayPrefs

    fun bind(displayPrefs: DisplayPrefs, type: RaceAdapterType) {
        this.displayPrefs = displayPrefs
        when {
            !displayPrefs.q2 && !displayPrefs.q3 -> {
                itemView.tvQ1.show(false, isGone = false)
                itemView.tvQ2.show(false, isGone = false)
                itemView.tvQ3.show(true, isGone = false)
                itemView.tvQ1.setBackgroundResource(0)
                itemView.tvQ2.setBackgroundResource(0)
                itemView.tvQ3.setBackgroundResource(R.drawable.background_qualifying_header)
                itemView.tvQ3.text = itemView.context.getString(R.string.qualifying_q1)
            }
            displayPrefs.q1 && displayPrefs.q2 && !displayPrefs.q3 -> {
                itemView.tvQ1.show(false, isGone = false)
                itemView.tvQ2.show(true, isGone = false)
                itemView.tvQ3.show(true, isGone = false)
                itemView.tvQ1.setBackgroundResource(0)
                itemView.tvQ2.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_header else 0)
                itemView.tvQ3.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_header else 0)
                itemView.tvQ2.text = itemView.context.getString(R.string.qualifying_q1)
                itemView.tvQ3.text = itemView.context.getString(R.string.qualifying_q2)
            }
            else -> {
                itemView.tvQ1.show(true, isGone = false)
                itemView.tvQ2.show(true, isGone = false)
                itemView.tvQ3.show(true, isGone = false)
                itemView.tvQ1.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_header else 0)
                itemView.tvQ2.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_header else 0)
                itemView.tvQ3.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS) R.drawable.background_qualifying_header else 0)
                itemView.tvQ1.text = itemView.context.getString(R.string.qualifying_q1)
                itemView.tvQ2.text = itemView.context.getString(R.string.qualifying_q2)
                itemView.tvQ3.text = itemView.context.getString(R.string.qualifying_q3)
            }
        }
    }

    override fun onClick(p0: View?) {
        if (displayPrefs.q1 && displayPrefs.q2 && !displayPrefs.q3) {
            when (p0) {
                itemView.vOther -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
                itemView.tvQ2 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
                itemView.tvQ3 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
            }
        }
        else {
            when (p0) {
                itemView.vOther -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
                itemView.tvQ1 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
                itemView.tvQ2 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
                itemView.tvQ3 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
            }
        }
    }
}