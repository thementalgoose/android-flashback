package tmg.flashback.upnext.ui.timelist.viewholders

import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.configuration.repository.models.TimeListDisplayType
import tmg.flashback.data.utils.daysBetween
import tmg.flashback.data.utils.secondsBetween
import tmg.flashback.upnext.R
import tmg.flashback.upnext.databinding.ViewUpNextTimeListBinding
import tmg.flashback.upnext.extensions.secondsToHHmm
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString

class TimeViewHolder(
    private val binding: ViewUpNextTimeListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: tmg.flashback.upnext.ui.timelist.TimeListItem) {

        if (!item.item.timestamp.isInPast || item.itemInList == item.totalList - 1) {
            binding.container.alpha = 1.0f
        }
        else {
            binding.container.alpha = 0.5f
        }

        binding.title.text = item.item.label

        item.item.timestamp.ifDate { date ->
            when (item.type) {
                TimeListDisplayType.RELATIVE -> {
                    val days = daysBetween(LocalDate.now(), date)
                    if (date == LocalDate.now()) {
                        binding.dates.text = getString(R.string.dashboard_up_next_date_today)
                    } else {
                        binding.dates.text = context.resources.getQuantityString(
                            R.plurals.dashboard_up_next_suffix_days,
                            days,
                            days
                        )
                    }
                }
                else -> {
                    val ordinal = date.dayOfMonth.ordinalAbbreviation
                    binding.dates.text = date.format(DateTimeFormatter.ofPattern("'${ordinal}' MMM"))
                }
            }
        }

        item.item.timestamp.ifDateAndTime { utc, local ->
            when (item.type) {
                TimeListDisplayType.UTC -> {
                    val ordinal = utc.dayOfMonth.ordinalAbbreviation
                    val date = utc.format(DateTimeFormatter.ofPattern("'${ordinal}' MMM"))
                    val time = utc.format(DateTimeFormatter.ofPattern("HH:mm"))
                    binding.dates.text = getString(R.string.dashboard_up_next_date_date_on_time, time, date)
                }
                TimeListDisplayType.LOCAL -> {
                    val ordinal = local.dayOfMonth.ordinalAbbreviation
                    val date = local.format(DateTimeFormatter.ofPattern("'${ordinal}' MMM"))
                    val time = local.format(DateTimeFormatter.ofPattern("HH:mm"))
                    binding.dates.text = getString(R.string.dashboard_up_next_date_date_on_time, time, date)
                }
                TimeListDisplayType.RELATIVE -> {
                    val localDate = local.toLocalDate()
                    val localTime = local.toLocalTime()
                    val nowTime = LocalTime.now()
                    val nowDate = LocalDate.now()

                    if (localDate > nowDate) {
                        val days = daysBetween(nowDate, localDate)
                        if (localDate == nowDate) {
                            binding.dates.text = getString(R.string.dashboard_up_next_date_today)
                        } else {
                            binding.dates.text = context.resources.getQuantityString(
                                R.plurals.dashboard_up_next_suffix_days,
                                days,
                                days
                            )
                        }
                    }
                    else if (localDate < nowDate) {
                        val days = daysBetween(localDate, nowDate)
                        binding.dates.text = context.resources.getQuantityString(
                            R.plurals.dashboard_up_next_datetime_started_days,
                            days,
                            days
                        )
                    }
                    else if (localTime >= nowTime) {
                        val (hours, minutes) = secondsBetween(nowTime, localTime).secondsToHHmm
                        when {
                            hours > 12 -> {
                                binding.dates.text = getString(R.string.dashboard_up_next_datetime_hour, hours)
                            }
                            hours > 0 -> {
                                binding.dates.text = getString(
                                    R.string.dashboard_up_next_datetime_hour_min,
                                    hours,
                                    minutes
                                )
                            }
                            minutes > 0 -> {
                                binding.dates.text = context.resources.getQuantityString(
                                    R.plurals.dashboard_up_next_datetime_mins,
                                    minutes,
                                    minutes
                                )
                            }
                            else -> {
                                binding.dates.text = getString(R.string.dashboard_up_next_datetime_now)
                            }
                        }
                    }
                    else {
                        val (hoursSinceStart, minutesSinceStart) = secondsBetween(
                            localTime,
                            nowTime
                        ).secondsToHHmm

                        when {
                            hoursSinceStart > 0 -> {
                                binding.dates.text = context.resources.getQuantityString(
                                    R.plurals.dashboard_up_next_datetime_started_hour,
                                    hoursSinceStart,
                                    hoursSinceStart
                                )
                            }
                            else -> {
                                binding.dates.text = context.resources.getQuantityString(
                                    R.plurals.dashboard_up_next_datetime_started_mins,
                                    minutesSinceStart,
                                    minutesSinceStart
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}