package tmg.flashback.shared.viewholders

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_shared_message.view.*
import tmg.utilities.extensions.fromHtml

class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(@StringRes msg: Int) {
        itemView.message.setText(msg)
    }

    fun bind(msg: String) {
        itemView.message.text = msg.fromHtml()
    }
}