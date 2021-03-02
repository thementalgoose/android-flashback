package tmg.flashback.statistics.ui.dashboard.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_hero.view.*

class HeroViewHolder(
    itemView: View,
    private val settingsClicked: () -> Unit
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.settingsButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        settingsClicked()
    }
}