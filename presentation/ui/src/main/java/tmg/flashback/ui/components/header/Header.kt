package tmg.flashback.ui.components.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextHeadline1

@Composable
fun Header(
    text: String,
    icon: ImageVector,
    iconContentDescription: String,
    actionUpClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = actionUpClicked
        ) {
            Icon(
                imageVector = icon,
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
            icon = Icons.Default.Menu,
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
            icon = Icons.Default.ArrowBack,
            iconContentDescription = "Menu",
            actionUpClicked = { }
        )
    }
}