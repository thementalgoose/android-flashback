package tmg.flashback.dashboard.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_header.view.*
import tmg.flashback.R
import tmg.flashback.bottomSheetFastScrollDuration
import tmg.flashback.dashboard.list.HeaderType
import tmg.flashback.dashboard.list.ListItem
import tmg.flashback.extensions.dimensionPx
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class HeaderViewHolder(
    private var featureToggled: ((type: HeaderType) -> Unit)? = null,
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
            }
            false -> {
                itemView.header.contentDescription = getString(R.string.ab_season_list_header_toggle_collapsed, getString(header.type.label))
                itemView.arrow.setImageResource(R.drawable.arrow_up)
            }
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == itemView.container) {
            featureToggled?.invoke(type)
        }
    }
}