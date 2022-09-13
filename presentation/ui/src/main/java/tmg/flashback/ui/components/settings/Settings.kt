package tmg.flashback.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.input.InputSwitch
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.R
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.settings.Setting

private const val disabledAlpha = 0.5f

@Composable
fun SettingPref(
    model: Setting.Pref,
    onClick: (Setting.Pref) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .alpha(if (model.isEnabled) 1f else disabledAlpha)
        .clickable(
            enabled = model.isEnabled,
            onClick = { onClick(model) }
        )
        .padding(
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitle(
                    text = stringResource(id = model.title)
                )
                if (model.isBeta) {
                    Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
                    ExperimentalLabel()
                }
            }
            model.subtitle?.let {
                TextBody2(
                    text = stringResource(id = it),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimensions.paddingXSmall)
                )
            }
        }
    }
}

@Composable
fun SettingSwitch(
    model: Setting.Switch,
    onClick: (Setting.Switch) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .alpha(if (model.isEnabled) 1f else disabledAlpha)
        .clickable(
            enabled = model.isEnabled,
            onClick = { onClick(model) }
        )
        .padding(
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Column(Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitle(
                    text = stringResource(id = model.title)
                )
                if (model.isBeta) {
                    Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
                    ExperimentalLabel()
                }
            }
            model.subtitle?.let {
                TextBody2(
                    text = stringResource(id = it),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimensions.paddingXSmall)
                )
            }
        }
        InputSwitch(
            isChecked = model.isChecked,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun SettingHeader(
    model: Setting.Heading
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = AppTheme.dimensions.paddingSmall,
                horizontal = AppTheme.dimensions.paddingMedium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextSection(
            brand = true,
            text = stringResource(id = model.title)
        )
        if (model.isBeta) {
            Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
            ExperimentalLabel()
        }
    }
}

@Composable
fun SettingSection(
    model: Setting.Section,
    onClick: (model: Setting.Section) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .alpha(if (model.isEnabled) 1f else disabledAlpha)
        .clickable(
            enabled = model.isEnabled,
            onClick = { onClick(model) }
        )
        .padding(
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            painter = painterResource(id = model.icon),
            contentDescription = null,
            tint = AppTheme.colors.contentPrimary
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
        Column(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitle(
                    text = stringResource(id = model.title)
                )
                if (model.isBeta) {
                    Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
                    ExperimentalLabel()
                }
            }
            TextBody2(
                text = stringResource(id = model.subtitle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimensions.paddingXSmall)
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Column(Modifier.fillMaxWidth()) {
            Header(text = "Settings", icon = painterResource(id = R.drawable.ic_back), iconContentDescription = null, actionUpClicked = { })
            SettingHeader(
                model = Setting.Heading.get()
            )
            SettingSection(
                model = Setting.Section.get(),
                onClick = {}
            )
            SettingSection(
                model = Setting.Section.get(isBeta = true),
                onClick = {}
            )
            SettingSection(
                model = Setting.Section.get(isEnabled = false),
                onClick = {}
            )
            SettingHeader(
                model = Setting.Heading.get(isBeta = true)
            )
            SettingPref(
                model = Setting.Pref.get(),
                onClick = {}
            )
            SettingPref(
                model = Setting.Pref.get(isBeta = true),
                onClick = {}
            )
            SettingPref(
                model = Setting.Pref.get(isEnabled = false),
                onClick = {}
            )
            SettingHeader(
                model = Setting.Heading.get()
            )
            SettingSwitch(
                model = Setting.Switch.get(isChecked = true),
                onClick = {}
            )
            SettingSwitch(
                model = Setting.Switch.get(isChecked = false),
                onClick = {}
            )
            SettingSwitch(
                model = Setting.Switch.get(isBeta = true),
                onClick = {}
            )
            SettingSwitch(
                model = Setting.Switch.get(isEnabled = false),
                onClick = {}
            )
            SettingSwitch(
                model = Setting.Switch.get(isEnabled = false, isChecked = true),
                onClick = {}
            )
        }
    }
}

private fun Setting.Heading.Companion.get(
    isBeta: Boolean = false,
) = Setting.Heading(
    _key = "key",
    title = R.string.settings_theme_title,
    isBeta = isBeta
)

private fun Setting.Pref.Companion.get(
    isBeta: Boolean = false,
    isEnabled: Boolean = true
) = Setting.Pref(
    _key = "key",
    title = R.string.settings_theme_theme_title,
    subtitle = R.string.settings_theme_theme_description,
    isEnabled = isEnabled,
    isBeta = isBeta
)

private fun Setting.Switch.Companion.get(
    isChecked: Boolean = false,
    isBeta: Boolean = false,
    isEnabled: Boolean = true
) = Setting.Switch(
    _key = "key",
    isChecked = isChecked,
    title = R.string.settings_theme_theme_title,
    subtitle = R.string.settings_theme_theme_description,
    isEnabled = isEnabled,
    isBeta = isBeta
)

private fun Setting.Section.Companion.get(
    isBeta: Boolean = false,
    isEnabled: Boolean = true
) = Setting.Section(
    _key = "key",
    icon = R.drawable.ic_menu,
    title = R.string.settings_theme_theme_title,
    subtitle = R.string.settings_theme_theme_description,
    isEnabled = isEnabled,
    isBeta = isBeta
)