package tmg.flashback.statistics.ui.dashboard.season.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.enums.EventType
import tmg.flashback.formula1.model.Event
import tmg.flashback.statistics.databinding.ViewDashboardSeasonEventsBinding
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.utilities.extensions.views.context

class EventsViewHolder(
    private val binding: ViewDashboardSeasonEventsBinding,
    private val eventTypeClicked: (season: Int, type: EventType) -> Unit
): RecyclerView.ViewHolder(binding.root), (PillItem) -> Unit {

    private val adapter: PillAdapter = PillAdapter(
        pillClicked = this
    )
    private lateinit var model: SeasonItem.Events

    init {
        binding.links.adapter = adapter
        binding.links.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun bind(model: SeasonItem.Events) {
        this.model = model
        adapter.list = model.events.keys
            .sortedBy { it.ordinal }
            .map {
                when (it) {
                    EventType.TESTING -> PillItem.EventTypeTesting
                    EventType.CAR_LAUNCH -> PillItem.EventTypeCarLaunches
                    EventType.OTHER -> PillItem.EventTypeOther
                }
            }
    }

    override fun invoke(item: PillItem) {
        when (item) {
            PillItem.EventTypeCarLaunches -> {
                eventTypeClicked(model.season, EventType.CAR_LAUNCH)
            }
            PillItem.EventTypeOther -> {
                eventTypeClicked(model.season, EventType.OTHER)
            }
            PillItem.EventTypeTesting -> {
                eventTypeClicked(model.season, EventType.TESTING)
            }
            else -> { /* Do nothing */ }
        }
    }
}