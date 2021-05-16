package tmg.flashback.ui.dashboard.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.databinding.ViewSeasonListHeaderBinding
import tmg.flashback.ui.dashboard.list.HeaderType
import tmg.flashback.ui.dashboard.list.ListItem
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show
import tmg.utilities.extensions.views.visible

class HeaderViewHolder(
    private var featureToggled: (type: HeaderType) -> Unit,
    private val binding: ViewSeasonListHeaderBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.container.setOnClickListener(this)
    }

    private lateinit var type: HeaderType
    private var expanded: Boolean? = null

    fun bind(header: ListItem.Header) {

        type = header.type
        expanded = header.expanded

        binding.header.setText(header.type.label)
        binding.arrow.show(header.expanded != null)
        when (header.expanded) {
            true -> {
                binding.header.contentDescription = getString(R.string.ab_season_list_header_toggle_expanded, getString(header.type.label))
                binding.arrow.setImageResource(R.drawable.arrow_down)
                binding.arrow.visible()
            }
            false -> {
                binding.header.contentDescription = getString(R.string.ab_season_list_header_toggle_collapsed, getString(header.type.label))
                binding.arrow.setImageResource(R.drawable.arrow_up)
                binding.arrow.visible()
            }
            null -> {
                binding.arrow.gone()
            }
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == binding.container && expanded != null) {
            featureToggled.invoke(type)
        }
    }
}