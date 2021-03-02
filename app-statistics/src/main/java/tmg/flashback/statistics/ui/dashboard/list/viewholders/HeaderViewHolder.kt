package tmg.flashback.statistics.ui.dashboard.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_header.view.*
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.dashboard.list.HeaderType
import tmg.flashback.statistics.ui.dashboard.list.ListItem
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show
import tmg.utilities.extensions.views.visible

class HeaderViewHolder(
    private var featureToggled: (type: HeaderType) -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
    }

    private lateinit var type: HeaderType
    private var expanded: Boolean? = null

    fun bind(header: ListItem.Header) {

        type = header.type
        expanded = header.expanded

        itemView.header.setText(header.type.label)
        itemView.arrow.show(header.expanded != null)
        when (header.expanded) {
            true -> {
                itemView.header.contentDescription = getString(R.string.ab_season_list_header_toggle_expanded, getString(header.type.label))
                itemView.arrow.setImageResource(R.drawable.arrow_down)
                itemView.arrow.visible()
            }
            false -> {
                itemView.header.contentDescription = getString(R.string.ab_season_list_header_toggle_collapsed, getString(header.type.label))
                itemView.arrow.setImageResource(R.drawable.arrow_up)
                itemView.arrow.visible()
            }
            null -> {
                itemView.arrow.gone()
            }
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == itemView.container && expanded != null) {
            featureToggled.invoke(type)
        }
    }
}