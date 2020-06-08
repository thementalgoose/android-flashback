package tmg.flashback.home.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_home_loading.view.*

class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    init {
        itemView.skeleton.showSkeleton()
    }
}