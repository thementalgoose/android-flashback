package tmg.flashback.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.fragment_web.*
import tmg.flashback.R
import tmg.flashback.base.BaseFragment

@SuppressLint("SetJavaScriptEnabled")
class WebFragment: BaseFragment() {

    private val webViewClient: FlashbackWebViewClient = FlashbackWebViewClient()
    private val webChromeClient: FlashbackWebChromeClient = FlashbackWebChromeClient()

    override fun layoutId(): Int = R.layout.fragment_web

    override fun arguments(bundle: Bundle) {

    }

    override fun initViews() {

        webview.webChromeClient = webChromeClient
        webview.webViewClient = webViewClient
        webview.settings.loadsImagesAutomatically = true
        webview.settings.javaScriptEnabled = true
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.loadUrl("https://old.reddit.com/r/formula1")
    }
}