package tmg.flashback.debug.core.styleguide

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import tmg.flashback.debug.core.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.*
import tmg.flashback.ui.components.header.Header

@Composable
fun StyleGuideComposeLayout(
    modifier: Modifier = Modifier,
    actionUpClicked: () -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
    ) {
        Header(
            text = "Style Guide",
            icon = painterResource(id = R.drawable.ic_back),
            iconContentDescription = null,
            actionUpClicked = actionUpClicked
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimens.xlarge,
                    start = AppTheme.dimens.medium,
                    end = AppTheme.dimens.medium,
                    bottom = AppTheme.dimens.medium
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
                    ButtonPrimary(text = "Primary", onClick = { })
                    Spacer(modifier = Modifier.height(16.dp))
                    ButtonSecondary(text = "Secondary", onClick = { })
                    Spacer(modifier = Modifier.height(16.dp))
                    ButtonTertiary(text = "Tertiary", onClick = { })
                    Spacer(modifier = Modifier.height(16.dp))
                    val state = remember { mutableStateOf(TextFieldValue("Primary Input")) }
                    InputPrimary(text = state, placeholder = "input_placeholder")
                }
        }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        StyleGuideComposeLayout(
            actionUpClicked = { }
        )
    }
}
