package tmg.flashback.dashboard.menu

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_dashboard_menu_item.view.*
import tmg.flashback.R
import tmg.flashback.dashboard.year.DashboardMenuItem
import tmg.flashback.utils.Selected

class DashboardMenuViewHolder(
        itemView: View,
        val menuItemClicked: (menuItem: DashboardMenuItem) -> Unit
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var currentItem: DashboardMenuItem

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(selected: Selected<DashboardMenuItem>) {
        this.currentItem = selected.value
        itemView.apply {
            menuItemIcon.setImageResource(selected.value.icon)
            menuItemLabel.setText(selected.value.msg)

            if (selected.isSelected) {
                container.setBackgroundResource(R.drawable.background_selected)
            }
            else {
                container.setBackgroundResource(0)
            }
        }
    }

    override fun onClick(p0: View?) {
        menuItemClicked(currentItem)
    }
}