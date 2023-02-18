package tmg.flashback.debug.core.styleguide.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.style.text.TextTitle

@Composable
internal fun TextTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppTheme.dimens.medium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextHeadline1(
            text = "Headline 1"
        )
        TextHeadline2(
            text = "Headline 2"
        )
        TextTitle(
            text = "Title"
        )
        TextBody1(
            text = "Body 1"
        )
        TextBody2(
            text = "Body 2"
        )
        TextCaption(
            text = "Caption"
        )
        TextCaption(
            text = "Section"
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        TextTabScreen()
    }
}