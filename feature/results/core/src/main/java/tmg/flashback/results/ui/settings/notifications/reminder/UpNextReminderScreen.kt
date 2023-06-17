package tmg.flashback.results.ui.settings.notifications.reminder

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.results.R
import tmg.flashback.results.repository.models.NotificationReminder
import tmg.flashback.style.AppTheme
import tmg.flashback.style.input.InputSelection
import tmg.flashback.ui.components.layouts.BottomSheet

@Composable
fun UpNextReminderScreenVM(
    viewModel: UpNextReminderViewModel = hiltViewModel(),
    dismiss: () -> Unit
) {
    val current = viewModel.outputs.currentlySelected.collectAsState(NotificationReminder.MINUTES_30)
    UpNextReminderScreen(
        currentlySelected = current.value,
        itemClicked = viewModel.inputs::selectNotificationReminder,
        dismiss = dismiss
    )
}

@Composable
fun UpNextReminderScreen(
    currentlySelected: NotificationReminder,
    itemClicked: (NotificationReminder) -> Unit,
    dismiss: () -> Unit
) {
    BottomSheet(
        title = stringResource(id = R.string.settings_up_next_reminder_title),
        subtitle = stringResource(id = R.string.settings_up_next_reminder_description),
        backClicked = dismiss,
        content = {
            NotificationReminder.values()
                .sortedByDescending { it.seconds }
                .forEach { reminder ->
                    InputSelection(
                        modifier = Modifier.padding(
                            horizontal = AppTheme.dimens.medium,
                            vertical = AppTheme.dimens.xsmall
                        ),
                        label = stringResource(id = reminder.label),
                        icon = reminder.icon,
                        isSelected = reminder == currentlySelected,
                        itemClicked = {
                            itemClicked(reminder)
                            dismiss()
                        }
                    )
                }
        }
    )
}