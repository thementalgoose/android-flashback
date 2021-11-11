package tmg.flashback.statistics.ui.race.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewRaceOverviewBinding
import tmg.flashback.statistics.ui.race.RaceModel
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class OverviewViewHolder(
    private val pillItemClicked: (PillItem) -> Unit,
    private val binding: ViewRaceOverviewBinding,
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private var linkAdapter: PillAdapter = PillAdapter(pillItemClicked)

    private lateinit var model: RaceModel.Overview

    init {
        binding.links.adapter = linkAdapter
        binding.links.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun bind(item: RaceModel.Overview) {

        this.model = item

        binding.imgCountry.setImageResource(binding.imgCountry.context.getFlagResourceAlpha3(item.countryISO))

        @SuppressLint("SetTextI18n")
        binding.tvCircuitName.text = "${item.circuitName}\n${item.country}"
        binding.tvRoundInfo.text = getString(R.string.race_round, item.round)
        binding.tvDate.text = item.raceDate?.format(DateTimeFormatter.ofPattern("dd MMMM")) ?: ""

        val track = TrackLayout.getTrack(item.circuitId, item.season, item.raceName)
        binding.trackLayout.show(track != null)
        binding.trackLayout.setImageResource(track?.icon ?: 0)
        binding.trackLayout.setOnClickListener(if (track != null) this else null)

        linkAdapter.list = mutableListOf<PillItem>().apply {
            add(PillItem.Circuit(item.circuitId, item.circuitName))
            if (item.wikipedia != null) {
                add(PillItem.Wikipedia(item.wikipedia))
            }
        }
    }

    override fun onClick(p0: View?) {
        pillItemClicked(PillItem.Circuit(model.circuitId, model.circuitName))
    }

}