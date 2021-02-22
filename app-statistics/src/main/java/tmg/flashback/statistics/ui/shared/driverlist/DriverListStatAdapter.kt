package tmg.flashback.statistics.ui.shared.driverlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_constructor_driver_label.view.*
import tmg.flashback.data.models.stats.ConstructorOverviewDriverStanding
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.ordinalAbbreviation

class DriverListStatAdapter: RecyclerView.Adapter<DriverListStatAdapter.ViewHolder>() {

    var list: List<ConstructorOverviewDriverStanding> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_constructor_driver_label, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: ConstructorOverviewDriverStanding) {
            itemView.tvName.text = item.driver.name
            itemView.imgFlag.setImageResource(itemView.context.getFlagResourceAlpha3(item.driver.nationalityISO))
            itemView.tvNumber.text = itemView.context.resources.getQuantityString(R.plurals.race_points, item.points, item.points)

            itemView.standing.text = item.championshipStanding.ordinalAbbreviation
        }
    }
}