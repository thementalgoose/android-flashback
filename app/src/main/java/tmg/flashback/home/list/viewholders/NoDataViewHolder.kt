package tmg.flashback.home.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_home_no_data.view.*
import tmg.flashback.R

class NoDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(hasSeasonStarted: Boolean) {
        itemView.title.setText(if (hasSeasonStarted) R.string.season_not_found else R.string.season_not_started)
    }
}