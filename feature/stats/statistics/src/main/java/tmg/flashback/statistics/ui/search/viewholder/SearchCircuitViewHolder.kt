package tmg.flashback.statistics.ui.search.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.databinding.ViewSearchCircuitBinding
import tmg.flashback.statistics.ui.search.SearchItem
import tmg.utilities.extensions.views.context

class SearchCircuitViewHolder(
    private val binding: ViewSearchCircuitBinding,
    private val itemClicked: (item: SearchItem) -> Unit
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var item: SearchItem.Circuit

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: SearchItem.Circuit) {
        this.item = item

        val trackLayout = TrackLayout.getTrack(item.circuitId)

        binding.circuitName.text = item.name
        binding.circuitLocation.text = item.location
        binding.circuitCountry.text = item.nationality
        binding.circuitISO.setImageResource(context.getFlagResourceAlpha3(item.nationalityISO))

        if (trackLayout != null) {
            binding.circuitImage.setImageResource(trackLayout.icon)
        } else {
            binding.circuitImage.setImageResource(0)
        }
    }

    override fun onClick(p0: View?) {
        itemClicked(item)
    }
}