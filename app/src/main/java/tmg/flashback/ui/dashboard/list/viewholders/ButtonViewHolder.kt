package tmg.flashback.ui.dashboard.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.databinding.ViewSeasonListButtonBinding
import tmg.flashback.ui.dashboard.list.ListItem

class ButtonViewHolder(
        private val buttonIdClicked: (String) -> Unit,
        private val binding: ViewSeasonListButtonBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var buttonId: String

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: ListItem.Button) {
        this.buttonId = item.itemId
        binding.label.setText(item.label)
        binding.icon.setImageResource(item.icon)
    }

    override fun onClick(p0: View?) {
        buttonIdClicked(buttonId)
    }
}