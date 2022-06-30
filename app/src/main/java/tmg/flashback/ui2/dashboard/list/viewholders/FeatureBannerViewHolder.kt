package tmg.flashback.ui2.dashboard.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.databinding.ViewSeasonListFeatureBannerBinding
import tmg.flashback.ui2.dashboard.list.ListItem
import tmg.utilities.extensions.views.context

class FeatureBannerViewHolder(
    private val binding: ViewSeasonListFeatureBannerBinding,
    private val itemClicked: (item: ListItem.FeatureBanner) -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var featureBanner: ListItem.FeatureBanner

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: ListItem.FeatureBanner) {
        this.featureBanner = item
        binding.text.text = item.text.resolve(context)
    }

    override fun onClick(p0: View?) {
        itemClicked(featureBanner)
    }
}