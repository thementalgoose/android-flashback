package tmg.flashback.dashboard.year

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R

class DashboardYearAdapter: RecyclerView.Adapter<DashboardYearViewHolder>() {

    var list: List<DashboardYearModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardYearViewHolder {
        return DashboardYearViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_dashboard_year, parent, false))
    }

    override fun onBindViewHolder(holder: DashboardYearViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}