package tmg.flashback.statistics.ui.shared.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tmg.flashback.formula1.enums.RaceWeekend
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.utils.NotificationUtils
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.databinding.ViewInlineScheduleDayBinding
import tmg.flashback.statistics.databinding.ViewInlineScheduleItemBinding
import tmg.flashback.statistics.ui.shared.schedule.viewholders.DayViewHolder
import tmg.flashback.statistics.ui.shared.schedule.viewholders.ItemViewHolder
import tmg.utilities.difflist.GenericDiffCallback
import java.lang.RuntimeException

@KoinApiExtension
class InlineScheduleAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent {

    private val notificationController: ScheduleController by inject()

    fun setSchedule(schedule: List<Schedule>) {
        this.list = schedule
            .sortedBy { it.date.atTime(it.time) }
            .groupBy { it.date }
            .map { (date, items) ->
                val list = mutableListOf<InlineSchedule>()
                list.add(InlineSchedule.Day(date))
                list.addAll(items.map {
                    val showBellIndicator = when (NotificationUtils.getCategoryBasedOnLabel(it.label)) {
                        RaceWeekend.FREE_PRACTICE -> notificationController.notificationFreePractice
                        RaceWeekend.QUALIFYING -> notificationController.notificationQualifying
                        RaceWeekend.RACE -> notificationController.notificationRace
                        null -> false
                    }
                    InlineSchedule.Item(
                        label = it.label,
                        date = it.date,
                        time = it.time,
                        showBell = showBellIndicator
                    )
                })
                return@map list
            }
            .flatten()
    }

    var list: List<InlineSchedule> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_inline_schedule_day -> DayViewHolder(
                ViewInlineScheduleDayBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_inline_schedule_item -> ItemViewHolder(
                ViewInlineScheduleItemBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw RuntimeException("View holder item type not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is InlineSchedule.Day -> (holder as DayViewHolder).bind(item)
            is InlineSchedule.Item -> (holder as ItemViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int) = list[position].layoutId

}