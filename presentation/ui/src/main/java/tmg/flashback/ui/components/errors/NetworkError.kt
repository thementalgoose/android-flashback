package tmg.flashback.ui.components.errors

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.R

enum class NetworkError(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
) {
    NETWORK_ERROR(
        label = R.string.network_error_network,
        icon = R.drawable.error_network
    ),
    INTERNAL_ERROR(
        label = R.string.network_error_internal,
        icon = R.drawable.error_internal
    )
}

@Composable
fun NetworkError(
    modifier: Modifier = Modifier,
    error: NetworkError = NetworkError.NETWORK_ERROR
) {
    Row(modifier = modifier
        .padding(
            vertical = AppTheme.dimensions.paddingMedium,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Icon(
            painter = painterResource(id = error.icon),
            contentDescription = null,
            tint = AppTheme.colors.contentSecondary
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
        TextBody1(text = stringResource(id = error.label))
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        NetworkError()
    }
}