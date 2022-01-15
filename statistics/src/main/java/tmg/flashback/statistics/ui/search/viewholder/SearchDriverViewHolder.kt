package tmg.flashback.statistics.ui.search.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.databinding.ViewSearchDriverBinding
import tmg.flashback.statistics.ui.search.SearchItem
import tmg.utilities.extensions.views.context

class SearchDriverViewHolder(
    private val binding: ViewSearchDriverBinding,
    private val itemClicked: (item: SearchItem) -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var item: SearchItem.Driver

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: SearchItem.Driver) {
        this.item = item

        Glide.with(binding.driverImage).clear(binding.driverImage)

        binding.driverName.text = item.name
        binding.driverISO.setImageResource(context.getFlagResourceAlpha3(item.nationalityISO))
        binding.driverNationality.text = item.nationality

        Glide.with(binding.driverImage)
            .load(item.imageUrl)
            .into(binding.driverImage)
    }

    override fun onClick(p0: View?) {
        itemClicked(item)
    }
}