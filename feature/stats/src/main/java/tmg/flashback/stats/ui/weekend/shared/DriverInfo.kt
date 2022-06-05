package tmg.flashback.stats.ui.weekend.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.stats.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.utils.isInPreview

private val colorIndicator: Dp = 6.dp

@Composable
fun DriverInfo(
    driver: DriverConstructor,
    position: Int?,
    extraContent: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .width(colorIndicator)
            .background(driver.constructor.colour)
        )
        TextTitle(
            modifier = Modifier
                .width(42.dp)
                .padding(horizontal = AppTheme.dimensions.paddingXSmall)
                .align(Alignment.CenterVertically),
            bold = true,
            textAlign = TextAlign.Center,
            text = position?.toString() ?: ""
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(vertical = 3.dp)
        ) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                TextTitle(
                    text = driver.driver.name,
                    bold = true
                )
            }
            Spacer(Modifier.height(4.dp))
            Row {
                val resourceId = when (isInPreview()) {
                    true -> R.drawable.gb
                    false -> LocalContext.current.getFlagResourceAlpha3(driver.driver.nationalityISO)
                }
                Image(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = resourceId),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
                Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
                extraContent?.let { content ->
                    content()
                    Spacer(Modifier.width(AppTheme.dimensions.paddingXSmall))
                }
                TextBody2(text = driver.constructor.name)
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverConstructor
) {
    AppThemePreview {
        DriverInfo(driver = driverConstructor, position = 1)
    }
}