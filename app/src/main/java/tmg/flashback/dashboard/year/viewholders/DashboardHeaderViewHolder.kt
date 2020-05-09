package tmg.flashback.dashboard.year.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_header.view.*


class DashboardHeaderViewHolder(
    itemView: View,
    val settingsClicked: () -> Unit
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.ibtnSettings.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        settingsClicked()
    }
}