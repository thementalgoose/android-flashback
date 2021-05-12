package tmg.flashback.statistics.ui.shared.sync.viewholders

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewSharedConstructorChampionshipNotAwardedBinding
import tmg.flashback.statistics.databinding.ViewSharedMessageBinding
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.getString

class MessageViewHolder(
    private val binding: ViewSharedMessageBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(@StringRes msg: Int, list: List<Any>) {
        if (list.isNotEmpty()) {
            binding.message.text = getString(msg, *list.toTypedArray()).fromHtml()
        }
        else {
            binding.message.text = getString(msg).fromHtml()
        }
    }

    fun bind(msg: String) {
        binding.message.text = msg.fromHtml()
    }
}