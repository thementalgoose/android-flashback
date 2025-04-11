package tmg.flashback.privacypolicy.contract

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen

val Screen.Settings.PrivacyPolicy: NavigationDestination
    get() = NavigationDestination("privacy_policy")

interface PrivacyPolicyNavigationComponent {
    @Composable
    fun PrivacyPolicy(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass,
        paddingValues: PaddingValues
    )
}