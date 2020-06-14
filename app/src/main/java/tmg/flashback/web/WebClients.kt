package tmg.flashback.web

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import tmg.utilities.extensions.views.show

class FlashbackWebChromeClient(
    val updateProgressToo: (progress: Int) -> Unit
): WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        Log.i("Flashback", "Progress $newProgress")
        view?.show(newProgress >= 10)
        updateProgressToo(newProgress)
        super.onProgressChanged(view, newProgress)
    }
}

class FlashbackWebViewClient(
    val domainChanged: (domain: String) -> Unit,
    val titleChanged: (title: String) -> Unit
): WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url)
        val uri = Uri.parse(url)
        domainChanged(uri.host ?: "")
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        titleChanged(view?.title ?: "...")
    }
}