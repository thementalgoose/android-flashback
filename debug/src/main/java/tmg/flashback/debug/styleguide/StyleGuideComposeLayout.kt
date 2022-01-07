package tmg.flashback.debug.styleguide

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.*

@Composable
fun StyleGuideComposeLayout(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = AppTheme.dimensions.paddingXLarge,
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingMedium
            )
    ) {
        TextHeadline1(text = "Headline 1")
        Spacer(modifier = Modifier.height(16.dp))
        TextHeadline2(text = "Headline 2")
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextTitle(
                text = "Title",
                modifier = Modifier.weight(1f)
            )
            TextTitle(
                text = "Title Strong",
                bold = true,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextSection(text = "Section")
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextBody1(
                text = "Body 1",
                modifier = Modifier.weight(1f)
            )
            TextBody1(
                text = "Body 1 Strong",
                bold = true,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextBody2(text = "Body 2")
        Spacer(modifier = Modifier.height(16.dp))
        TextCaption(text = "Caption")
        Spacer(modifier = Modifier.height(16.dp))
        ButtonPrimary(text = "Primary")
        Spacer(modifier = Modifier.height(16.dp))
        ButtonSecondary(text = "Secondary")
        Spacer(modifier = Modifier.height(16.dp))
        ButtonTertiary(text = "Tertiary")
        Spacer(modifier = Modifier.height(16.dp))
        val state = remember { mutableStateOf(TextFieldValue("Primary Input")) }
        InputPrimary(text = state)
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppTheme(isLight = true) {
        StyleGuideComposeLayout()
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(isLight = false) {
        StyleGuideComposeLayout()
    }
}
