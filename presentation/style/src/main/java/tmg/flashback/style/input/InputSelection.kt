package tmg.flashback.style.input

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.R
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1

@Composable
fun InputSelection(
    label: String,
    @DrawableRes
    icon: Int,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isChecked: Boolean? = null,
    itemClicked: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .clickable(
                enabled = itemClicked != null,
                onClick = itemClicked ?: {}
            )
            .background(
                if (isSelected) AppTheme.colors.backgroundTertiary else Color.Transparent
            )
            .padding(
                vertical = AppTheme.dimens.nsmall,
                horizontal = AppTheme.dimens.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = AppTheme.colors.contentSecondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(AppTheme.dimens.nsmall))
        TextBody1(
            text = label,
            modifier = Modifier.weight(1f)
        )
        if (isChecked != null) {
            Spacer(Modifier.width(AppTheme.dimens.small))
            InputSwitch(
                isChecked = isChecked
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewSwitch(
    @PreviewParameter(BooleanParamProvider::class) isSelected: Boolean
) {
    AppThemePreview {
        Column {
            InputSelection(
                label = "Night mode",
                icon = androidx.core.R.drawable.ic_call_answer_low,
                isSelected = isSelected
            )
            Spacer(Modifier.height(16.dp))
            InputSelection(
                label = "Night mode",
                icon = androidx.core.R.drawable.ic_call_answer_low,
                isSelected = isSelected,
                isChecked = true
            )
            Spacer(Modifier.height(16.dp))
            InputSelection(
                label = "Night mode",
                icon = androidx.core.R.drawable.ic_call_answer_low,
                isSelected = isSelected,
                isChecked = false
            )
        }
    }
}

private class BooleanParamProvider: PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}