package tmg.flashback.rss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.FragmentRssBinding
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.ui.feed.RSSViewModel
import tmg.flashback.rss.ui.settings.InitialScreen
import tmg.flashback.rss.ui.settings.RSSSettingsActivity
import tmg.flashback.rss.ui.web.WebFragment
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import tmg.utilities.lifecycle.viewInflateBinding

internal class RSSFragment: BaseFragment() {

    private val viewModel: RSSViewModel by viewModel()
    private val binding by viewInflateBinding(FragmentRssBinding::inflate)

    private val repository: RSSRepository by inject()

    private lateinit var adapter: RSSAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.inputs.refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("RSS")

        adapter = RSSAdapter(
            openConfigure = {
                openConfigure(InitialScreen.CONFIGURE)
            },
            articleClicked = { article, _ ->
                if (repository.newsOpenInExternalBrowser) {
                    viewUrl(article.link)
                } else {
                    val bundle = WebFragment.bundle(
                        title = article.title,
                        url = article.link
                    )
                    findNavController().navigate(R.id.action_RSSFragment_to_webFragment, bundle)
                }
            }
        )
        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = LinearLayoutManager(context)

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.inputs.refresh()
        }

        binding.refresh.setOnClickListener {
            viewModel.inputs.refresh()
        }

        binding.back.setOnClickListener {
            activity?.finish()
        }

        binding.settings.setOnClickListener {
            openConfigure(InitialScreen.SETTINGS)
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.isRefreshing) {
            if (it) {
                binding.progress.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
                binding.progress.visible()

                binding.dataList.alpha = dataListAlpha
                binding.dataList.locked = true
            } else {
                binding.progress.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
                binding.progress.gone()

                binding.dataList.alpha = 1.0f
                binding.dataList.locked = false

                binding.dataList.smoothScrollToPosition(0)
            }
        }
    }

    private fun openConfigure(screen: InitialScreen) {
        activity?.let {
            startActivity(RSSSettingsActivity.intent(it, screen))
        }
    }

    companion object {
        private const val dataListAlpha = 0.5f
    }
}