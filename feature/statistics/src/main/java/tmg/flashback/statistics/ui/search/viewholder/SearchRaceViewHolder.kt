package tmg.flashback.statistics.ui.search.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.databinding.ViewSearchRaceBinding
import tmg.flashback.statistics.ui.search.SearchItem
import tmg.utilities.extensions.views.context

class SearchRaceViewHolder(
    private val binding: ViewSearchRaceBinding,
    private val itemClicked: (item: SearchItem) -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var item: SearchItem.Race

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: SearchItem.Race) {
        this.item = item

        binding.round.text = "#${item.round}"
        binding.title.text = "${item.season} ${item.raceName}"
        binding.circuit.text = item.circuitName
        binding.country.text = item.country
        binding.raceISO.setImageResource(context.getFlagResourceAlpha3(item.countryISO))
    }



    override fun onClick(p0: View?) {
        itemClicked(item)
    }
}