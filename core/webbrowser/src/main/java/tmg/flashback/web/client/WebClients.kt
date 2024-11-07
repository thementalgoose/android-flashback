package tmg.flashback.web.client

import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import tmg.utilities.extensions.views.show

internal class FlashbackWebChromeClient(
    val updateProgressToo: (progress: Int) -> Unit
): WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        view?.show(newProgress >= 10)
        updateProgressToo(newProgress)
        super.onProgressChanged(view, newProgress)
    }
}

internal class FlashbackWebViewClient(
    val domainChanged: (domain: String) -> Unit,
    val titleChanged: (title: String) -> Unit,
    val urlChanged: (url: String) -> Unit
): WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
        view?.loadUrl(url)
        val uri = Uri.parse(url)
        urlChanged(url)
        domainChanged(uri.host ?: "")
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        titleChanged(view?.title ?: "...")
    }
}