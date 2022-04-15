package tmg.flashback.statistics.ui.race.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewRaceScheduleListBinding
import tmg.flashback.statistics.ui.race.RaceItem
import tmg.flashback.statistics.ui.shared.schedule.InlineScheduleAdapter
import tmg.utilities.extensions.views.context

@Suppress("EXPERIMENTAL_API_USAGE")
class ScheduleItemViewHolder(
    private val binding: ViewRaceScheduleListBinding
): RecyclerView.ViewHolder(binding.root) {

    private val adapter: InlineScheduleAdapter = InlineScheduleAdapter()

    init {
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)
    }

    fun bind(item: RaceItem.ScheduleMax) {
        adapter.setSchedule(item.schedule)
    }
}