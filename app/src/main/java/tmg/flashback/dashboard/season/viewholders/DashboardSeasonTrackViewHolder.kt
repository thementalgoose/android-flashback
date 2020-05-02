package tmg.flashback.dashboard.season.viewholders

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_season_track.view.*
import tmg.flashback.R
import tmg.flashback.dashboard.season.DashboardSeasonAdapterItem
import tmg.flashback.utils.SeasonRound
import tmg.flashback.utils.getFlagResource
import tmg.flashback.utils.getFlagResourceAlpha3

class DashboardSeasonTrackViewHolder(
    val itemClickedCallback: (seasonRound: DashboardSeasonAdapterItem.Track) -> Unit,
    itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.clMain.setOnClickListener(this)
    }

    lateinit var seasonRound: DashboardSeasonAdapterItem.Track

    fun bind(item: DashboardSeasonAdapterItem.Track) {
        seasonRound = item
        itemView.tvCountry.text = item.trackNationality
        itemView.tvRaceName.text = item.trackName
        itemView.imgCountry.setImageResource(itemView.context.getFlagResourceAlpha3(item.trackISO))
        itemView.tvRound.text = itemView.context.getString(R.string.race_round, item.round)
    }

    override fun onClick(p0: View?) {
        itemClickedCallback(seasonRound)
    }
}