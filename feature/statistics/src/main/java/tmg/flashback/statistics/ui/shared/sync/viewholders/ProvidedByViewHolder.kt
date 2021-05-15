package tmg.flashback.statistics.ui.shared.sync.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tmg.core.ui.navigation.NavigationProvider
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewSharedProvidedBinding
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.getString

class ProvidedByViewHolder(
    private val navigationManager: NavigationProvider,
    private val binding: ViewSharedProvidedBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(text: String?) {
        binding.providedBy.text = text?.fromHtml() ?: getString(R.string.shared_provided_by)
        if (text != null) {
            Glide.with(binding.icon)
                    .load(R.drawable.ergast)
                    .into(binding.icon)
        }
    }

    override fun onClick(p0: View?) {
        itemView.context.apply {
            startActivity(navigationManager.aboutAppIntent(this))
        }
    }
}