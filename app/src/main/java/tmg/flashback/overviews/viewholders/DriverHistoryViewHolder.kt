package tmg.flashback.overviews.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.overviews.driver.summary.DriverSummaryItem
import tmg.flashback.overviews.driver.summary.PipeType
import tmg.flashback.overviews.driver.summary.PipeType.*
import tmg.flashback.overviews.driver.season.DriverSeasonItem
import tmg.flashback.repo.models.stats.SlimConstructor
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

    protected val container: ConstraintLayout = itemView.findViewById(R.id.container)
    protected val year: TextView = itemView.findViewById(R.id.year)
    protected val constructor: TextView = itemView.findViewById(R.id.constructor)
    private val constructorColor: View = itemView.findViewById(R.id.constructorColor)
    private val pipeCircle: ImageView = itemView.findViewById(R.id.pipeCircle)
    private val pipeTop: View = itemView.findViewById(R.id.pipeTop)
    private val pipeBottom: View = itemView.findViewById(R.id.pipeBottom)

    protected var season: Int = -1

    fun bind(item: DriverSeasonItem.RacedFor) {
        season = item.season ?: -1
        this.bind(item.season, item.constructors, item.type, item.isChampionship)
    }

    fun bind(item: DriverSummaryItem.RacedFor) {
        season = item.season
        this.bind(item.season, item.constructors, item.type, item.isChampionship)
    }

    open fun bind(
            season: Int?,
            constructors: SlimConstructor,
            type: PipeType,
            isChampionship: Boolean
    ) {
        year.isVisible = type != SINGLE_PIPE && season != null
        year.text = season.toString()

        constructor.text = constructors.name
        constructorColor.setBackgroundColor(constructors.color)

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