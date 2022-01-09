package tmg.flashback.rss.ui.settings.configure

import android.content.ClipData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.rss.R
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseComposeFragment
import tmg.utilities.extensions.managerClipboard
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl

class RSSConfigureFragment: BaseComposeFragment() {

    private val viewModel: RSSConfigureViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val list = viewModel.list.observeAsState()
                    RSSConfigureLayout(
                        state = list.value ?: emptyList(),
                        removeItem = viewModel.inputs::removeItem,
                        addQuickLink = viewModel.inputs::addQuickItem,
                        addCustom = viewModel.inputs::addCustomItem,
                        websiteClicked = viewModel.inputs::visitWebsite
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Settings RSS Configure")

        observeEvent(viewModel.outputs.openWebsite) {
            it.article.contactLink?.let { contactLink ->
                if (!viewUrl(contactLink)) {
                    context?.let { context ->
                        Snackbar.make(view, R.string.rss_configure_cannot_open_page, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.rss_configure_cannot_open_page_action) { _ ->
                                val clipData = ClipData.newPlainText(it.article.title, it.article.contactLink ?: it.article.source)
                                context.managerClipboard?.setPrimaryClip(clipData)
                            }
                            .show()
                    }
                }
            }
        }
    }
}