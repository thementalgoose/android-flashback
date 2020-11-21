package tmg.flashback.shared.pill

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_link_pill.view.*
import tmg.utilities.extensions.views.context

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