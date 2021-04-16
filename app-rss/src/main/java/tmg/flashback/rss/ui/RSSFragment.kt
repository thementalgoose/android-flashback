package tmg.flashback.rss.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseFragment
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.rss.R
import tmg.flashback.rss.constants.ViewType
import tmg.flashback.rss.constants.logEvent
import tmg.flashback.rss.databinding.FragmentRssBinding
import tmg.flashback.rss.prefs.RSSRepository
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.ui.settings.InitialScreen
import tmg.flashback.rss.ui.settings.RSSSettingsActivity
import tmg.flashback.rss.web.WebFragment
import tmg.utilities.extensions.observe

class RSSFragment: BaseFragment<FragmentRssBinding>() {

    private val viewModel: RSSViewModel by viewModel()

    private val repository: RSSRepository by inject()

    override val screenAnalytics = ScreenAnalytics(
        screenName = "RSS Feed"
    )

    private lateinit var adapter: RSSAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentRssBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsController.logEvent(ViewType.RSS)

        adapter = RSSAdapter(
            openConfigure = {
                openConfigure()
            },
            articleClicked = { article, _ ->
                if (repository.newsOpenInExternalBrowser) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                    startActivity(intent)
                } else {
                    val action = RSSFragmentDirections.actionRSSFragmentToWebFragment(
                        title = article.title,
                        url = article.link
                    )
                    findNavController().navigate(action)
                }
            }
        )
        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = LinearLayoutManager(context)

        binding.refresh.setOnClickListener {
            viewModel.inputs.refresh()
        }

        binding.settings.setOnClickListener {
            openConfigure()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.isRefreshing) {
            if (it) {
                // Sub-optimal workaround for visibility issue in motion layout
                binding.progress.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
                binding.progress.alpha = 1.0f

                binding.dataList.alpha = dataListAlpha
                binding.dataList.locked = true
            } else {
                // Sub-optimal workaround for visibility issue in motion layout
                binding.progress.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
                binding.progress.alpha = 0.0f

                binding.dataList.alpha = 1.0f
                binding.dataList.locked = false

                binding.dataList.smoothScrollToPosition(0)
            }
        }
    }

    private fun openConfigure() {
        activity?.let {
            startActivityForResult(
                RSSSettingsActivity.intent(it, InitialScreen.CONFIGURE),
                1001
            )
        }
    }

    companion object {
        private const val dataListAlpha = 0.5f
    }
}