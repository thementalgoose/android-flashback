package tmg.flashback.dashboard.year.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_banner.view.*

class DashboardBannerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(msg: String) {
        itemView.message.text = msg
    }
}