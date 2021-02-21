package tmg.flashback.ui.dashboard.list.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_up_next.view.*
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.statistics.enums.TrackLayout
import tmg.flashback.data.utils.daysBetween
import tmg.flashback.data.utils.hoursAndMins
import tmg.flashback.data.utils.secondsBetween
import tmg.flashback.ui.dashboard.list.ListItem
import tmg.flashback.ui.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.*

class UpNextViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bind(item: ListItem.UpNext) {

        val event = item.upNextSchedule

        itemView.name.text = event.name
        itemView.circuit.text = event.circuitName ?: ""
        itemView.circuit.show(event.circuitName != null)
        itemView.round.text = event.round.toString()
        itemView.round.show(event.round != 0)

        // Track graphic
        val track = TrackLayout.values().firstOrNull { it.circuitId == event.circuitId }?.icon ?: R.drawable.ic_map_unknown
        itemView.track.setImageResource(track)

        event.flag?.let {
            itemView.flag.setImageResource(context.getFlagResourceAlpha3(it))
            itemView.flag.visible()
        } ?: run {
            itemView.flag.invisible()
        }

        // If there's only a date available in the timestamp
        event.timestamp.ifDate { date ->
            val days = daysBetween(LocalDate.now(), date)
            itemView.suffix.show(days > 0)
            itemView.spacer.show()
            itemView.countdown.show()
            if (date == LocalDate.now()) {
                itemView.countdown.text = getString(R.string.dashboard_up_next_date_today)
                itemView.suffix.text = ""
            }
            else {
                itemView.countdown.text = days.toString()
                itemView.suffix.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_suffix_days, days)
            }

            itemView.scheduledat.text = getString(
                    R.string.dashboard_up_next_date_absolute,
                    date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
            ).fromHtml()
        }

        // If there's a date anda timestamp available
        event.timestamp.ifDateAndTime { utc, local ->

            // TODO: Handle the case where UTC doesn't match local for localdatetime

            val localDate = local.toLocalDate()
            val localTime = local.toLocalTime()
            val nowTime = LocalTime.now()
            val nowDate = LocalDate.now()

            when {
                localDate > nowDate -> {
                    val days = daysBetween(nowDate, localDate)
                    itemView.suffix.show(days > 0)
                    itemView.spacer.show()
                    itemView.countdown.show()
                    if (localDate == nowDate) {
                        itemView.countdown.text = getString(R.string.dashboard_up_next_date_today)
                        itemView.suffix.text = ""
                    }
                    else {
                        itemView.countdown.text = days.toString()
                        itemView.suffix.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_suffix_days, days)
                    }
                }
                localTime >= nowTime -> { // Upcoming
                    val (hours, minutes) = secondsBetween(nowTime, localTime).hoursAndMins
                    itemView.suffix.show(hours != 0 || minutes != 0)
                    itemView.spacer.show()
                    itemView.countdown.show(true)
                    when {
                        hours > 12 -> {
                            itemView.countdown.text = getString(R.string.dashboard_up_next_datetime_hour, hours)
                            itemView.suffix.text = getString(R.string.dashboard_up_next_suffix_ontheday)
                        }
                        hours > 0 -> {
                            itemView.countdown.text = getString(R.string.dashboard_up_next_datetime_hour_min, hours, minutes)
                            itemView.suffix.text = getString(R.string.dashboard_up_next_suffix_ontheday)
                        }
                        minutes > 0 -> {
                            itemView.countdown.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_datetime_mins, minutes, minutes)
                            itemView.suffix.text = getString(R.string.dashboard_up_next_suffix_ontheday)
                        }
                        else -> {
                            itemView.countdown.text = getString(R.string.dashboard_up_next_datetime_now)
                            itemView.suffix.text = getString(R.string.dashboard_up_next_suffix_ontheday)
                        }
                    }
                }
                else -> {
                    val (hoursSinceStart, minutesSinceStart) = secondsBetween(localTime, nowTime).hoursAndMins
                    itemView.countdown.invisible()
                    itemView.countdown.text = ""
                    itemView.spacer.gone()
                    itemView.suffix.show(true)
                    when {
                        hoursSinceStart > 0 -> {
                            itemView.suffix.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_datetime_started_hour, hoursSinceStart, hoursSinceStart)
                        }
                        else -> {
                            itemView.suffix.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_datetime_started_mins, minutesSinceStart, minutesSinceStart)
                        }
                    }
                }
            }

            itemView.scheduledat.text = getString(
                    R.string.dashboard_up_next_datetime_absolute,
                    localDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy")),
                    localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            ).fromHtml()
        }
    }
}