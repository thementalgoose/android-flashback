package tmg.flashback.privacypolicy.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.observe

@AndroidEntryPoint
internal class PrivacyPolicyActivity: BaseActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(content = {
                    PrivacyPolicyScreenVM(
                        actionUpClicked = {
                            finish()
                        }
                    )
                })
            }
        }
    }
}