package tmg.flashback.stats.ui.weekend.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.stats.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1

@Composable
fun NotAvailable(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(
        vertical = AppTheme.dimensions.paddingNSmall,
        horizontal = AppTheme.dimensions.paddingMedium
    )) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error_not_available),
            contentDescription = null,
            tint = AppTheme.colors.contentSecondary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.width(16.dp))
        TextBody1(
            text = stringResource(id = R.string.error_weekend_not_available)
        )
    }
}

@Composable
fun NotAvailableYet(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(
        vertical = AppTheme.dimensions.paddingNSmall,
        horizontal = AppTheme.dimensions.paddingMedium
    )) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error_not_available_yet),
            contentDescription = null,
            tint = AppTheme.colors.contentSecondary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.width(16.dp))
        TextBody1(
            text = stringResource(id = R.string.error_weekend_not_available_yet)
        )
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