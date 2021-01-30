package tmg.flashback.ui.dashboard.list.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_up_next.view.*
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import tmg.flashback.R
import tmg.flashback.constants.TrackLayout
import tmg.flashback.repo.utils.daysBetween
import tmg.flashback.repo.utils.secondsBetween
import tmg.flashback.ui.dashboard.list.ListItem
import tmg.flashback.ui.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.*
import kotlin.math.absoluteValue
import kotlin.math.floor

class UpNextViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bind(item: ListItem.UpNext) {

        itemView.name.text = item.upNextSchedule.name

        if (item.upNextSchedule.circuitName != null) {
            itemView.circuit.visible()
            itemView.circuit.text = item.upNextSchedule.circuitName
        }
        else {
            itemView.circuit.invisible()
        }

        // Track graphic
        when (val track = TrackLayout.values().firstOrNull { it.circuitId == item.upNextSchedule.circuitId }) {
            null -> {
                itemView.track.setImageResource(R.drawable.ic_map_unknown)
            }
            else -> {
                itemView.track.setImageResource(track.icon)
            }
        }

        item.upNextSchedule.flag?.let {
            itemView.flag.setImageResource(context.getFlagResourceAlpha3(it))
            itemView.flag.visible()
        } ?: run {
            itemView.flag.invisible()
        }

        item.upNextSchedule.timestamp.perform(
                ifDateOnly = { date ->
                    if (date == LocalDate.now()) {
                        itemView.days.text = getString(R.string.dashboard_up_next_today)
                        itemView.daysToGoLabel.gone()
                    }
                    else {
                        val days = daysBetween(date, LocalDate.now())
                        itemView.days.text = days.toString()
                        itemView.daysToGoLabel.show()
                        itemView.daysToGoLabel.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_days_to_go, if (days >= 1) days else 1)
                    }
                },
                ifTime = { utc, local ->
                    val localDate = local.toLocalDate()
                    val localTime = local.toLocalTime()
                    if (localDate == LocalDate.now()) {
                        if (localTime > LocalTime.now()) {
                            val seconds = secondsBetween(LocalTime.now(), localTime)
                            when {
                                seconds < 60 -> {
                                    itemView.days.text = getString(R.string.dashboard_up_next_now)
                                    itemView.daysToGoLabel.gone()
                                }
                                seconds < 3600 -> {
                                    itemView.days.text = getString(R.string.dashboard_up_next_time_minutes, floor((seconds % 3600f) / 60f).toInt())
                                    itemView.daysToGoLabel.gone()
                                }
                                else -> {
                                    itemView.days.text = getString(R.string.dashboard_up_next_time_hours, floor(seconds / 3600f).toInt(), floor((seconds % 3600f) / 60f).toInt())
                                    itemView.daysToGoLabel.gone()
                                }
                            }
                        }
                        else {
                            itemView.days.text = getString(R.string.dashboard_up_next_started_past, localTime)
                            itemView.daysToGoLabel.gone()
                        }
                    }
                    else {
                        val days = ChronoUnit.DAYS.between(local.toLocalDate(), LocalDate.now()).toInt().absoluteValue
                        itemView.days.text = days.toString()
                        itemView.daysToGoLabel.show()
                        itemView.daysToGoLabel.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_days_to_go, if (days >= 1) days else 1)
                    }
                }
        )

        // Days to go
        val date = item.upNextSchedule.timestamp.originalDate
        @SuppressLint("SetTextI18n")
        val dateString = "${date.dayOfMonth.ordinalAbbreviation} ${DateTimeFormatter.ofPattern("MMM yyyy").format(date)}"
        if (item.upNextSchedule.round != 0) {
            itemView.date.text = getString(R.string.dashboard_up_next_date, item.upNextSchedule.round, dateString).fromHtml()
        }
        else {
            itemView.date.text = "<br/><b>${dateString}</b>".fromHtml()
        }
    }
}