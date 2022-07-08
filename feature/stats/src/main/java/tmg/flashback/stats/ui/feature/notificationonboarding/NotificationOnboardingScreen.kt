package tmg.flashback.stats.ui.feature.notificationonboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.stats.R
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.components.layouts.BottomSheet

@Composable
fun NotificationOnboardingScreenVM() {
    val viewModel by viewModel<NotificationOnboardingViewModel>()

    viewModel.outputs.notificationPreferences.observeAsState(emptyList())

    NotificationOnboardingScreen()
}

@Composable
fun NotificationOnboardingScreen() {
    BottomSheet(
        title = stringResource(id = R.string.notification_onboarding_title),
        subtitle = stringResource(id = R.string.notification_onboarding_description)
    ) {

        TODO

    }
}