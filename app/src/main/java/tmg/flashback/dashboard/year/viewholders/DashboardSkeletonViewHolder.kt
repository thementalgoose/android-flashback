package tmg.flashback.dashboard.year.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_skeleton.view.*

class DashboardSkeletonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    init {
        itemView.skeletonLayout.showSkeleton()
    }
}