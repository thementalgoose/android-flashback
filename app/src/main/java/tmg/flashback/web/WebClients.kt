package tmg.flashback.web

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class FlashbackWebChromeClient: WebChromeClient() {

}

class FlashbackWebViewClient: WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url)
        return true
    }
}