package tmg.flashback.statistics.ui.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.skeleton_race.view.*

class SkeletonLoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    init {
        itemView.skeletonLayout.showSkeleton()
    }
}