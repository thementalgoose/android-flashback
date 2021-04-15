package tmg.flashback.statistics.ui.dashboard.list.viewholders

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.ZoneId
import org.threeten.bp.format.TextStyle
import tmg.flashback.core.model.TimeListDisplayType
import tmg.flashback.statistics.enums.TrackLayout
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewSeasonListUpNextBinding
import tmg.flashback.statistics.ui.dashboard.list.ListItem
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.statistics.ui.shared.timelist.TimeListAdapter
import tmg.flashback.statistics.ui.shared.timelist.TimeListItem
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.views.*
import java.util.*

class UpNextViewHolder(
    private val binding: ViewSeasonListUpNextBinding,
    private val timeFilterCallback: (TimeListDisplayType) -> Unit
) : RecyclerView.ViewHolder(binding.root), (PillItem) -> Unit {

    private lateinit var upNext: ListItem.UpNext
    private val adapter = TimeListAdapter()
    private val pillAdapter = PillAdapter(this)

    init {
        binding.timelist.adapter = adapter
        binding.timelist.layoutManager = LinearLayoutManager(binding.root.context)

        binding.pillList.adapter = pillAdapter
        binding.pillList.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ListItem.UpNext) {
        this.upNext = item

        val event = item.upNextSchedule

        binding.title.text = event.title
        binding.subtitle.text = event.subtitle ?: ""
        binding.subtitle.show(event.subtitle != null)
        binding.round.text = getString(R.string.dashboard_up_next_round, event.round.toString())
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

        pillAdapter.list = listOf(
            PillItem.Label(getString(R.string.dashboard_up_next_timefilter_delta), item.timeFormatType == TimeListDisplayType.RELATIVE),
            PillItem.Label(ZoneId.systemDefault().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()), item.timeFormatType == TimeListDisplayType.LOCAL),
            PillItem.Label(getString(R.string.dashboard_up_next_timefilter_utc), item.timeFormatType == TimeListDisplayType.UTC)
        )

        populateTimeList(item.timeFormatType)
    }

    private fun populateTimeList(type: TimeListDisplayType) {
        adapter.list = upNext.upNextSchedule.values
            .sortedBy { it.timestamp.string() }
            .mapIndexed { index, it ->
                TimeListItem(type, index, upNext.upNextSchedule.values.size, it)
            }
    }

    override fun invoke(p1: PillItem) {
        when {
            p1.label.resolve(context) == getString(R.string.dashboard_up_next_timefilter_delta) -> {
                timeFilterCallback(TimeListDisplayType.RELATIVE)
            }
            p1.label.resolve(context) == getString(R.string.dashboard_up_next_timefilter_utc) -> {
                timeFilterCallback(TimeListDisplayType.UTC)
            }
            p1.label.resolve(context) == ZoneId.systemDefault().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()) -> {
                timeFilterCallback(TimeListDisplayType.LOCAL)
            }
        }
    }
}