package tmg.flashback.ui.components.errors

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.R
import tmg.flashback.strings.R.string

@Composable
fun TryAgain(
    modifier: Modifier = Modifier
) {
    ErrorMessage(
        modifier = modifier,
        icon = R.drawable.ic_error_not_available,
        label = string.error_data_not_available_try_again
    )
}

@Composable
fun NotAvailable(
    modifier: Modifier = Modifier
) {
    ErrorMessage(
        modifier = modifier,
        icon = R.drawable.ic_error_not_available,
        label = string.error_weekend_not_available
    )
}

@Composable
fun NotAvailableYet(
    modifier: Modifier = Modifier
) {
    ErrorMessage(
        modifier = modifier,
        icon = R.drawable.ic_error_not_available_yet,
        label = string.error_weekend_not_available_yet
    )
}

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    @DrawableRes
    icon: Int = R.drawable.ic_error_not_available,
    @StringRes
    label: Int
) {
    Row(modifier = modifier.padding(
        vertical = AppTheme.dimens.nsmall,
        horizontal = AppTheme.dimens.medium
    )) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = AppTheme.colors.contentSecondary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.width(16.dp))
        TextBody1(
            text = stringResource(id = label)
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewTryAgain() {
    AppThemePreview {
        TryAgain()
    }
}

@PreviewTheme
@Composable
private fun PreviewNotAvailable() {
    AppThemePreview {
        NotAvailable()
    }
}

@PreviewTheme
@Composable
private fun PreviewNotAvailableYet() {
    AppThemePreview {
        NotAvailableYet()
    }
}