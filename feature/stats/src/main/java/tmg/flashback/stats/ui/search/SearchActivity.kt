package tmg.flashback.stats.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class SearchActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                SearchScreenVM(
                    actionUpClicked = {
                        onBackPressed()
                    }
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
}