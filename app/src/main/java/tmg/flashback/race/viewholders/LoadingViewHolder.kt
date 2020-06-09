package tmg.flashback.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_race_loading.view.*

class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    init {
        itemView.skeleton.showSkeleton()
    }
}