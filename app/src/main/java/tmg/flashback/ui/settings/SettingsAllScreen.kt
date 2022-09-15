package tmg.flashback.ui.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Section

@Composable
fun SettingsAllScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel = hiltViewModel<SettingsAllViewModel>()

    val isThemeEnabled = viewModel.outputs.isThemeEnabled.observeAsState(false)
    val isAdsEnabled = viewModel.outputs.isAdsEnabled.observeAsState(false)
    val isRSSEnabled = viewModel.outputs.isRSSEnabled.observeAsState(false)
    SettingsAllScreen(
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::itemClicked,
        isThemeEnabled = isThemeEnabled.value,
        isAdsEnabled = isAdsEnabled.value,
        isRSSEnabled = isRSSEnabled.value
    )
}

@Composable
fun SettingsAllScreen(
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    isThemeEnabled: Boolean,
    isAdsEnabled: Boolean,
    isRSSEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        content = {
            item("header") {
                Header(
                    text = stringResource(id = R.string.settings_title),
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }

//            val input = mutableStateOf(TextFieldValue(""))
//            item("search") {
//                InputPrimary(
//                    modifier = Modifier.padding(
//                        vertical = AppTheme.dimensions.paddingSmall,
//                        horizontal = AppTheme.dimensions.paddingMedium
//                    ),
//                    icon = R.drawable.ic_settings_search,
//                    text = input,
//                    placeholder = stringResource(id = R.string.settings_search)
//                )
//            }

            Header(title = R.string.settings_header_appearance)
            Section(
                model = Settings.Theme.darkMode,
                onClick = prefClicked
            )
            if (isThemeEnabled) {
                Section(
                    model = Settings.Theme.theme,
                    onClick = prefClicked
                )
            }
            Header(title = R.string.settings_header_layout)
            Section(
                model = Settings.Layout.home,
                onClick = prefClicked
            )
            if (isRSSEnabled) {
                Header(title = R.string.settings_header_rss_feed)
                Section(
                    model = Settings.RSS.rss,
                    onClick = prefClicked
                )
            }
            Header(title = R.string.settings_header_web_browser)
            Section(
                model = Settings.Web.inAppBrowser,
                onClick = prefClicked
            )
            Header(title = R.string.settings_header_notifications)
            Section(
                model = Settings.Notifications.notificationUpcoming,
                onClick = prefClicked
            )
            Section(
                model = Settings.Notifications.notificationResults,
                onClick = prefClicked
            )
            if (isAdsEnabled) {
                Header(title = R.string.settings_header_ads)
                Section(
                    model = Settings.Ads.ads,
                    onClick = prefClicked
                )
            }
            Header(title = R.string.settings_header_other)
            Section(
                model = Settings.Other.privacy,
                onClick = prefClicked
            )
            Section(
                model = Settings.Other.about,
                onClick = prefClicked
            )
        }
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SettingsAllScreen(
            actionUpClicked = {},
            prefClicked = {},
            isThemeEnabled = true,
            isAdsEnabled = true,
            isRSSEnabled = true
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNoTheme() {
    AppThemePreview {
        SettingsAllScreen(
            actionUpClicked = {},
            prefClicked = {},
            isThemeEnabled = false,
            isAdsEnabled = false,
            isRSSEnabled = false
        )
    }
}