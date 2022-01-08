package tmg.flashback.rss.ui.settings.configure.layouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.rss.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.input.InputPrimary

@Composable
internal fun RssCustomLayout(
    text: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    addClicked: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .defaultMinSize(0.dp, 0.dp)
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = 10.dp,
                bottom = 10.dp
            )
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        InputPrimary(
            text = text,
            placeholder = "https://formula1.com/link/to/rss",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingSmall))
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .clickable(
                    onClick = {
                        addClicked(text.value.text)
                    }
                ),
            painter = painterResource(R.drawable.ic_rss_configure_add),
            tint = AppTheme.colors.rssAdd,
            contentDescription = stringResource(id = R.string.ab_rss_configure_add)
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        RssCustomLayout(
            text = textState,
            addClicked = { }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        RssCustomLayout(
            text = textState,
            addClicked = { }
        )
    }
}