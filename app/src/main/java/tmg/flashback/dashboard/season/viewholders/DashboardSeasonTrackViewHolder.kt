package tmg.flashback.dashboard.season.viewholders

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_season_track.view.*
import tmg.flashback.dashboard.season.DashboardSeasonAdapterItem
import tmg.flashback.utils.getFlagResource

class DashboardSeasonTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: DashboardSeasonAdapterItem.Track) {
        Log.i("Flashback", "Item binding $item")
        itemView.imgCountry.setImageResource(itemView.context.getFlagResource(item.trackISO))
        itemView.tvRaceName.text = item.trackName
    }
}