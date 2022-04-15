package tmg.flashback.releasenotes.ui.releasenotes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

internal class ReleaseActivity : BaseActivity() {

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

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, ReleaseActivity::class.java)
        }
    }
}