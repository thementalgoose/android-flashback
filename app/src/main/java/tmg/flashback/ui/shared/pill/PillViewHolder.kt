package tmg.flashback.ui.shared.pill

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_link_pill.view.*

class PillViewHolder(
        val linkClicked: (PillItem) -> Unit,
        itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    lateinit var item: PillItem

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(item: PillItem) {
        this.item = item
        itemView.text.setText(item.label)
        itemView.image.setImageResource(item.icon)
    }

    override fun onClick(p0: View?) {
        linkClicked(item)
    }
}