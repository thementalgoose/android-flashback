package tmg.flashback.rss.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rss_sources_disabled.view.*

class RSSSourcesDisabledViewHolder(
    private val configure: () -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    init {
        itemView.container.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        configure()
    }
}