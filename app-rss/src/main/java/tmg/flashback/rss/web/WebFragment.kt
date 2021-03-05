package tmg.flashback.rss.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import org.koin.android.ext.android.inject
import tmg.flashback.core.controllers.AnalyticsController
import tmg.flashback.core.ui.BaseFragment
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.FragmentWebBinding
import tmg.flashback.rss.prefs.RSSRepository
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.show
import java.lang.RuntimeException
import java.net.MalformedURLException

@SuppressLint("SetJavaScriptEnabled")
class WebFragment : BaseFragment<FragmentWebBinding>() {

    private val repository: RSSRepository by inject()
    private val analyticsManager: AnalyticsController by inject()

    private var backCallback: FragmentRequestBack? = null

    private lateinit var pageTitle: String
    private lateinit var pageUrl: String

    override fun inflateView(inflater: LayoutInflater) = FragmentWebBinding.inflate(inflater)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentRequestBack) {
            backCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.pageTitle = it.getString(keyTitle)!!
            this.pageUrl = it.getString(keyUrl)!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.progressColour = context?.theme?.getColor(R.attr.colorPrimary) ?: Color.BLUE
        binding.progressBar.timeLimit = 100

        val webViewClient = FlashbackWebViewClient(
            domainChanged = { binding.domain.text = it },
            titleChanged = { binding.title.text = it }
        )
        val webChromeClient = FlashbackWebChromeClient(
            updateProgressToo = {
                val result = it.toFloat() / 100f
                binding.progressBar.animateProgress(result, false) { "" }
                binding.progressBar.show(result != 1.0f)
            }
        )

        binding.webview.webChromeClient = webChromeClient
        binding.webview.webViewClient = webViewClient
        binding.webview.settings.loadsImagesAutomatically = true
        binding.webview.settings.javaScriptEnabled = repository.inAppEnableJavascript
        binding.webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        load(pageTitle, pageUrl)

        binding.back.setOnClickListener {
            exitWeb()
            backCallback?.fragmentBackPressed()
        }

        binding.openInBrowser.setOnClickListener {
            openInBrowser()
        }

        binding.share.setOnClickListener {
            share()
        }

        try {
            val host = Uri.parse(pageUrl).host
            host?.let {
                analyticsManager.viewScreen(
                    screenName = "Webpage",
                    clazz = WebFragment::class.java,
                    params = mapOf(
                        "host" to host
                    )
                )
            }
        }
        catch (e: MalformedURLException) {/* Do nothing */ }
        catch (e: RuntimeException) {/* Do nothing */ }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(keyTitle, binding.title.text.toString())
        outState.putString(keyUrl, binding.webview.url)
        super.onSaveInstanceState(outState)
    }

    fun load(pageTitle: String, url: String) {
        this.pageTitle = pageTitle
        this.pageUrl = url
        binding.webview.loadUrl(pageUrl)

        binding.title.text = pageTitle
        binding.domain.text = Uri.parse(pageUrl).host ?: "-"
    }

    private fun openInBrowser() {
        val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.webview.url))
        startActivity(Intent.createChooser(intent, getString(R.string.choose_browser)))
    }

    private fun share() {
        val intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, binding.webview.url)
        startActivity(Intent.createChooser(intent, getString(R.string.choose_share)))
    }

    override fun onPause() {
        super.onPause()
        binding.webview.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webview.onResume()
    }

    fun exitWeb() {
        binding.webview.onPause()
        binding.webview.stopLoading()
    }

    companion object {

        private const val keyTitle: String = "title"
        private const val keyUrl: String = "url"

        fun instance(title: String, url: String): WebFragment {
            val bundle = Bundle().apply {
                putString(keyTitle, title)
                putString(keyUrl, url)
            }
            return WebFragment().apply {
                arguments = bundle
            }
        }
    }
}