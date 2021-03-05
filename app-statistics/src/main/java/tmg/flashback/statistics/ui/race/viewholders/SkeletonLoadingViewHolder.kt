package tmg.flashback.statistics.ui.race.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.SkeletonRaceBinding

class SkeletonLoadingViewHolder(
    private val binding: SkeletonRaceBinding
): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.skeletonLayout.showSkeleton()
    }
}