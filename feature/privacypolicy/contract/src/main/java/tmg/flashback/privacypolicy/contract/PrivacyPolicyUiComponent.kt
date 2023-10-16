package tmg.flashback.privacypolicy.contract

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface PrivacyPolicyUiComponent {
    fun privacyPolicyNav(): PrivacyPolicyNavigationComponent
}

private lateinit var uiComponent: PrivacyPolicyUiComponent
@Composable
fun requirePrivacyPolicyNavigationComponent(): PrivacyPolicyNavigationComponent {
    if (!::uiComponent.isInitialized) {
        uiComponent = EntryPoints.get(LocalContext.current.applicationContext, PrivacyPolicyUiComponent::class.java)
    }
    return uiComponent.privacyPolicyNav()
}