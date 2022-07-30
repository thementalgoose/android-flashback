package tmg.flashback.stats.ui.feature.notificationonboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.stats.R
import tmg.flashback.stats.repository.models.NotificationChannel
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.layouts.BottomSheet
import tmg.utilities.models.Selected

@Composable
fun NotificationOnboardingScreenVM() {
    val viewModel = hiltViewModel<NotificationOnboardingViewModel>()
    
    ScreenView(screenName = "Onboarding Notifications")

    val list = viewModel.outputs.notificationPreferences.observeAsState(emptyList())

    NotificationOnboardingScreen(
        list = list.value,
        itemClicked = {
            viewModel.inputs.selectNotificationChannel(it.channel)
        }
    )
}

@Composable
fun NotificationOnboardingScreen(
    list: List<Selected<NotificationOnboardingModel>>,
    itemClicked: (NotificationOnboardingModel) -> Unit
) {
    BottomSheet(
        title = stringResource(id = R.string.notification_onboarding_title),
        subtitle = stringResource(id = R.string.notification_onboarding_description)
    ) {
        list.forEach { item ->
            Item(
                model = item,
                itemClicked = itemClicked
            )
        }
    }
}

@Composable
private fun Item(
    model: Selected<NotificationOnboardingModel>,
    itemClicked: (NotificationOnboardingModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = {
            itemClicked(model.value)
        })
        .padding(
            horizontal = AppTheme.dimensions.paddingMedium,
            vertical = AppTheme.dimensions.paddingSmall
        )
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = model.value.icon),
            contentDescription = null,
        )
        TextBody1(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(
                    horizontal = AppTheme.dimensions.paddingSmall
                ),
            text = stringResource(id = model.value.name)
        )
        Checkbox(
            checked = model.isSelected,
            onCheckedChange = null
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        NotificationOnboardingScreen(
            list = listOf(
                Selected(NotificationOnboardingModel(
                    id = "1",
                    channel = NotificationChannel.QUALIFYING,
                    name = R.string.notification_channel_race,
                    icon = R.drawable.ic_notification_reminder_15
                ), false),
                Selected(NotificationOnboardingModel(
                    id = "3",
                    channel = NotificationChannel.FREE_PRACTICE,
                    name = R.string.notification_channel_race,
                    icon = R.drawable.ic_notification_reminder_60
                ), false),
                Selected(NotificationOnboardingModel(
                    id = "2",
                    channel = NotificationChannel.RACE,
                    name = R.string.notification_channel_race,
                    icon = R.drawable.ic_notification_reminder_30
                ), true)
            ),
            itemClicked = {}
        )
    }
}