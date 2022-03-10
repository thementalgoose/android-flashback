package tmg.flashback.common.ui.releasenotes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class ReleaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logScreenViewed("Release notes")

        setContent {
            AppTheme {
                Scaffold(
                    content = {
                        ReleaseScreen(
                            clickBack = {
                                finish()
                            }
                        )
                    }
                )
            }
        }
    }
}