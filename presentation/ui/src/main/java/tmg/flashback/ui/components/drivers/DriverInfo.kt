package tmg.flashback.ui.components.drivers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.flag.Flag

// TODO Move this out of the shared UI module

private val colorIndicator: Dp = 6.dp

@Composable
fun DriverInfo(
    driverName: String,
    driverNationalityISO: String,
    constructorName: String,
    constructorColor: Color,
    position: Int?,
    modifier: Modifier = Modifier,
    extraContent: (@Composable RowScope.() -> Unit)? = null
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .width(colorIndicator)
            .background(constructorColor)
        )
        if (position != null) {
            TextTitle(
                modifier = Modifier
                    .width(36.dp)
                    .padding(horizontal = AppTheme.dimens.xsmall)
                    .align(Alignment.CenterVertically),
                bold = true,
                textAlign = TextAlign.Center,
                text = position.toString()
            )
        } else {
            Spacer(Modifier.width(AppTheme.dimens.medium - colorIndicator))
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(vertical = 3.dp)
        ) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                TextTitle(
                    text = driverName,
                    bold = true
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Flag(
                    iso = driverNationalityISO,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically),
                )
                Spacer(Modifier.width(AppTheme.dimens.small))
                extraContent?.let { content ->
                    content()
                    Spacer(Modifier.width(AppTheme.dimens.xsmall))
                }
                TextBody2(text = constructorName)
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        DriverInfo(
            driverName = "Daniel Riccardo",
            driverNationalityISO = "GBR",
            constructorName = "Red Bull",
            constructorColor = Color.Red,
            position = 1,
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewShowPosition() {
    AppThemePreview {
        DriverInfo(
            driverName = "Daniel Riccardo",
            driverNationalityISO = "GBR",
            constructorName = "Red Bull",
            constructorColor = Color.Red,
            position = 1,
        )
    }
}