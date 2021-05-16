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
import tmg.core.ui.base.BaseFragment
import tmg.flashback.rss.databinding.FragmentRssBinding
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.ui.settings.InitialScreen
import tmg.flashback.rss.ui.settings.RSSSettingsActivity
import tmg.utilities.extensions.observe

class RSSFragment: BaseFragment<FragmentRssBinding>() {

    private val viewModel: RSSViewModel by viewModel()

    private val repository: RSSRepository by inject()

    private lateinit var adapter: RSSAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentRssBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("RSS")

        adapter = RSSAdapter(
            openConfigure = {
                openConfigure(InitialScreen.CONFIGURE)
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

    private fun openConfigure(screen: InitialScreen) {
        activity?.let {
            startActivityForResult(
                RSSSettingsActivity.intent(it, screen),
                1001
            )
        }
    }

    companion object {
        private const val dataListAlpha = 0.5f
    }
}