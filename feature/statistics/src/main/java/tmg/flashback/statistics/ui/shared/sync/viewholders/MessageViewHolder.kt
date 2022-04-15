package tmg.flashback.statistics.ui.shared.sync.viewholders

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.databinding.ViewSharedMessageBinding
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.getString
import java.net.MalformedURLException

class MessageViewHolder(
    private val binding: ViewSharedMessageBinding
): RecyclerView.ViewHolder(binding.root), KoinComponent {

    private val crashController: CrashController by inject()

    private var messageUrl: String? = null

    fun bind(@StringRes msg: Int, list: List<Any>, clickableUrl: String? = null) {
        if (BuildConfig.DEBUG) {
            Log.d("Statistics", "Message view holder ${getString(msg, *list.toTypedArray())}")
        }
        if (list.isNotEmpty()) {
            binding.message.text = getString(msg, *list.toTypedArray()).fromHtml()
        }
        else {
            binding.message.text = getString(msg).fromHtml()
        }
        setupClickable(clickableUrl)
    }

    fun bind(msg: String, clickableUrl: String? = null) {
        if (BuildConfig.DEBUG) {
            Log.d("Statistics", "Message view holder $msg")
        }
        binding.message.text = msg.fromHtml()
        setupClickable(clickableUrl)
    }

    private fun setupClickable(url: String?) {
        this.messageUrl = url
        binding.message.isClickable = url != null
        binding.message.isFocusable = url != null
        if (messageUrl == null) {
            binding.message.setOnClickListener(null)
        } else {
            binding.message.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(messageUrl))
                    it.context.startActivity(intent)
                } catch (e: MalformedURLException) {
                    crashController.logException(e)
                }
            }
        }
    }
}