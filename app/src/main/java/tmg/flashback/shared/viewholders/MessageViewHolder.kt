package tmg.flashback.shared.viewholders

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_shared_message.view.*
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.getString

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(@StringRes msg: Int, list: List<Any>) {
        if (list.isNotEmpty()) {
            itemView.message.text = getString(msg, *list.toTypedArray()).fromHtml()
        } else {
            itemView.message.text = getString(msg).fromHtml()
        }
    }

    fun bind(msg: String) {
        itemView.message.text = msg.fromHtml()
    }
}