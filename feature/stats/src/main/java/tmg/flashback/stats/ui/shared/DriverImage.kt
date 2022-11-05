package tmg.flashback.stats.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tmg.flashback.formula1.model.Driver
import tmg.flashback.stats.R
import tmg.flashback.stats.components.DriverNumber
import tmg.flashback.style.AppTheme

@Composable
fun DriverImage(
    driver: Driver,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    DriverImage(
        photoUrl = driver.photoUrl,
        number = driver.number,
        code = driver.code,
        modifier = modifier,
        size = size
    )
}

@Composable
fun DriverImage(
    photoUrl: String?,
    modifier: Modifier = Modifier,
    number: Int? = null,
    code: String? = null,
    size: Dp = 48.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall)),
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = photoUrl,
            contentDescription = null,
            error = painterResource(id = R.drawable.unknown_avatar)
        )
        if (number != null || code != null) {
            Row(Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundSecondary.copy(alpha = 0.8f))
                .padding(
                    horizontal = AppTheme.dimens.small,
                    vertical = AppTheme.dimens.xsmall
                )
                .align(Alignment.BottomCenter)
            ) {
                code?.let { code ->
                    DriverNumber(
                        modifier = Modifier.weight(1f),
                        number = code,
                        textAlign = TextAlign.Start
                    )
                }
                number?.let { number ->
                    DriverNumber(
                        modifier = Modifier.weight(1f),
                        number = number.toString(),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}