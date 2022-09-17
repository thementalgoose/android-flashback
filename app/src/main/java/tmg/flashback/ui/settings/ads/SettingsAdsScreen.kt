package tmg.flashback.ui.settings.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsAdsScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel = hiltViewModel<SettingsAdsViewModel>()

    ScreenView(screenName = "Settings - Ads")

    val adsEnabled = viewModel.outputs.adsEnabled.observeAsState(false)
    SettingsAdsScreen(
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        adsEnabled = adsEnabled.value
    )
}

@Composable
fun SettingsAdsScreen(
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    adsEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_section_ads_title),
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_ads)
            Switch(
                model = Settings.Ads.enableAds(adsEnabled),
                onClick = prefClicked
            )
        }
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SettingsAdsScreen(
            actionUpClicked = {},
            prefClicked = {},
            adsEnabled = false
        )
    }
}