package tmg.f1stats.home.season

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.f1stats.R

class SeasonTrackAdapter: RecyclerView.Adapter<SeasonTrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_season_track, parent, false)
        return SeasonTrackViewHolder(view)
    }

    override fun getItemCount(): Int = 20

    override fun onBindViewHolder(holder: SeasonTrackViewHolder, position: Int) {

    }
}

class SeasonTrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

}