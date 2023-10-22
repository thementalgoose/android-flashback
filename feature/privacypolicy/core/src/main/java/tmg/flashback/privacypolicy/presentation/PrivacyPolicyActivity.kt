package tmg.flashback.privacypolicy.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

@AndroidEntryPoint
internal class PrivacyPolicyActivity: BaseActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(content = {
                    PrivacyPolicyScreenVM(
                        windowSizeClass = calculateWindowSizeClass(activity = this),
                        actionUpClicked = {
                            finish()
                        }
                    )
                })
            }
        }
    }
}