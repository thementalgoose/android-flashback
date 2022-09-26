package tmg.flashback.style.input

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    imeAction: ImeAction = ImeAction.Default,
    onValueChange: (TextFieldValue) -> Unit = { text.value = it },
    maxLines: Int = 1,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50)),
        colors = TextFieldDefaults.textFieldColors(
            textColor = AppTheme.colors.contentPrimary,
            disabledTextColor = Color.Transparent,
            backgroundColor = AppTheme.colors.backgroundTertiary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        value = text.value,
        onValueChange = onValueChange,
        maxLines = maxLines,
        placeholder = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
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
                    placeholder,
                    bold = true,
                    textColor = AppTheme.colors.contentTertiary.copy(alpha = 0.5f)
                )
            }
        },
        textStyle = AppTheme.typography.body1,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        )
    )
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