package tmg.flashback.dashboard.season.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_season_header.view.*
import tmg.flashback.R
import tmg.flashback.colours
import tmg.flashback.dashboard.season.DashboardSeasonAdapterItem
import tmg.flashback.extensions.ordinalAbbreviation
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.getColor

class DashboardSeasonHeaderViewHolder(
    val listClosed: () -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.ibtnClose.setOnClickListener(this)
    }

    fun bind(item: DashboardSeasonAdapterItem.Header) {

        itemView.tvTitle.text = item.year.toString()

        val label = StringBuilder()
            .apply {
                if (item.raceScheduled > 0) {
                    append(itemView.context.getString(R.string.dashboard_race_not_started, item.raceScheduled.toString()))
                }
                if (item.raceCompleted > 0) {
                    append(itemView.context.getString(R.string.dashboard_race_completed, item.raceCompleted.toString()))
                }
            }
            .toString()
        itemView.races.text = label.fromHtml()

        val progress = item.raceCompleted.toFloat() / (item.raceScheduled + item.raceCompleted).toFloat()
        itemView.pill.backgroundColour = itemView.context.theme.getColor(R.attr.f1BackgroundSecondary)
        itemView.pill.progressColour = ContextCompat.getColor(itemView.context, R.color.colorTheme)
        itemView.pill.setProgress(progress) { "" }
    }

    override fun onClick(p0: View?) {
        listClosed()
    }
}