package tmg.flashback.ui.dashboard.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_button.view.*
import tmg.flashback.ui.dashboard.list.ListItem

class ButtonViewHolder(
        private val buttonIdClicked: (String) -> Unit,
        itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var buttonId: String

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(item: ListItem.Button) {
        this.buttonId = item.itemId
        itemView.label.setText(item.label)
        itemView.icon.setImageResource(item.icon)
    }

    override fun onClick(p0: View?) {
        buttonIdClicked(buttonId)
    }
}