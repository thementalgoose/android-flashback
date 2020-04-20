package tmg.flashback.season.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_header.view.*
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.invisible

class RacePlaceholderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    init {
        itemView.main.invisible()
    }
}