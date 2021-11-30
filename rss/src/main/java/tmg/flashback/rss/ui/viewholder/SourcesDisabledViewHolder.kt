package tmg.flashback.rss.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.rss.databinding.ViewRssSourcesDisabledBinding

internal class SourcesDisabledViewHolder(
    private val configure: () -> Unit,
    private val binding: ViewRssSourcesDisabledBinding
): RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {
    init {
        binding.container.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        configure()
    }
}