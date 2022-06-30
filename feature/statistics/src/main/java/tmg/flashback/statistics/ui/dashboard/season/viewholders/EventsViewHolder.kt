package tmg.flashback.statistics.ui.dashboard.season.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.enums.EventType
import tmg.flashback.formula1.extensions.icon
import tmg.flashback.formula1.extensions.label
import tmg.flashback.formula1.model.Event
import tmg.flashback.statistics.databinding.ViewDashboardSeasonEventsBinding
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.models.StringHolder

class EventsViewHolder(
    private val binding: ViewDashboardSeasonEventsBinding,
    private val eventTypeClicked: (season: Int, type: EventType) -> Unit,
    private val tyresClicked: (season: Int) -> Unit
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
        adapter.list = mutableListOf<PillItem>().apply {
            add(PillItem.Tyres(model.season))
            addAll(model.events.keys
                .sortedBy { it.ordinal }
                .map {
                    PillItem.LabelIcon(
                        _icon = it.icon,
                        string = getString(it.label)
                    )
                })
        }
    }

    override fun invoke(item: PillItem) {
        if (item is PillItem.LabelIcon) {
            val eventType = EventType.values().firstOrNull { it.icon == item.icon } ?: return
            eventTypeClicked(model.season, eventType)
        } else if (item is PillItem.Tyres) {
            tyresClicked(item.year)
        }
    }
}