package tmg.flashback.dashboard.season.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_season_header.view.*
import tmg.flashback.R
import tmg.flashback.dashboard.season.DashboardSeasonAdapterItem
import tmg.flashback.extensions.ordinalAbbreviation
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
        itemView.races.text = itemView.context.getString(R.string.dashboard_race_completed, item.raceRound.toString()).fromHtml()

//        itemView.pill.setBackgroundColor()
    }

    override fun onClick(p0: View?) {
        listClosed()
    }
}