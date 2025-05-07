package tmg.flashback.web.presentation.browser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.components.layouts.AppScaffold
import tmg.flashback.web.R
import tmg.utilities.extensions.viewUrl

@AndroidEntryPoint
internal class WebActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val title: String = intent.extras!!.getString(keyTitle)!!
        val url: String = intent.extras!!.getString(keyUrl)!!

        setContent {
            AppTheme {
                AppScaffold(
                    content = {
                        Box(modifier = Modifier
                            .background(AppTheme.colors.backgroundPrimary)
                        ) {
                            WebScreenVM(
                                title = title,
                                url = url,
                                actionUpClicked = {
                                    finish()
                                }
                            )
                        }
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