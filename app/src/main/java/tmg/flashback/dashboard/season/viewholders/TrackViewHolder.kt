package tmg.flashback.dashboard.season.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_season_track.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.dashboard.season.SeasonItem
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.ordinalAbbreviation

class TrackViewHolder(
    val trackClicked: (SeasonItem.Track) -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var data: SeasonItem.Track

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(item: SeasonItem.Track) {
        data = item
        itemView.apply {
            country.setImageResource(context.getFlagResourceAlpha3(item.raceCountryISO))
            when {
                data.hasResults -> {
                    status.setColorFilter(context.theme.getColor(R.attr.f1DeltaNegative))
                    status.setImageResource(R.drawable.race_status_hasdata)
                }
                data.hasQualifying -> {
                    status.setColorFilter(context.theme.getColor(R.attr.f1DeltaWaiting))
                    status.setImageResource(R.drawable.race_status_hasqualifying)
                }
                data.date > LocalDate.now() -> {
                    status.setColorFilter(context.theme.getColor(R.attr.f1DeltaNeutral))
                    status.setImageResource(R.drawable.race_status_nothappened)
                }
                else -> {
                    status.setColorFilter(context.theme.getColor(R.attr.f1DeltaNeutral))
                    status.setImageResource(R.drawable.race_status_waitingfor)
                }
            }
            raceName.text = item.raceName
            circuitName.text = item.circuitName
            raceCountry.text = item.raceCountry
            round.text = context.getString(R.string.race_round, item.round)

            @SuppressLint("SetTextI18n")
            date.text = "${item.date.dayOfMonth.ordinalAbbreviation} ${item.date.format(DateTimeFormatter.ofPattern("MMM yy"))}"
        }
    }

    override fun onClick(p0: View?) {
        trackClicked(data)
    }
}