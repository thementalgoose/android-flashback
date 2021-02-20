package tmg.flashback.rss.ui.settings.configure

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rss_settings_configure.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseFragment
import tmg.flashback.rss.R
import tmg.flashback.rss.ui.configure.RSSConfigureViewModel
import tmg.utilities.extensions.managerClipboard
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class RSSConfigureFragment: BaseFragment() {

    private val viewModel: RSSConfigureViewModel by viewModel()

    override fun layoutId() = R.layout.fragment_rss_settings_configure

    private lateinit var adapter: RSSConfigureAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RSSConfigureAdapter(
            quickAddItem = viewModel.inputs::addQuickItem,
            removeItem = viewModel.inputs::removeItem,
            customAddItem = viewModel.inputs::addCustomItem,
            visitWebsite = viewModel.inputs::visitWebsite
        )

        configuration.adapter = adapter
        configuration.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openWebsite) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.article.contactLink))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context?.let { context ->
                    Snackbar.make(configuration, R.string.rss_configure_cannot_open_page, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.rss_configure_cannot_open_page_action) { view ->
                            val clipData = ClipData.newPlainText(it.article.title, it.article.contactLink ?: it.article.source)
                            context.managerClipboard.setPrimaryClip(clipData)
                        }
                        .show()
                }
            }
        }
    }
}