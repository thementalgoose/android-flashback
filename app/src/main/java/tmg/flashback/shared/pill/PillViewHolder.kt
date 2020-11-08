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
        itemView.button.setOnClickListener(this)
    }

    fun bind(item: PillItem) {
        this.item = item
        itemView.button.setText(item.label)
        item.icon.toDrawable()?.let {
            itemView.button.setCompoundDrawablesRelative(it, null, null, null)
        }
    }

    private fun Int.toDrawable(): Drawable? {
        return ContextCompat.getDrawable(context, this)
    }

    override fun onClick(p0: View?) {
        linkClicked(item)
    }
}