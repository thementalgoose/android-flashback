package tmg.flashback.style.input

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextTitle

@Composable
fun InputPrimary(
    text: MutableState<TextFieldValue>,
    placeholder: String,
    modifier: Modifier = Modifier,
    @DrawableRes
    icon: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions(),
    imeAction: ImeAction = ImeAction.Default,
    onValueChange: (TextFieldValue) -> Unit = { text.value = it },
    singleLine: Boolean = true,
    showBorder: Boolean = true,
    clear: (() -> Unit)? = null,
    maxLines: Int = 1,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .border(
                width = if (showBorder) 1.dp else 0.dp,
                color = AppTheme.colors.backgroundSecondary,
                shape = RoundedCornerShape(50.dp)
            )
            .background(AppTheme.colors.backgroundTertiary)
    ) {
        if (text.value.text.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = AppTheme.dimens.medlarge,
                        vertical = AppTheme.dimens.medium
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                icon?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        tint = AppTheme.colors.contentTertiary.copy(alpha = 0.5f)
                    )
                    Spacer(Modifier.width(AppTheme.dimens.small))
                }
                TextTitle(
                    modifier = Modifier.padding(start = 1.dp),
                    text = placeholder,
                    bold = true,
                    textColor = AppTheme.colors.contentTertiary.copy(alpha = 0.5f)
                )
            }
        }

        val keyboardController = LocalSoftwareKeyboardController.current
        BasicTextField(
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.dimens.medlarge,
                    vertical = AppTheme.dimens.medium
                )
                .fillMaxWidth(),
            value = text.value,
            onValueChange = onValueChange,
            maxLines = maxLines,
            singleLine = singleLine,
            cursorBrush = SolidColor(AppTheme.colors.contentPrimary),
            textStyle = AppTheme.typography.title.copy(
                color = AppTheme.colors.contentPrimary
            ),
            keyboardActions = keyboardActions,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
        )

        if (clear != null && text.value.text.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.Clear,
                tint = AppTheme.colors.contentTertiary,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = AppTheme.dimens.medium)
                    .clickable {
                        text.value = TextFieldValue("")
                        clear()
                        keyboardController?.hide()
                    }
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            val textState = remember { mutableStateOf(TextFieldValue("Input Field")) }
            InputPrimary(
                text = textState,
                placeholder = "https://flashback.pages.dev"
            )
        }
    }
}

@Preview
@Composable
private fun PreviewEmpty() {
    AppThemePreview(isLight = true) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            val textState = remember { mutableStateOf(TextFieldValue("")) }
            InputPrimary(
                text = textState,
                placeholder = "https://flashback.pages.dev"
            )
        }
    }
}