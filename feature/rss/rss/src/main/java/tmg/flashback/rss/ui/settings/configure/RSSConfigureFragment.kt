package tmg.flashback.rss.ui.settings.configure

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.shared.ui.base.BaseFragment
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.rss.R
import tmg.flashback.rss.constants.ViewType
import tmg.flashback.rss.databinding.FragmentRssSettingsConfigureBinding
import tmg.flashback.rss.ui.configure.RSSConfigureViewModel
import tmg.utilities.extensions.managerClipboard
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class RSSConfigureFragment: BaseFragment<FragmentRssSettingsConfigureBinding>() {

    private val viewModel: RSSConfigureViewModel by viewModel()

    override val screenAnalytics = ScreenAnalytics(
        screenName = "Settings - RSS Configure"
    )

    private lateinit var adapter: RSSConfigureAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentRssSettingsConfigureBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsController.logEvent(ViewType.SETTINGS_RSS_CONFIGURE)

        adapter = RSSConfigureAdapter(
            quickAddItem = viewModel.inputs::addQuickItem,
            removeItem = viewModel.inputs::removeItem,
            customAddItem = viewModel.inputs::addCustomItem,
            visitWebsite = viewModel.inputs::visitWebsite
        )

        binding.configuration.adapter = adapter
        binding.configuration.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openWebsite) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.article.contactLink))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context?.let { context ->
                    Snackbar.make(binding.configuration, R.string.rss_configure_cannot_open_page, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.rss_configure_cannot_open_page_action) { _ ->
                            val clipData = ClipData.newPlainText(it.article.title, it.article.contactLink ?: it.article.source)
                            context.managerClipboard.setPrimaryClip(clipData)
                        }
                        .show()
                }
            }
        }
    }
}