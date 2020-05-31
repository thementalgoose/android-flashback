package tmg.flashback.standings.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_standings_skeleton.view.*

class StandingsSkeletonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    init {
        itemView.skeletonLayout.showSkeleton()
    }
}