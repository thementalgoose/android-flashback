package tmg.flashback.statistics.ui.shared.driverlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.data.models.stats.ConstructorOverviewDriverStanding
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutConstructorDriverLabelBinding
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.ordinalAbbreviation

class DriverListStatAdapter: RecyclerView.Adapter<DriverListStatAdapter.ViewHolder>() {

    var list: List<ConstructorOverviewDriverStanding> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            LayoutConstructorDriverLabelBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(
        private val binding: LayoutConstructorDriverLabelBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ConstructorOverviewDriverStanding) {
            binding.tvName.text = item.driver.name
            binding.imgFlag.setImageResource(itemView.context.getFlagResourceAlpha3(item.driver.nationalityISO))
            binding.tvNumber.text = itemView.context.resources.getQuantityString(R.plurals.race_points, item.points, item.points)

            binding.standing.text = item.championshipStanding.ordinalAbbreviation
        }
    }
}