package tmg.flashback.statistics.ui.dashboard.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewInlineScheduleDayBinding
import tmg.flashback.statistics.databinding.ViewInlineScheduleItemBinding
import tmg.flashback.statistics.ui.dashboard.schedule.viewholders.DayViewHolder
import tmg.flashback.statistics.ui.dashboard.schedule.viewholders.ItemViewHolder
import tmg.utilities.difflist.GenericDiffCallback
import java.lang.RuntimeException

class InlineScheduleAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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