package tmg.flashback.standings.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_standings_header.view.*
import kotlinx.android.synthetic.main.view_standings_header.view.ibtnClose
import kotlinx.android.synthetic.main.view_standings_header.view.pill
import kotlinx.android.synthetic.main.view_standings_header.view.races
import kotlinx.android.synthetic.main.view_standings_header.view.tvTitle
import tmg.flashback.R
import tmg.flashback.standings.StandingsItem
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.getColor


class StandingsHeaderViewHolder(
    private val closed: () -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.ibtnClose.setOnClickListener(this)
    }

    fun bind(header: StandingsItem.Header) {
        itemView.tvTitle.text = itemView.context.getString(R.string.standings_header, header.season.toString())

        val label = StringBuilder()
            .apply {
                if (header.raceScheduled > 0) {
                    append(itemView.context.getString(R.string.dashboard_race_not_started, header.raceScheduled.toString()))
                }
                if (header.raceCompleted > 0) {
                    append(itemView.context.getString(R.string.dashboard_race_completed, header.raceCompleted.toString()))
                }
            }
            .toString()
        itemView.races.text = label.fromHtml()

        val progress = header.raceCompleted.toFloat() / (header.raceScheduled + header.raceCompleted).toFloat()
        itemView.pill.backgroundColour = itemView.context.theme.getColor(R.attr.f1BackgroundSecondary)
        itemView.pill.progressColour = ContextCompat.getColor(itemView.context, R.color.colorTheme)
        itemView.pill.setProgress(progress) { "" }
    }

    override fun onClick(p0: View?) {
        closed()
    }
}