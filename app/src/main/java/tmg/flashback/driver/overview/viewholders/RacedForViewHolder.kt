package tmg.flashback.driver.overview.viewholders

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_driver_overview_raced_for.view.*
import tmg.flashback.R
import tmg.flashback.driver.overview.DriverOverviewItem
import tmg.flashback.driver.overview.RaceForPositionType.*
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible

class RacedForViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: DriverOverviewItem.RacedFor) {

        itemView.year.isVisible = item.type != MID_SEASON_CHANGE
        itemView.year.text = item.season.toString()

        itemView.constructor.text = item.constructors.name
        itemView.constructorColor.setBackgroundColor(item.constructors.color)

        if (item.isChampionship) {
            itemView.pipeCircle.setImageResource(R.drawable.ic_star_filled_coloured)
        }
        else {
            itemView.pipeCircle.setImageResource(0)
        }

        when (item.type) {
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