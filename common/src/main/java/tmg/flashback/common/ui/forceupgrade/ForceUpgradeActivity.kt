package tmg.flashback.common.ui.forceupgrade

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.model.DisplayType
import tmg.utilities.extensions.viewUrl

internal class ForceUpgradeActivity: BaseActivity() {

    private val viewModel: ForceUpgradeViewModel by viewModel()

    override val themeType: DisplayType = DisplayType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Force upgrade")

        setContent {
            AppTheme {
                Scaffold(
                    content = {
                        val title = viewModel.outputs.title.observeAsState("")
                        val message = viewModel.outputs.message.observeAsState("")
                        val showLink = viewModel.outputs.showLink.observeAsState(null)

                        ForceUpgradeScreen(
                            title = title.value,
                            description = message.value,
                            link = showLink.value,
                            openLink = { link ->
                                try {
                                    viewUrl(link)
                                } catch (e: ActivityNotFoundException) {
                                    finish()
                                }
                            }
                        )
                    }
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, ForceUpgradeActivity::class.java)
        }
    }
}