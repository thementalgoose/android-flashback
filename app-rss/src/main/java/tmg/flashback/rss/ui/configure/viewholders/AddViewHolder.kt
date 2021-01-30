package tmg.flashback.rss.ui.configure.viewholders

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rss_configure_add.view.*
import tmg.flashback.rss.R
import tmg.utilities.extensions.views.getString
import java.lang.NullPointerException
import java.lang.RuntimeException
import java.net.MalformedURLException
import java.net.URL

class AddViewHolder(
        private val addCustomItem: (String) -> Unit,
        itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    init {
        itemView.add.setOnClickListener(this)
        itemView.input.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    onClick(itemView.add)
                    true
                }
                else -> false
            }
        }
    }

    override fun onClick(p0: View?) {

        var text = itemView.input.text?.toString() ?: ""

        if (text.isEmpty()) {
            itemView.input.error = getString(R.string.rss_configure_valid_url)
        }
        if (!text.startsWith("http://") && !text.startsWith("https://")) {
            text = "https://$text"
        }
        try {
            val url = URL(text)
            addCustomItem(text)
            itemView.input.setText("")
            itemView.input.clearFocus()
        } catch (e: MalformedURLException) {
            itemView.input.error = getString(R.string.rss_configure_valid_url)
        } catch (e: RuntimeException) {
            itemView.input.error = getString(R.string.rss_configure_valid_url)
        } catch (e: NullPointerException) {
            itemView.input.error = getString(R.string.rss_configure_valid_url)
        }
    }
}