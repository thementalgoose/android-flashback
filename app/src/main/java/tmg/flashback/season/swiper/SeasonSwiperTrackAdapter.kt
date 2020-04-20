package tmg.flashback.season.swiper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.view_swiper_track.view.*
import tmg.flashback.R
import tmg.flashback.utils.getFlagResourceAlpha3

class SeasonTrackAdapter: RecyclerView.Adapter<SeasonTrackViewHolder>() {

    var list: List<SeasonSwiperAdapterModel> = listOf(
        SeasonSwiperAdapterModel(-1, -1, "...", "", "")
    )
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(0, parent, false)
        return SeasonTrackViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SeasonTrackViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.apply {
//            imgCountry.setImageResource(context.getFlagResourceAlpha3(item.raceKey))
//            tvTitle.text = item.raceKey
//            tvCircuit.text = item.circuitName
        }
    }
}

class SeasonTrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

}