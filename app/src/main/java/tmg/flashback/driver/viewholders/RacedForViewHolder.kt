package tmg.flashback.driver.viewholders

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_driver_overview_raced_for.view.*
import tmg.flashback.R
import tmg.flashback.driver.overview.DriverOverviewItem
import tmg.flashback.driver.overview.RaceForPositionType
import tmg.flashback.driver.overview.RaceForPositionType.*
import tmg.flashback.driver.season.DriverSeasonItem
import tmg.flashback.repo.models.stats.SlimConstructor
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible

class RacedForViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: DriverSeasonItem.RacedFor) {
        this.bind(item.season, item.constructors, item.type, item.isChampionship)
    }

    fun bind(item: DriverOverviewItem.RacedFor) {
        this.bind(item.season, item.constructors, item.type, item.isChampionship)
    }

    private fun bind(
            season: Int?,
            constructors: SlimConstructor,
            type: RaceForPositionType,
            isChampionship: Boolean
    ) {
        itemView.year.isVisible = type != MID_SEASON_CHANGE && season != null
        itemView.year.text = season.toString()

        itemView.constructor.text = constructors.name
        itemView.constructorColor.setBackgroundColor(constructors.color)

        if (isChampionship) {
            itemView.pipeCircle.setImageResource(R.drawable.ic_star_filled_coloured)
        }
        else {
            itemView.pipeCircle.setImageResource(0)
        }

        when (type) {
            SINGLE -> {
                itemView.pipeTop.invisible()
                itemView.pipeCircle.visible()
                itemView.pipeBottom.invisible()
            }
            START -> {
                itemView.pipeTop.invisible()
                itemView.pipeCircle.visible()
                itemView.pipeBottom.visible()
            }
            SEASON -> {
                itemView.pipeTop.visible()
                itemView.pipeCircle.visible()
                itemView.pipeBottom.visible()
            }
            MID_SEASON_CHANGE -> {
                itemView.pipeTop.visible()
                itemView.pipeCircle.invisible()
                itemView.pipeBottom.visible()
            }
            END -> {
                itemView.pipeTop.visible()
                itemView.pipeCircle.visible()
                itemView.pipeBottom.invisible()
            }
        }
    }
}