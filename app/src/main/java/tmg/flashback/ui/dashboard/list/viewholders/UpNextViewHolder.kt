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
import tmg.flashback.repo.utils.hoursAndMins
import tmg.flashback.repo.utils.secondsBetween
import tmg.flashback.ui.dashboard.list.ListItem
import tmg.flashback.ui.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.*
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.time.hours

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

        item.upNextSchedule.flag?.let {
            itemView.flag.setImageResource(context.getFlagResourceAlpha3(it))
            itemView.flag.visible()
        } ?: run {
            itemView.flag.invisible()
        }

        // If there's only a date available in the timestamp
        event.timestamp.ifDate { date ->
            val days = daysBetween(date, LocalDate.now())
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
            val now = LocalTime.now()

            if (localTime >= now) {
                // Upcoming
                val (hours, minutes) = secondsBetween(now, localTime).hoursAndMins
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
            else {
                val (hoursSinceStart, minutesSinceStart) = secondsBetween(localTime, now).hoursAndMins
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

            itemView.scheduledat.text = getString(
                    R.string.dashboard_up_next_datetime_absolute,
                    localDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy")),
                    localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            ).fromHtml()
        }
    }
}