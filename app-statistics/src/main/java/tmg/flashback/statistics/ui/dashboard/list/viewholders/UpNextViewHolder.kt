package tmg.flashback.statistics.ui.dashboard.list.viewholders

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.enums.TrackLayout
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewSeasonListUpNextBinding
import tmg.flashback.statistics.ui.dashboard.list.ListItem
import tmg.flashback.statistics.ui.shared.timelist.TimeListAdapter
import tmg.flashback.statistics.ui.shared.timelist.TimeListDisplayType
import tmg.flashback.statistics.ui.shared.timelist.TimeListItem
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
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
    }
}