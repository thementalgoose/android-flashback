package tmg.flashback.rss.ui.configure

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import tmg.flashback.rss.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.ui.components.layouts.BottomSheet

@Composable
internal fun AddRSSScreen(
    sourceAdded: (rssLink: String) -> Unit,
    closeSheet: () -> Unit,
) {
    val input = remember { mutableStateOf(TextFieldValue("")) }
    BottomSheet(
        modifier = Modifier
            .imePadding(),
        title = stringResource(id = R.string.settings_rss_add_title),
        subtitle = stringResource(id = R.string.settings_rss_add_description),
        backClicked = closeSheet
    ) {
        InputPrimary(
            modifier = Modifier.padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.nsmall
            ),
            text = input,
            placeholder = "https://www.website.com/rss"
        )

        ButtonPrimary(
            enabled = input.value.text.startsWith("https://"),
            modifier = Modifier.padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.nsmall
            ),
            text = stringResource(id = R.string.settings_rss_add_confirm),
            onClick = {
                sourceAdded(input.value.text.trim())
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        AddRSSScreen(
            sourceAdded = { },
            closeSheet = { }
        )
    }
}