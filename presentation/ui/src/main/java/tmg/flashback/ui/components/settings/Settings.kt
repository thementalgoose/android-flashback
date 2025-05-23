package tmg.flashback.ui.components.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.input.InputRadio
import tmg.flashback.style.input.InputSwitch
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.R
import tmg.flashback.strings.R.string
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.settings.Setting

private const val disabledAlpha = 0.5f

fun LazyListScope.Pref(
    model: Setting.Pref,
    onClick: (Setting.Pref) -> Unit
) {
    item(key = model.key) {
        SettingPref(
            model = model,
            onClick = onClick
        )
    }
}

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
            vertical = AppTheme.dimens.nsmall,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        if (model.icon != null) {
            if (model.icon == 0) {
                Box(Modifier
                    .size(32.dp)
                    .padding(start = AppTheme.dimens.small)
                )
            } else {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = AppTheme.dimens.small),
                    painter = painterResource(id = model.icon),
                    contentDescription = null,
                    tint = AppTheme.colors.contentPrimary
                )
            }
            Spacer(Modifier.width(AppTheme.dimens.medium))
        }
        Column(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitle(
                    text = stringResource(id = model.title)
                )
                if (model.isBeta) {
                    Spacer(Modifier.width(AppTheme.dimens.small))
                    ExperimentalLabel()
                }
            }
            model.subtitle?.let {
                TextBody2(
                    text = stringResource(id = it),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimens.xsmall)
                )
            }
        }
    }
}


fun LazyListScope.Switch(
    model: Setting.Switch,
    onClick: (Setting.Switch) -> Unit
) {
    item(key = model.key) {
        SettingSwitch(
            model = model,
            onClick = onClick
        )
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
        .toggleable(model.isChecked, onValueChange = {
            onClick(model)
        }, enabled = model.isEnabled)
        .padding(
            vertical = AppTheme.dimens.nsmall,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        if (model.icon != null) {
            if (model.icon == 0) {
                Box(Modifier
                    .size(32.dp)
                    .padding(start = AppTheme.dimens.small)
                )
            } else {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = AppTheme.dimens.small),
                    painter = painterResource(id = model.icon),
                    contentDescription = null,
                    tint = AppTheme.colors.contentPrimary
                )
            }
            Spacer(Modifier.width(AppTheme.dimens.medium))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = AppTheme.dimens.medium),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitle(
                    text = stringResource(id = model.title)
                )
                if (model.isBeta) {
                    Spacer(Modifier.width(AppTheme.dimens.small))
                    ExperimentalLabel()
                }
            }
            model.subtitle?.let {
                TextBody2(
                    text = stringResource(id = it),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimens.xsmall)
                )
            }
        }
        InputSwitch(
            isChecked = model.isChecked,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

fun LazyListScope.Option(
    model: Setting.Option,
    onClick: (Setting.Option) -> Unit
) {
    item(key = model.key) {
        SettingOption(
            model = model,
            onClick = onClick
        )
    }
}

@Composable
fun SettingOption(
    model: Setting.Option,
    onClick: (Setting.Option) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .alpha(if (model.isEnabled) 1f else disabledAlpha)
        .toggleable(model.isChecked, onValueChange = {
            onClick(model)
        }, enabled = model.isEnabled)
        .padding(
            vertical = AppTheme.dimens.nsmall,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = AppTheme.dimens.medium),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitle(
                    text = stringResource(id = model.title)
                )
                if (model.isBeta) {
                    Spacer(Modifier.width(AppTheme.dimens.small))
                    ExperimentalLabel()
                }
            }
            model.subtitle?.let {
                TextBody2(
                    text = stringResource(id = it),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimens.xsmall)
                )
            }
        }
        InputRadio(
            isChecked = model.isChecked,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

fun LazyListScope.Header(
    @StringRes
    title: Int
) {
    this.Header(model = Setting.Heading(
        _key = "heading_$title",
        title = title
    ))
}
fun LazyListScope.Header(
    model: Setting.Heading
) {
    item(key = model.key) {
        SettingHeader(model = model)
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
                top = AppTheme.dimens.medium,
                bottom = AppTheme.dimens.nsmall,
                start = AppTheme.dimens.medium,
                end = AppTheme.dimens.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextSection(
            brand = true,
            text = stringResource(id = model.title)
        )
        if (model.isBeta) {
            Spacer(Modifier.width(AppTheme.dimens.small))
            ExperimentalLabel()
        }
    }
}


fun LazyListScope.Footer(
    height: Dp = AppTheme.dimens.large
) {
    item(key = "footer") {
        Spacer(Modifier.height(height))
    }
}

fun LazyListScope.Section(
    model: Setting.Section,
    onClick: (Setting.Section) -> Unit
) {
    item(key = model.key) {
        SettingSection(
            model = model,
            onClick = onClick
        )
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
            vertical = AppTheme.dimens.nsmall,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = AppTheme.dimens.small),
            painter = painterResource(id = model.icon),
            contentDescription = null,
            tint = AppTheme.colors.contentPrimary
        )
        Spacer(Modifier.width(AppTheme.dimens.medium))
        Column(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitle(
                    text = stringResource(id = model.title)
                )
                if (model.isBeta) {
                    Spacer(Modifier.width(AppTheme.dimens.small))
                    ExperimentalLabel()
                }
            }
            TextBody2(
                text = stringResource(id = model.subtitle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimens.xsmall)
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Column(Modifier.fillMaxWidth()) {
            Header(
                text = "Settings",
                action = HeaderAction.BACK,
                actionUpClicked = { }
            )
            SettingHeader(
                model = Setting.Heading.get()
            )
            SettingSection(
                model = Setting.Section.get(),
                onClick = {}
            )
            SettingSection(
                model = Setting.Section.get(isEnabled = false),
                onClick = {}
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
            SettingPref(
                model = Setting.Pref.get(icon = R.drawable.ic_menu),
                onClick = {}
            )
            SettingPref(
                model = Setting.Pref.get(icon = 0),
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
                model = Setting.Switch.get(isBeta = true),
                onClick = {}
            )
            SettingSwitch(
                model = Setting.Switch.get(isEnabled = false, isChecked = true),
                onClick = {}
            )
            SettingSwitch(
                model = Setting.Switch.get(icon = R.drawable.ic_menu_drivers),
                onClick = {}
            )
            SettingSwitch(
                model = Setting.Switch.get(icon = 0),
                onClick = {}
            )
        }
    }
}

private fun Setting.Heading.Companion.get(
    isBeta: Boolean = false,
) = Setting.Heading(
    _key = "key",
    title = string.settings_theme_title,
    isBeta = isBeta
)

private fun Setting.Pref.Companion.get(
    @DrawableRes
    icon: Int? = null,
    isBeta: Boolean = false,
    isEnabled: Boolean = true
) = Setting.Pref(
    _key = "key",
    title = string.settings_theme_theme_title,
    subtitle = string.settings_theme_theme_description,
    icon = icon,
    isEnabled = isEnabled,
    isBeta = isBeta
)

private fun Setting.Switch.Companion.get(
    @DrawableRes
    icon: Int? = null,
    isChecked: Boolean = false,
    isBeta: Boolean = false,
    isEnabled: Boolean = true
) = Setting.Switch(
    _key = "key",
    isChecked = isChecked,
    title = string.settings_theme_theme_title,
    subtitle = string.settings_theme_theme_description,
    icon = icon,
    isEnabled = isEnabled,
    isBeta = isBeta
)

private fun Setting.Section.Companion.get(
    isBeta: Boolean = false,
    isEnabled: Boolean = true
) = Setting.Section(
    _key = "key",
    icon = R.drawable.ic_menu,
    title = string.settings_theme_theme_title,
    subtitle = string.settings_theme_theme_description,
    isEnabled = isEnabled,
    isBeta = isBeta
)