package tmg.flashback.standings.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_header.view.*
import tmg.flashback.R
import tmg.flashback.standings.StandingsItem


class StandingsHeaderViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    fun bind(header: StandingsItem.Header) {
        itemView.tvTitle.text = itemView.context.getString(R.string.standings_header, header.season.toString())
    }
}