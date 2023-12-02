package tmg.flashback.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.ui.R
import tmg.flashback.strings.R.string

@Composable
fun BottomSheet(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    backClicked: (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit
){
    Column(modifier = modifier
        .background(AppTheme.colors.backgroundPrimary)
        .navigationBarsPadding()
    ) {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                TextHeadline2(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppTheme.dimens.medium,
                            end = AppTheme.dimens.medium,
                            top = AppTheme.dimens.nsmall
                        )
                )
                TextBody2(
                    text = subtitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = AppTheme.dimens.small,
                            horizontal = AppTheme.dimens.medium
                        )
                )
            }
            if (backClicked != null) {
                IconButton(onClick = backClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = stringResource(id = string.ab_back),
                        tint = AppTheme.colors.contentPrimary
                    )
                }
            }
        }
        content()
        Spacer(Modifier.height(AppTheme.dimens.small))
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        BottomSheet(
            title = "Bottom Sheet",
            subtitle = "See your bottom sheet content here",
            backClicked = { },
            content = {
                Box(
                    Modifier
                        .size(100.dp)
                        .background(Color.Green))
            }
        )
    }
}