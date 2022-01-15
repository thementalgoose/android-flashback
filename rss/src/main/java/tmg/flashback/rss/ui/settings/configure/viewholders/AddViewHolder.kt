package tmg.flashback.rss.ui.settings.configure.viewholders

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.ViewRssConfigureAddBinding
import tmg.utilities.extensions.views.getString
import java.net.MalformedURLException
import java.net.URL

class AddViewHolder(
        private val addCustomItem: (String) -> Unit,
        private val binding: ViewRssConfigureAddBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    init {
        binding.add.setOnClickListener(this)
        binding.input.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    onClick(binding.add)
                    true
                }
                else -> false
            }
        }
    }

    override fun onClick(p0: View?) {

        var text = binding.input.text?.toString() ?: ""

        if (text.isEmpty()) {
            binding.input.error = getString(R.string.rss_configure_valid_url)
        }
        if (!text.startsWith("http://") && !text.startsWith("https://")) {
            text = "https://$text"
        }
        try {
            @Suppress("UNUSED_VARIABLE")
            val url = URL(text)
            addCustomItem(text)
            binding.input.setText("")
            binding.input.clearFocus()
        } catch (e: MalformedURLException) {
            binding.input.error = getString(R.string.rss_configure_valid_url)
        } catch (e: RuntimeException) {
            binding.input.error = getString(R.string.rss_configure_valid_url)
        } catch (e: NullPointerException) {
            binding.input.error = getString(R.string.rss_configure_valid_url)
        }
    }
}