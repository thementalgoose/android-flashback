package tmg.flashback.web.ui.browser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.web.R
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.viewUrl

internal class WebActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title: String = intent.extras!!.getString(keyTitle)!!
        val url: String = intent.extras!!.getString(keyUrl)!!

        setContent {
            AppTheme {
                WebScreenVM(
                    title = title,
                    url = url,
                    actionUpClicked = {
                        finish()
                    },
                    shareClicked = {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, it)
                        startActivity(Intent.createChooser(intent, getString(R.string.choose_share)))
                    },
                    openInBrowser = {
                        viewUrl(it)
                    }
                )
            }
        }
    }

    companion object {

        private const val keyTitle: String = "title"
        private const val keyUrl: String = "url"

        fun intent(context: Context, url: String, title: String = ""): Intent {
            return Intent(context, WebActivity::class.java).apply {
                putExtra(keyUrl, url)
                putExtra(keyTitle, title)
            }
        }
    }
}