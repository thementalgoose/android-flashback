package tmg.flashback.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_web.*
import tmg.flashback.R
import tmg.flashback.base.BaseFragment
import tmg.flashback.utils.FragmentRequestBack

@SuppressLint("SetJavaScriptEnabled")
class WebFragment: BaseFragment() {

    private var backCallback: FragmentRequestBack? = null

    private lateinit var pageTitle: String
    private lateinit var pageUrl: String

    private val webViewClient: FlashbackWebViewClient = FlashbackWebViewClient()
    private val webChromeClient: FlashbackWebChromeClient = FlashbackWebChromeClient()

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

        webview.webChromeClient = webChromeClient
        webview.webViewClient = webViewClient
        webview.settings.loadsImagesAutomatically = true
        webview.settings.javaScriptEnabled = true
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.loadUrl(pageUrl)

        title.text = pageTitle

        back.setOnClickListener {
            backCallback?.fragmentBackPressed()
        }

        openInBrowser.setOnClickListener {
            openInBrowser()
        }

        share.setOnClickListener {
            share()
        }
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