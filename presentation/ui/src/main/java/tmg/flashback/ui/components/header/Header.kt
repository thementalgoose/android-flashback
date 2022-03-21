package tmg.flashback.ui.components.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.ui.R

@Composable
fun Header(
    text: String,
    icon: Painter,
    iconContentDescription: String,
    actionUpClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.dimensions.paddingXSmall)
    ) {
        IconButton(
            onClick = actionUpClicked
        ) {
            Icon(
                painter = icon,
                contentDescription = iconContentDescription,
                tint = AppTheme.colors.contentPrimary
            )
        }
        TextHeadline1(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingMedium
                )
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        Header(
            text = "2022",
            icon = painterResource(id = R.drawable.ic_menu),
            iconContentDescription = "Menu",
            actionUpClicked = { }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        Header(
            text = "Daniel Ricciardo\n2022",
            icon = painterResource(id = R.drawable.ic_back),
            iconContentDescription = "Menu",
            actionUpClicked = { }
        )
    }
}