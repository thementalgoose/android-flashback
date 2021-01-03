package tmg.flashback.ui.shared.driverlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_constructor_driver.view.*
import tmg.flashback.R
import tmg.flashback.repo.models.stats.Driver
import tmg.flashback.ui.utils.getFlagResourceAlpha3

class DriverListAdapter: RecyclerView.Adapter<DriverListAdapter.ViewHolder>() {

    var list: List<Pair<Driver, Int>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_constructor_driver, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: Pair<Driver, Int>) {
            val (driver, points) = item
            itemView.tvName.text = driver.name
            itemView.imgFlag.setImageResource(itemView.context.getFlagResourceAlpha3(driver.nationalityISO))
            itemView.tvNumber.text = itemView.context.resources.getQuantityString(R.plurals.race_points, points, points)
        }
    }
}