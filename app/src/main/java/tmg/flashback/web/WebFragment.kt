package tmg.flashback.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_web.*
import tmg.flashback.R
import tmg.flashback.base.BaseFragment
import tmg.flashback.utils.FragmentRequestBack
import tmg.flashback.utils.getColor
import tmg.utilities.extensions.views.show

@SuppressLint("SetJavaScriptEnabled")
class WebFragment : BaseFragment() {

    private var backCallback: FragmentRequestBack? = null

    private lateinit var pageTitle: String
    private lateinit var pageUrl: String

    override fun layoutId(): Int = R.layout.fragment_web

    override fun arguments(bundle: Bundle) {
        this.pageTitle = bundle.getString(keyTitle)!!
        this.pageUrl = bundle.getString(keyUrl)!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentRequestBack) {
            backCallback = context
        }
    }

    override fun initViews() {

        progressBar.progressColour = context?.theme?.getColor(R.attr.colorPrimary) ?: ContextCompat.getColor(requireContext(), R.color.colorTheme)
        progressBar.timeLimit = 100

        val webViewClient = FlashbackWebViewClient(
            domainChanged = { domain?.text = it },
            titleChanged = { title?.text = it }
        )
        val webChromeClient = FlashbackWebChromeClient(
            updateProgressToo = {
                val result = it.toFloat() / 100f
                progressBar?.animateProgress(result, false) { "" }
                progressBar?.show(result != 1.0f)
            }
        )

        webview.webChromeClient = webChromeClient
        webview.webViewClient = webViewClient
        webview.settings.loadsImagesAutomatically = true
        webview.settings.javaScriptEnabled = true
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        load(pageTitle, pageUrl)

        back.setOnClickListener {
            if (webview.canGoBack()) {
                webview.goBack()
            }
            else {
                exitWeb()
                backCallback?.fragmentBackPressed()
            }
        }

        openInBrowser.setOnClickListener {
            openInBrowser()
        }

        share.setOnClickListener {
            share()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(keyTitle, title.text.toString())
        outState.putString(keyUrl, webview.url)
        super.onSaveInstanceState(outState)
    }

    fun load(pageTitle: String, url: String) {
        this.pageTitle = pageTitle
        this.pageUrl = url
        webview.loadUrl(pageUrl)

        title.text = pageTitle
        domain.text = Uri.parse(pageUrl).host ?: "-"
    }

    private fun openInBrowser() {
        val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(webview.url))
        startActivity(Intent.createChooser(intent, getString(R.string.choose_browser)))
    }

    private fun share() {
        val intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, webview.url)
        startActivity(Intent.createChooser(intent, getString(R.string.choose_share)))
    }

    override fun onPause() {
        super.onPause()
        webview.onPause()
    }

    override fun onResume() {
        super.onResume()
        webview.onResume()
    }

    fun exitWeb() {
        webview.onPause()
        webview.stopLoading()
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