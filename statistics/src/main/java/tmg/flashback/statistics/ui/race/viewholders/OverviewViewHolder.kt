package tmg.flashback.statistics.ui.race.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewRaceOverviewBinding
import tmg.flashback.statistics.ui.race.RaceItem
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class OverviewViewHolder(
    private val pillItemClicked: (PillItem) -> Unit,
    private val binding: ViewRaceOverviewBinding,
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private var linkAdapter: PillAdapter = PillAdapter(pillItemClicked)

    private lateinit var model: RaceItem.Overview

    init {
        binding.links.adapter = linkAdapter
        binding.links.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun bind(item: RaceItem.Overview) {

        this.model = item

        binding.imgCountry.setImageResource(binding.imgCountry.context.getFlagResourceAlpha3(item.countryISO))

        @SuppressLint("SetTextI18n")
        binding.tvCircuitName.text = "${item.circuitName}\n${item.country}"
        binding.tvRoundInfo.text = getString(R.string.race_round, item.round)
        binding.tvDate.text = when (val date = item.raceDate) {
            null -> ""
            else -> "${item.raceDate.dayOfMonth.ordinalAbbreviation} ${item.raceDate.format(DateTimeFormatter.ofPattern("MMMM"))}"
        }

        val track = TrackLayout.getTrack(item.circuitId, item.season, item.raceName)
        binding.trackLayout.show(track != null)
        binding.trackLayout.setImageResource(track?.icon ?: 0)
        binding.trackLayout.setOnClickListener(if (track != null) this else null)

        linkAdapter.list = mutableListOf<PillItem>().apply {
            if (item.laps != null) {
                add(PillItem.LabelIcon(
                    string = getString(R.string.circuit_info_laps, item.laps),
                    _icon = R.drawable.ic_laps
                ))
            }
            if (item.wikipedia != null) {
                add(PillItem.Wikipedia(item.wikipedia))
            }
            if (item.youtube != null) {
                add(PillItem.Youtube(item.youtube))
            }
            if (track != null) {
                add(PillItem.Circuit(item.circuitId, item.circuitName))
            }

            val date = item.raceDate ?: return@apply
            if (date < LocalDate.now() && item.schedule.isNotEmpty()) {
                addAll(item.schedule.sortedBy { it.date.atTime(it.time) }
                    .groupBy { it.date }
                    .map { (_, items) -> items.map {
                        val icon = when (it.date.dayOfWeek.value) {
                            1 -> R.drawable.ic_cal_mon
                            2 -> R.drawable.ic_cal_tue
                            3 -> R.drawable.ic_cal_wed
                            4 -> R.drawable.ic_cal_thu
                            5 -> R.drawable.ic_cal_fri
                            6 -> R.drawable.ic_cal_sat
                            7 -> R.drawable.ic_cal_sun
                            else -> null
                        }
                        if (icon != null) {
                            PillItem.LabelIcon(_icon = icon, string = "${it.label} ${it.time}", highlight = false)
                        }
                        else {
                            PillItem.Label(string = "${it.label} ${it.time}", highlight = false)
                        }
                    }}
                    .flatten()
                )
            }
        }
    }

    override fun onClick(p0: View?) {
        pillItemClicked(PillItem.Circuit(model.circuitId, model.circuitName))
    }
}