package tmg.flashback.statistics.ui.shared.driverlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutConstructorDriverBinding
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3

class DriverListAdapter: RecyclerView.Adapter<DriverListAdapter.ViewHolder>() {

    var list: List<Pair<Driver, Int>> = emptyList()
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
        fun bind(item: Pair<Driver, Int>) {
            val (driver, points) = item
            binding.tvName.text = driver.name
            binding.imgFlag.setImageResource(itemView.context.getFlagResourceAlpha3(driver.nationalityISO))
            binding.tvNumber.text = itemView.context.resources.getQuantityString(R.plurals.race_points, points, points)
        }
    }
}