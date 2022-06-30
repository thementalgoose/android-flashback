package tmg.flashback.ui2.dashboard.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.databinding.ViewSeasonListSwitchBinding
import tmg.flashback.ui2.dashboard.list.ListItem

class SwitchViewHolder(
    private val buttonIdClicked: (String) -> Unit,
    private val binding: ViewSeasonListSwitchBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var buttonId: String

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(item: ListItem.Switch) {
        this.buttonId = item.itemId
        binding.label.setText(item.label)
        binding.icon.setImageResource(item.icon)
        binding.switchWidget.isChecked = item.isChecked
    }

    override fun onClick(p0: View?) {
        buttonIdClicked(buttonId)
    }
}