package tmg.flashback.maintenance.presentation.forceupgrade

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.components.layouts.AppScaffold
import tmg.flashback.ui.model.DisplayType
import tmg.utilities.extensions.viewUrl

@AndroidEntryPoint
class ForceUpgradeActivity: BaseActivity() {

    private val viewModel: ForceUpgradeViewModel by viewModels()

    override val themeType: DisplayType = DisplayType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                AppScaffold(
                    content = {
                        val title = viewModel.outputs.title.collectAsState("")
                        val message = viewModel.outputs.message.collectAsState("")
                        val showLink = viewModel.outputs.showLink.collectAsState(null)

                        ScreenView(screenName = "Force upgrade")

                        ForceUpgradeScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(AppTheme.colors.backgroundContainer)
                                .padding(it),
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