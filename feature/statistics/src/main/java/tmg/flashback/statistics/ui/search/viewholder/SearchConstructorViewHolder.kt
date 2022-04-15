package tmg.flashback.statistics.ui.search.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.databinding.ViewSearchConstructorBinding
import tmg.flashback.statistics.ui.search.SearchItem
import tmg.utilities.extensions.views.context

class SearchConstructorViewHolder(
    private val binding: ViewSearchConstructorBinding,
    private val itemClicked: (item: SearchItem) -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var item: SearchItem.Constructor

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: SearchItem.Constructor) {
        this.item = item

        binding.constructorName.text = item.name
        binding.constructorISO.setImageResource(context.getFlagResourceAlpha3(item.nationalityISO))
        binding.constructorNationality.text = item.nationality
        binding.constructorImage.setBackgroundColor(item.colour)
    }

    override fun onClick(p0: View?) {
        itemClicked(item)
    }
}