package tmg.flashback.shared.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.SkeletonLayout
import tmg.flashback.R

class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    init {
        itemView.findViewById<SkeletonLayout>(R.id.skeleton).showSkeleton()
    }
}