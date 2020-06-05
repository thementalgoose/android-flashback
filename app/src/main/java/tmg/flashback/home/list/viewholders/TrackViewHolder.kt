package tmg.flashback.home.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_home_track.view.*
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.home.list.HomeItem
import tmg.flashback.utils.SeasonRound
import tmg.flashback.utils.getFlagResourceAlpha3

class TrackViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var data: SeasonRound

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(item: HomeItem.Track) {
        data = SeasonRound(item.season, item.round)
        itemView.apply {
            country.setImageResource(context.getFlagResourceAlpha3(item.raceCountryISO))
            raceName.text = item.raceName
            circuitName.text = item.circuitName
            raceCountry.text = item.raceCountry
            round.text = context.getString(R.string.race_round, item.round)
            date.text = item.date.format(DateTimeFormatter.ofPattern("dd MMMM"))
        }
    }

    override fun onClick(p0: View?) {

    }
}