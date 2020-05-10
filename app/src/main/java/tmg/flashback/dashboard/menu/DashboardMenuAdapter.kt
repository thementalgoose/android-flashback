package tmg.flashback.dashboard.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.dashboard.year.DashboardMenuItem
import tmg.flashback.utils.Selected

class DashboardMenuAdapter(
    val menuItemClicked: (menuItem: DashboardMenuItem) -> Unit
): RecyclerView.Adapter<DashboardMenuViewHolder>() {

    var list: List<Selected<DashboardMenuItem>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardMenuViewHolder {
        return DashboardMenuViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_dashboard_menu_item, parent, false),
            menuItemClicked
        )
    }

    override fun onBindViewHolder(holder: DashboardMenuViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}