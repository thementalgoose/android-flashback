package tmg.flashback.statistics.ui.overview.constructor.summary.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.firebase.extensions.pointsDisplay
import tmg.flashback.statistics.databinding.ViewConstructorSummaryHistoryBinding
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryItem
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.statistics.ui.shared.driverlist.DriverListStatAdapter
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.show
import tmg.utilities.extensions.views.visible

class ConstructorHistoryViewHolder(
    private val binding: ViewConstructorSummaryHistoryBinding
): RecyclerView.ViewHolder(binding.root) {

    var driverAdapter = DriverListStatAdapter()

    init {
        binding.drivers.adapter = driverAdapter
        binding.drivers.layoutManager = LinearLayoutManager(itemView.context)
    }

    fun bind(item: ConstructorSummaryItem.History) {
        binding.apply {

            year.text = item.season.toString()
            points.text = item.points.pointsDisplay()
            standing.text = item.championshipPosition.ordinalAbbreviation

            inProgress.show(item.isInProgress)

            championship.show(item.championshipPosition == 1 && !item.isInProgress)

            driverAdapter.list = item.drivers.sortedByDescending { it.points }

            when (item.pipe) {
                PipeType.SINGLE -> {
                    pipeTop.invisible()
                    pipeCircle.visible()
                    pipeBottom.invisible()
                }
                PipeType.START -> {
                    pipeTop.invisible()
                    pipeCircle.visible()
                    pipeBottom.visible()
                }
                PipeType.START_END -> {
                    pipeTop.visible()
                    pipeCircle.visible()
                    pipeBottom.visible()
                }
                PipeType.SINGLE_PIPE -> {
                    pipeTop.visible()
                    pipeCircle.invisible()
                    pipeBottom.visible()
                }
                PipeType.END -> {
                    pipeTop.visible()
                    pipeCircle.visible()
                    pipeBottom.invisible()
                }
            }
        }
    }
}