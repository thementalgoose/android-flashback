package tmg.flashback.rss.ui.settings.configure

import android.content.ClipData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.FragmentRssSettingsConfigureBinding
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.managerClipboard
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl

class RSSConfigureFragment: BaseFragment<FragmentRssSettingsConfigureBinding>() {

    private val viewModel: RSSConfigureViewModel by viewModel()

    private lateinit var adapter: RSSConfigureAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentRssSettingsConfigureBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Settings RSS Configure")

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
            it.article.contactLink?.let { contactLink ->
                if (!viewUrl(contactLink)) {
                    context?.let { context ->
                        Snackbar.make(binding.configuration, R.string.rss_configure_cannot_open_page, Snackbar.LENGTH_INDEFINITE)
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