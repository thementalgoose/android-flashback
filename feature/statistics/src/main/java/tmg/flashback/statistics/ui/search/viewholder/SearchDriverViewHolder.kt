package tmg.flashback.statistics.ui.search.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.databinding.ViewSearchDriverBinding
import tmg.flashback.statistics.databinding.ViewSearchRaceBinding
import tmg.flashback.statistics.ui.search.SearchItem
import tmg.utilities.extensions.views.context

class SearchDriverViewHolder(
    private val binding: ViewSearchDriverBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchItem.Driver) {

        println("Binding item ${item.driverId}")

        Glide.with(binding.driverImage).clear(binding.driverImage)

        binding.driverName.text = item.name
        binding.driverISO.setImageResource(context.getFlagResourceAlpha3(item.driverId))
        binding.driverNationality.text = item.nationality

        Glide.with(binding.driverImage)
            .load(item.imageUrl)
            .into(binding.driverImage)
    }
}