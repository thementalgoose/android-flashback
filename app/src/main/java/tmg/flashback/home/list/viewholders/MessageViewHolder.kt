package tmg.flashback.home.list.viewholders

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_home_message.view.*

class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(@StringRes msg: Int) {
        itemView.message.setText(msg)
    }
}