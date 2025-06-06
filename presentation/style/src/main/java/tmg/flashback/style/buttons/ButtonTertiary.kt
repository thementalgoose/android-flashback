package tmg.flashback.style.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2

@Composable
fun ButtonTertiary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    narrow: Boolean = true,
    @DrawableRes
    icon: Int? = null
) {
    Button(
        modifier = modifier
            .focusable(true)
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(0.dp)
            .defaultMinSize(1.dp, 1.dp),
        border = BorderStroke(1.dp, AppTheme.colors.backgroundSecondary),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = AppTheme.colors.backgroundTertiary,
            contentColor = AppTheme.colors.contentSecondary
        ),
        contentPadding = PaddingValues(),
        shape = CircleShape,
        onClick = onClick
    ) {
        TextBody2(
            text,
            bold = true,
            textColor = AppTheme.colors.contentTertiary,
            modifier = Modifier
                .padding(
                    start = AppTheme.dimens.nsmall,
                    top = if (narrow) AppTheme.dimens.small else AppTheme.dimens.medium,
                    end = AppTheme.dimens.nsmall,
                    bottom = if (narrow) AppTheme.dimens.small else AppTheme.dimens.medium
                )
        )
        if (icon != null) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
            Spacer(Modifier.width(AppTheme.dimens.nsmall))
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ButtonTertiary(
            text = "Tertiary Button",
            onClick = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewWithIcon() {
    AppThemePreview {
        ButtonTertiary(
            icon = androidx.core.R.drawable.ic_call_answer_low,
            text = "Tertiary Button",
            onClick = { }
        )
    }
}