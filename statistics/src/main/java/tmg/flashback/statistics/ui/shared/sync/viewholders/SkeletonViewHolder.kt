package tmg.flashback.statistics.ui.shared.sync.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewSharedSkeletonBinding

class SkeletonViewHolder(
    private val binding: ViewSharedSkeletonBinding
): RecyclerView.ViewHolder(binding.skeletonLayout) {
    init {
        binding.skeletonLayout.alpha = 0.1f
        binding.skeletonLayout.showSkeleton()
    }
}