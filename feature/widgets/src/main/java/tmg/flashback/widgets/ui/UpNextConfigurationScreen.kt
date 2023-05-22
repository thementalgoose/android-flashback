package tmg.flashback.widgets.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.settings.SettingSwitch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.widgets.R

@Composable
internal fun UpNextConfigurationScreenVM(
    actionUpClicked: () -> Unit,
    viewModel: UpNextConfigurationViewModel = hiltViewModel()
) {
    val showBackground = viewModel.outputs.showBackground.observeAsState(initial = false)

    UpNextConfigurationScreen(
        actionUpClicked = actionUpClicked,
        showBackground = showBackground.value,
        updateShowBackground = viewModel.inputs::changeShowBackground,
        save = viewModel.inputs::save
    )
}

@Composable
private fun UpNextConfigurationScreen(
    actionUpClicked: () -> Unit,
    showBackground: Boolean,
    updateShowBackground: (Boolean) -> Unit,
    save: () -> Unit
) {
    Column(Modifier
        .fillMaxSize()
        .background(AppTheme.colors.backgroundPrimary)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    item {
                        Header(
                            text = stringResource(id = R.string.widget_up_next_title),
                            icon = painterResource(id = R.drawable.ic_close),
                            iconContentDescription = stringResource(id = R.string.ab_close),
                            actionUpClicked = { actionUpClicked() }
                        )
                    }
                    item {
                        SettingSwitch(
                            model = Setting.Switch(
                                _key = "show_background",
                                title = string.widget_settings_background_title,
                                subtitle = string.widget_settings_background_description,
                                isChecked = showBackground
                            ),
                            onClick = { updateShowBackground(!showBackground) }
                        )
                    }
                }
            )
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(AppTheme.dimens.large)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, AppTheme.colors.backgroundPrimary)))
            )
        }
        Column(Modifier.fillMaxWidth()) {
            ButtonPrimary(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.medium,
                    vertical = AppTheme.dimens.medium
                ),
                text = stringResource(id = string.widget_up_next_save),
                onClick = { save() }
            )
        }
    }
}