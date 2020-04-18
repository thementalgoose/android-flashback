package tmg.flashback.season.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_qualifying_header.view.*
import tmg.flashback.R
import tmg.flashback.season.race.RaceAdapterCallback
import tmg.flashback.season.race.RaceAdapterType

class QualifyingHeaderViewHolder(view: View, private val updateAdapterType: RaceAdapterCallback) : RecyclerView.ViewHolder(view), View.OnClickListener {
    init {
        itemView.vOther.setOnClickListener(this)
        itemView.tvQ1.setOnClickListener(this)
        itemView.tvQ2.setOnClickListener(this)
        itemView.tvQ3.setOnClickListener(this)
    }

    fun bind(type: RaceAdapterType) {
        itemView.tvQ1.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_1) R.drawable.background_qualifying_item else 0)
        itemView.tvQ2.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS_2) R.drawable.background_qualifying_item else 0)
        itemView.tvQ3.setBackgroundResource(if (type == RaceAdapterType.QUALIFYING_POS) R.drawable.background_qualifying_item else 0)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            itemView.vOther -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
            itemView.tvQ1 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_1)
            itemView.tvQ2 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS_2)
            itemView.tvQ3 -> updateAdapterType.orderBy(RaceAdapterType.QUALIFYING_POS)
        }
    }
}