package tmg.flashback.settings.ui.privacypolicy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.observeEvent

internal class PrivacyPolicyActivity: BaseActivity() {

    private val viewModel: PrivacyPolicyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(content = {
                    PrivacyPolicyScreenVM(
                        actionUpClicked = viewModel.inputs::clickBack
                    )
                })
            }
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, PrivacyPolicyActivity::class.java)
        }
    }
}