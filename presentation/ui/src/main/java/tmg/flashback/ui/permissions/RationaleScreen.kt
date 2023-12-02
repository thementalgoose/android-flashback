package tmg.flashback.ui.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.device.AppPermissions
import tmg.flashback.ui.R
import tmg.flashback.strings.R.string

@Composable
internal fun RationaleScreen(
    type: List<AppPermissions.RuntimePermission>,
    confirmClicked: () -> Unit,
    cancelClicked: () -> Unit,
) {
    Column(modifier = Modifier.padding(
        horizontal = AppTheme.dimens.medium,
        vertical = AppTheme.dimens.medium
    )) {
        TextHeadline2(text = stringResource(id = string.permissions_rationale_title))

        type.forEach { rationale ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppTheme.dimens.medium,
                    horizontal = AppTheme.dimens.small
                )
            ) {
                Icon(
                    painter = painterResource(id = rationale.icon),
                    contentDescription = null,
                    tint = AppTheme.colors.contentPrimary
                )
                TextBody1(
                    modifier = Modifier.padding(
                        horizontal = AppTheme.dimens.medium
                    ),
                    text = stringResource(rationale.description)
                )
            }
        }
        Row(modifier = Modifier.padding(
            top = AppTheme.dimens.medium
        )) {
            ButtonTertiary(
                modifier = Modifier.weight(1f),
                narrow = false,
                text = stringResource(id = string.permissions_rationale_cancel),
                onClick = cancelClicked
            )
            Spacer(Modifier.width(AppTheme.dimens.medium))
            ButtonPrimary(
                modifier = Modifier.weight(1f),
                text = stringResource(id = string.permissions_rationale_confirm),
                onClick = confirmClicked
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        RationaleScreen(
            type = listOf(AppPermissions.RuntimeNotifications),
            confirmClicked = { },
            cancelClicked = { }
        )
    }
}