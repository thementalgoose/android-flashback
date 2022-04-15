package tmg.flashback.statistics.ui.shared.driverlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutConstructorDriverBinding

class DriverListAdapter: RecyclerView.Adapter<DriverListAdapter.ViewHolder>() {

    var list: List<Pair<Driver, Double>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            LayoutConstructorDriverBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(
        private val binding: LayoutConstructorDriverBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<Driver, Double>) {
            val (driver, points) = item
            binding.tvName.text = driver.name
            binding.imgFlag.setImageResource(itemView.context.getFlagResourceAlpha3(driver.nationalityISO))
            binding.tvNumber.text = itemView.context.resources.getQuantityString(R.plurals.race_points, points.toInt(), points.pointsDisplay())
        }
    }
}