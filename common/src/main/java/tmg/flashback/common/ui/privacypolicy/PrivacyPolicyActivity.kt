package tmg.flashback.common.ui.privacypolicy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyActivity: BaseActivity() {

    private val viewModel: PrivacyPolicyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logScreenViewed("Privacy Policy")
        setContent {
            AppTheme {
                Scaffold(content = {
                    PrivacyPolicyScreen(
                        clickBack = viewModel.inputs::clickBack
                    )
                })
            }
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }
    }
}