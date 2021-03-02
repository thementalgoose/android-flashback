package tmg.flashback.statistics.ui.overview.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_driver_summary_history.view.*
import tmg.flashback.data.models.stats.SlimConstructor
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonItem
import tmg.flashback.statistics.ui.overview.driver.summary.DriverSummaryItem
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType.*
import tmg.flashback.statistics.ui.shared.constructorlist.ConstructorListAdapter
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible

class OverviewDriverHistoryViewHolder(
    private val callback: (season: Int) -> Unit,
    itemView: View
): DriverHistoryViewHolder(itemView), View.OnClickListener {

    init {
        container.isClickable = true
        container.isFocusable = true
        container.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        callback(season)
    }
}

open class DriverHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


    private val adapter: ConstructorListAdapter = ConstructorListAdapter()

    protected val container: ConstraintLayout = itemView.findViewById(R.id.container)
    protected val year: TextView = itemView.findViewById(R.id.year)
    private val pipeCircle: ImageView = itemView.findViewById(R.id.pipeCircle)
    private val pipeTop: View = itemView.findViewById(R.id.pipeTop)
    private val pipeBottom: View = itemView.findViewById(R.id.pipeBottom)

    protected var season: Int = -1

    fun bind(item: DriverSeasonItem.RacedFor) {
        season = item.season ?: -1
        this.bind(item.season, listOf(item.constructors), item.type, item.isChampionship)
    }

    fun bind(item: DriverSummaryItem.RacedFor) {
        season = item.season
        this.bind(item.season, item.constructors, item.type, item.isChampionship)
    }

    init {
        itemView.constructorList.layoutManager = LinearLayoutManager(context)
        itemView.constructorList.adapter = adapter
    }

    open fun bind(
        season: Int?,
        constructors: List<SlimConstructor>,
        type: PipeType,
        isChampionship: Boolean
    ) {
        year.isVisible = type != PipeType.SINGLE_PIPE && season != null
        year.text = season.toString()

        adapter.list = constructors

        if (isChampionship) {
            pipeCircle.setImageResource(R.drawable.ic_star_filled_coloured)
        }
        else {
            pipeCircle.setImageResource(0)
        }

        when (type) {
            SINGLE -> {
                pipeTop.invisible()
                pipeCircle.visible()
                pipeBottom.invisible()
            }
            START -> {
                pipeTop.invisible()
                pipeCircle.visible()
                pipeBottom.visible()
            }
            START_END -> {
                pipeTop.visible()
                pipeCircle.visible()
                pipeBottom.visible()
            }
            SINGLE_PIPE -> {
                pipeTop.visible()
                pipeCircle.invisible()
                pipeBottom.visible()
            }
            END -> {
                pipeTop.visible()
                pipeCircle.visible()
                pipeBottom.invisible()
            }
        }
    }
}