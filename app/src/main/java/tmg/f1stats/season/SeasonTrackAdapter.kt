package tmg.f1stats.season

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_track.view.*
import tmg.f1stats.R

class SeasonTrackAdapter: RecyclerView.Adapter<SeasonTrackViewHolder>() {

    var list: List<SeasonAdapterModel> = listOf(SeasonAdapterModel(-1, -1, "...", "", ""))
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_season_track, parent, false)
        return SeasonTrackViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SeasonTrackViewHolder, position: Int) {
        holder.itemView.apply {
            tvTitle.text = list[position].raceKey
            tvCircuit.text = list[position].circuitName
        }
    }
}

class SeasonTrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

}