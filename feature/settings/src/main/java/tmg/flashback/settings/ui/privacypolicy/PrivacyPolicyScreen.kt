package tmg.flashback.settings.ui.privacypolicy

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import tmg.flashback.settings.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.header.Header
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.getColor

@Composable
fun PrivacyPolicyScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Privacy Policy")
    
    Column(
        modifier = Modifier
            .background(AppTheme.colors.backgroundPrimary)
            .verticalScroll(rememberScrollState())
    ) {
        Header(
            text = stringResource(id = R.string.privacy_policy_title),
            icon = painterResource(id = R.drawable.ic_back),
            iconContentDescription = stringResource(id = R.string.ab_back),
            actionUpClicked = actionUpClicked
        )
        AndroidView(
            modifier = Modifier.padding(
                top = AppTheme.dimensions.paddingSmall,
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingMedium
            ),
            factory = {
                TextView(it).apply {
                    setTextColor(it.theme.getColor(R.attr.contentPrimary))
                    text = it.getString(R.string.privacy_policy_data).fromHtml()
                    movementMethod = LinkMovementMethod.getInstance()
                }
            }
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        PrivacyPolicyScreenVM(
            actionUpClicked = {}
        )
    }
}