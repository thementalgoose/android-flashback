package tmg.flashback.web.ui.browser

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.web.R
import tmg.flashback.web.databinding.FragmentWebBinding
import tmg.flashback.web.client.FlashbackWebChromeClient
import tmg.flashback.web.client.FlashbackWebViewClient
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.show
import java.net.MalformedURLException

@SuppressLint("SetJavaScriptEnabled")
internal class WebFragment : Fragment() {

    // Binding stripped out and nullable due to loading of webpages and fragment lifecycle causing
    // binding to throw NPE
    private var binding: FragmentWebBinding? = null

    private val analyticsManager: AnalyticsManager by inject()
    private val crashController: CrashController by inject()

    private val webBrowserRepository: WebBrowserRepository by inject()

    private lateinit var pageTitle: String
    private lateinit var pageUrl: String

    internal var callback: WebUpdated? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            progressBar.progressColour = context?.theme?.getColor(R.attr.colorPrimary) ?: Color.BLUE
            progressBar.timeLimit = 100
        }

        val webViewClient = FlashbackWebViewClient(
            domainChanged = { callback?.domainChanged(it) },
            titleChanged = { callback?.titleChanged(it) }
        )
        val webChromeClient = FlashbackWebChromeClient(
            updateProgressToo = {
                val result = it.toFloat() / 100f
                binding?.progressBar?.animateProgress(result, false) { "" }
                binding?.progressBar?.show(result != 1.0f)
            }
        )

        binding?.apply {
            webview.webChromeClient = webChromeClient
            webview.webViewClient = webViewClient
            webview.settings.loadsImagesAutomatically = true
            webview.settings.javaScriptEnabled = webBrowserRepository.enableJavascript
            webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

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
            } catch (e: MalformedURLException) {
                /* Do nothing */
            } catch (e: RuntimeException) {
                /* Do nothing */
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding?.let {
            outState.putString(keyTitle, it.webview.title.toString())
            outState.putString(keyUrl, it.webview.url)
        }
        super.onSaveInstanceState(outState)
    }

    fun load(pageTitle: String, url: String) {
        this.pageTitle = pageTitle
        this.pageUrl = url

        binding!!.apply {
            webview.loadUrl(pageUrl)
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.apply {
            webview.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            webview.onResume()
        }
    }

    companion object {

        private const val keyTitle: String = "title"
        private const val keyUrl: String = "url"

        fun bundle(title: String, url: String) = Bundle().apply {
            putString(keyTitle, title)
            putString(keyUrl, url)
        }
    }
}

internal interface WebUpdated {
    fun domainChanged(domain: String)
    fun titleChanged(title: String)
}