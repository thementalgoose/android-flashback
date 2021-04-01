package tmg.flashback.statistics.ui.dashboard.list.viewholders

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.core.model.string
import tmg.flashback.statistics.enums.TrackLayout
import tmg.flashback.data.utils.daysBetween
import tmg.flashback.data.utils.hoursAndMins
import tmg.flashback.data.utils.secondsBetween
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewSeasonListUpNextBinding
import tmg.flashback.statistics.ui.dashboard.list.ListItem
import tmg.flashback.statistics.ui.shared.timelist.TimeListAdapter
import tmg.flashback.statistics.ui.shared.timelist.TimeListDisplayType
import tmg.flashback.statistics.ui.shared.timelist.TimeListItem
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.*

class UpNextViewHolder(
    private val binding: ViewSeasonListUpNextBinding
) : RecyclerView.ViewHolder(binding.root) {

    val adapter = TimeListAdapter()

    init {
        binding.timelist.adapter = adapter
        binding.timelist.layoutManager = LinearLayoutManager(binding.root.context)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ListItem.UpNext) {

        val event = item.upNextSchedule

        binding.title.text = event.title
        binding.subtitle.text = event.subtitle ?: ""
        binding.subtitle.show(event.subtitle != null)
        binding.round.text = event.round.toString()
        binding.round.show(event.round != 0)

        // Track graphic
        val track = TrackLayout.values().firstOrNull { it.circuitId == event.circuitId }?.icon
            ?: R.drawable.ic_map_unknown
        binding.track.setImageResource(track)

        event.flag?.let {
            binding.flag.setImageResource(context.getFlagResourceAlpha3(it))
            binding.flag.visible()
        } ?: run {
            binding.flag.invisible()
        }

        adapter.list = item.upNextSchedule.values
            .map {
                TimeListItem(TimeListDisplayType.RELATIVE, it)
            }
            .sortedBy { it.item.timestamp.string() }

//        // If there's only a date available in the timestamp
//        event.timestamp.ifDate { date ->
//            val days = daysBetween(LocalDate.now(), date)
//            binding.suffix.show(days > 0)
//            binding.spacer.show()
//            binding.countdown.show()
//            if (date == LocalDate.now()) {
//                binding.countdown.text = getString(R.string.dashboard_up_next_date_today)
//                binding.suffix.text = ""
//            }
//            else {
//                binding.countdown.text = days.toString()
//                binding.suffix.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_suffix_days, days)
//            }
//
//            binding.scheduledat.text = getString(
//                    R.string.dashboard_up_next_date_absolute,
//                    date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
//            ).fromHtml()
//        }
//
//        // If there's a date anda timestamp available
//        event.timestamp.ifDateAndTime { utc, local ->
//
//            // TODO: Handle the case where UTC doesn't match local for localdatetime
//
//            val localDate = local.toLocalDate()
//            val localTime = local.toLocalTime()
//            val nowTime = LocalTime.now()
//            val nowDate = LocalDate.now()
//
//            when {
//                localDate > nowDate -> {
//                    val days = daysBetween(nowDate, localDate)
//                    binding.suffix.show(days > 0)
//                    binding.spacer.show()
//                    binding.countdown.show()
//                    if (localDate == nowDate) {
//                        binding.countdown.text = getString(R.string.dashboard_up_next_date_today)
//                        binding.suffix.text = ""
//                    }
//                    else {
//                        binding.countdown.text = days.toString()
//                        binding.suffix.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_suffix_days, days)
//                    }
//                }
//                localTime >= nowTime -> { // Upcoming
//                    val (hours, minutes) = secondsBetween(nowTime, localTime).hoursAndMins
//                    binding.suffix.show(hours != 0 || minutes != 0)
//                    binding.spacer.show()
//                    binding.countdown.show(true)
//                    when {
//                        hours > 12 -> {
//                            binding.countdown.text = getString(R.string.dashboard_up_next_datetime_hour, hours)
//                            binding.suffix.text = getString(R.string.dashboard_up_next_suffix_ontheday)
//                        }
//                        hours > 0 -> {
//                            binding.countdown.text = getString(R.string.dashboard_up_next_datetime_hour_min, hours, minutes)
//                            binding.suffix.text = getString(R.string.dashboard_up_next_suffix_ontheday)
//                        }
//                        minutes > 0 -> {
//                            binding.countdown.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_datetime_mins, minutes, minutes)
//                            binding.suffix.text = getString(R.string.dashboard_up_next_suffix_ontheday)
//                        }
//                        else -> {
//                            binding.countdown.text = getString(R.string.dashboard_up_next_datetime_now)
//                            binding.suffix.text = getString(R.string.dashboard_up_next_suffix_ontheday)
//                        }
//                    }
//                }
//                else -> {
//                    val (hoursSinceStart, minutesSinceStart) = secondsBetween(localTime, nowTime).hoursAndMins
//                    binding.countdown.invisible()
//                    binding.countdown.text = ""
//                    binding.spacer.gone()
//                    binding.suffix.show(true)
//                    when {
//                        hoursSinceStart > 0 -> {
//                            binding.suffix.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_datetime_started_hour, hoursSinceStart, hoursSinceStart)
//                        }
//                        else -> {
//                            binding.suffix.text = context.resources.getQuantityString(R.plurals.dashboard_up_next_datetime_started_mins, minutesSinceStart, minutesSinceStart)
//                        }
//                    }
//                }
//            }
//
//            binding.scheduledat.text = getString(
//                    R.string.dashboard_up_next_datetime_absolute,
//                    localDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy")),
//                    localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
//            ).fromHtml()
//        }
    }
}