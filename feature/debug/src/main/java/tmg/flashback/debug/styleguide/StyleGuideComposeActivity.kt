package tmg.flashback.debug.styleguide

import android.os.Bundle
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

@AndroidEntryPoint
class StyleGuideComposeActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                StyleGuideComposeLayout()
            }
        }
    }
}