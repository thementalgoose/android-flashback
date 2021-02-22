package tmg.flashback.statistics.ui.overview.constructor.summary.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_constructor_summary_history.view.*
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryItem
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.statistics.ui.shared.driverlist.DriverListStatAdapter
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.show
import tmg.utilities.extensions.views.visible

class ConstructorHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var driverAdapter = DriverListStatAdapter()

    init {
        itemView.drivers.adapter = driverAdapter
        itemView.drivers.layoutManager = LinearLayoutManager(itemView.context)
    }

    fun bind(item: ConstructorSummaryItem.History) {
        itemView.apply {

            year.text = item.season.toString()
            points.text = item.points.toString()
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