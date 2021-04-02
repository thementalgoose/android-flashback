package tmg.flashback.statistics.ui.dashboard.list.viewholders

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.core.model.TimeListDisplayType
import tmg.flashback.statistics.enums.TrackLayout
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewSeasonListUpNextBinding
import tmg.flashback.statistics.ui.dashboard.list.ListItem
import tmg.flashback.statistics.ui.shared.timelist.TimeListAdapter
import tmg.flashback.statistics.ui.shared.timelist.TimeListItem
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.views.*

class UpNextViewHolder(
    private val binding: ViewSeasonListUpNextBinding,
    private val timeFilterCallback: (TimeListDisplayType) -> Unit
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var upNext: ListItem.UpNext
    private val adapter = TimeListAdapter()

    init {
        binding.timelist.adapter = adapter
        binding.timelist.layoutManager = LinearLayoutManager(binding.root.context)

        binding.timeUTC.setOnClickListener(this)
        binding.timeLocal.setOnClickListener(this)
        binding.timeDelta.setOnClickListener(this)
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

        populateTimeList(item.timeFormatType)
    }

    private fun populateTimeList(type: TimeListDisplayType) {
        adapter.list = upNext.upNextSchedule.values
            .sortedBy { it.timestamp.string() }
            .mapIndexed { index, it ->
                TimeListItem(type, index, upNext.upNextSchedule.values.size, it)
            }

        binding.timeLocal.typeface = if (type == TimeListDisplayType.LOCAL) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        binding.timeUTC.typeface = if (type == TimeListDisplayType.UTC) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        binding.timeDelta.typeface = if (type == TimeListDisplayType.RELATIVE) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.timeUTC -> {
                timeFilterCallback(TimeListDisplayType.UTC)
            }
            binding.timeLocal -> {
                timeFilterCallback(TimeListDisplayType.LOCAL)
            }
            binding.timeDelta -> {
                timeFilterCallback(TimeListDisplayType.RELATIVE)
            }
        }
    }
}