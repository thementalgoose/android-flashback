package tmg.flashback.privacypolicy

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.privacypolicy.contract.PrivacyPolicyNavigationComponent
import tmg.flashback.privacypolicy.presentation.PrivacyPolicyScreenVM
import javax.inject.Inject

class PrivacyPolicyNavigationComponentImpl @Inject constructor(): PrivacyPolicyNavigationComponent {
    @Composable
    override fun PrivacyPolicy(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        paddingValues: PaddingValues
    ) {
        PrivacyPolicyScreenVM(
            paddingValues = paddingValues,
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
        )
    }
}