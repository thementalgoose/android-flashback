package tmg.flashback.home.season.viewholders

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_header.view.*
import tmg.flashback.R
import tmg.flashback.home.season.HeaderType
import tmg.flashback.home.season.SeasonListItem
import tmg.utilities.extensions.views.show

class HeaderViewHolder(
    var featureToggled: ((type: HeaderType) -> Unit)? = null,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
    }

    lateinit var type: HeaderType
    var expanded: Boolean? = null

    fun bind(header: SeasonListItem.Header) {

        type = header.type
        expanded = header.expanded

        itemView.header.setText(header.type.label)
        itemView.arrow.show(header.expanded != null)
        when (header.expanded) {
            true -> {
                itemView.arrow.setImageResource(R.drawable.arrow_down)
            }
            false -> {
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