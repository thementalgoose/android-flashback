package tmg.flashback.privacypolicy.presentation

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.viewinterop.AndroidView
import tmg.flashback.privacypolicy.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.strings.R.string
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.htmltext.HtmlText
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.getColor

@Composable
fun PrivacyPolicyScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    ScreenView(screenName = "Privacy Policy")

    Column(
        modifier = Modifier
            .background(AppTheme.colors.backgroundPrimary)
            .verticalScroll(rememberScrollState())
    ) {
        Header(
            text = stringResource(id = string.privacy_policy_title),
            action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
            actionUpClicked = actionUpClicked
        )
        HtmlText(
            modifier = Modifier.padding(horizontal = AppTheme.dimens.medium),
            html = stringResource(string.privacy_policy_data),
            linkColor = AppTheme.colors.primary
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        PrivacyPolicyScreenVM(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            actionUpClicked = {}
        )
    }
}