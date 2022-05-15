package tmg.flashback.rss.web

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.FragmentWebBinding
import tmg.flashback.rss.repo.RSSRepository
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.show
import java.net.MalformedURLException

@SuppressLint("SetJavaScriptEnabled")
internal class WebFragment : Fragment() {

    // Binding stripped out and nullable due to loading of webpages and fragment lifecycle causing
    // binding to throw NPE
    private var binding: FragmentWebBinding? = null

    private val repository: RSSRepository by inject()
    private val analyticsManager: AnalyticsManager by inject()
    private val crashController: CrashController by inject()

    private lateinit var pageTitle: String
    private lateinit var pageUrl: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding!!.root
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

        binding?.apply {
            progressBar.progressColour = context?.theme?.getColor(R.attr.colorPrimary) ?: Color.BLUE
            progressBar.timeLimit = 100
        }

        val webViewClient = FlashbackWebViewClient(
            domainChanged = { binding?.domain?.text = it },
            titleChanged = { binding?.title?.text = it }
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
            webview.settings.javaScriptEnabled = repository.inAppEnableJavascript
            webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

            load(pageTitle, pageUrl)

            back.setOnClickListener {
                exitWeb()
                findNavController().navigateUp()
            }

            openInBrowser.setOnClickListener {
                openInBrowser()
            }

            share.setOnClickListener {
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
            outState.putString(keyTitle, it.title.text.toString())
            outState.putString(keyUrl, it.webview.url)
        }
        super.onSaveInstanceState(outState)
    }

    fun load(pageTitle: String, url: String) {
        this.pageTitle = pageTitle
        this.pageUrl = url

        binding!!.apply {
            webview.loadUrl(pageUrl)
            title.text = pageTitle
            domain.text = Uri.parse(pageUrl).host ?: "-"
        }
    }

    private fun openInBrowser() {
        binding?.webview?.url?.let {
            viewUrl(it)
        } ?: crashController.logException(NullPointerException("URL cannot be retreived from webview to open in browser"))
    }

    private fun share() {
        binding?.webview?.url?.let {
            val intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, it)
            startActivity(Intent.createChooser(intent, getString(R.string.choose_share)))
        } ?: crashController.logException(NullPointerException("URL cannot be retreived from webview to share"))
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

    fun exitWeb() {
        binding?.apply {
            webview.onPause()
            webview.stopLoading()
        }
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

        fun bundle(title: String, url: String) = Bundle().apply {
            putString(keyTitle, title)
            putString(keyUrl, url)
        }
    }
}