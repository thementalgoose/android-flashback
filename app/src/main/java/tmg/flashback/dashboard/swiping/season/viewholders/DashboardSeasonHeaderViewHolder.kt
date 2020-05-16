package tmg.flashback.dashboard.swiping.season.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_season_header.view.*
import tmg.flashback.R
import tmg.flashback.dashboard.swiping.season.DashboardSeasonAdapterItem
import tmg.utilities.extensions.fromHtml

class DashboardSeasonHeaderViewHolder(
    val listClosed: () -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.ibtnClose.setOnClickListener(this)
    }

    fun bind(item: DashboardSeasonAdapterItem.Header) {

        itemView.tvTitle.text = item.year.toString()
        itemView.season.text = itemView.context.getString(R.string.dashboard_season_th, item.season.toString()).fromHtml()
        itemView.races.text = itemView.context.getString(R.string.dashboard_race_completed, item.raceRound.toString()).fromHtml()

//        itemView.pill.setBackgroundColor()
    }

    override fun onClick(p0: View?) {
        listClosed()
    }
}