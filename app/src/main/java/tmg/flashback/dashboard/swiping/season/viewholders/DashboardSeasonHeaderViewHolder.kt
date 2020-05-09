package tmg.flashback.dashboard.swiping.season.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_season_header.view.*
import tmg.flashback.dashboard.swiping.season.DashboardSeasonAdapterItem

class DashboardSeasonHeaderViewHolder(
    val listClosed: () -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.ibtnClose.setOnClickListener(this)
    }

    fun bind(item: DashboardSeasonAdapterItem.Header) {
        itemView.tvTitle.text = item.year.toString()
    }

    override fun onClick(p0: View?) {
        listClosed()
    }
}