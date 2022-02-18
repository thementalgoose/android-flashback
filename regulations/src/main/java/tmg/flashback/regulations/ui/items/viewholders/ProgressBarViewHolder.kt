package tmg.flashback.regulations.ui.items.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.regulations.databinding.ViewFormatProgressBarBinding
import tmg.flashback.regulations.domain.Item
import tmg.utilities.extensions.views.context
import kotlin.math.roundToInt

internal class ProgressBarViewHolder(
    private val binding: ViewFormatProgressBarBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item.ProgressBar) {
        val progress = (item.progress.toFloat() - item.initial.toFloat()) / (item.final.toFloat() - item.initial.toFloat())
        binding.progressBar.animateProgress(progress, fromBeginning = true, resolver = {
            (it * item.final).roundToInt().toString()
        })
        binding.label.text = item.label.resolve(context)
    }
}