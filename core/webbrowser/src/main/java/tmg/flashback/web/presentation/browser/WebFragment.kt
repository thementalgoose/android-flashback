package tmg.flashback.web.presentation.browser

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.web.R
import tmg.flashback.web.client.FlashbackWebChromeClient
import tmg.flashback.web.client.FlashbackWebViewClient
import tmg.flashback.web.databinding.FragmentWebBinding
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.show
import java.net.MalformedURLException
import javax.inject.Inject

@SuppressLint("SetJavaScriptEnabled")
@AndroidEntryPoint
internal class WebFragment : Fragment() {

    // Binding stripped out and nullable due to loading of webpages and fragment lifecycle causing
    // binding to throw NPE
    private var binding: FragmentWebBinding? = null

    @Inject
    protected lateinit var firebaseAnalyticsManager: FirebaseAnalyticsManager
    @Inject
    protected lateinit var crashController: CrashlyticsManager

    @Inject
    protected lateinit var webBrowserRepository: WebBrowserRepository

    internal lateinit var pageTitle: String
    internal lateinit var pageUrl: String

    internal var callback: WebUpdated? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null)
            binding?.webview?.restoreState(savedInstanceState.getBundle("webViewState")!!);
    }

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                progressBar.progressDrawable.colorFilter = BlendModeColorFilter(Color.RED, BlendMode.SRC_IN)
            }
            progressBar.max = 100
        }

        val webViewClient = FlashbackWebViewClient(
            domainChanged = { callback?.domainChanged(it) },
            titleChanged = { callback?.titleChanged(it) }
        )
        val webChromeClient = FlashbackWebChromeClient(
            updateProgressToo = {
                val result = it.toFloat() / 100f
                binding?.progressBar?.progress = it.coerceIn(0, 100)
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
                    firebaseAnalyticsManager.viewScreen(
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
        super.onSaveInstanceState(outState)
        val bundle = Bundle()
        binding?.webview?.saveState(bundle)
        outState.putBundle("webViewState", bundle)
        binding?.let {
            outState.putString(keyTitle, it.webview.title.toString())
            outState.putString(keyUrl, it.webview.url)
        }
        outState.putBoolean("hasRestored", true)
    }

    fun load(pageTitle: String, url: String) {
        this.pageTitle = pageTitle
        this.pageUrl = url

        Log.i("WebView", "Arguments ${arguments}")
        if (arguments?.getBoolean("hasRestored") != true) {
            binding!!.apply {
                webview.loadUrl(pageUrl)
            }
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