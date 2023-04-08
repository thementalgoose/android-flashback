package tmg.flashback.ui.components.drivers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.R
import tmg.flashback.ui.components.loading.Fade


val driverIconImageSize: Dp = 48.dp
val driverIconBorderSize: Dp = 6.dp

val driverIconSize: Dp
    get() = driverIconImageSize + driverIconBorderSize

@Composable
fun DriverIcon(
    photoUrl: String?,
    modifier: Modifier = Modifier,
    number: Int? = null,
    code: String? = null,
    size: Dp = driverIconImageSize,
    borderSize: Dp = driverIconBorderSize,
    constructorColor: Color? = null,
    defaultShowStats: Boolean = false
) {
    val showStats = remember { mutableStateOf(defaultShowStats) }
    Box(
        modifier = modifier
            .size(size + borderSize)
            .clip(CircleShape)
            .background(constructorColor ?: AppTheme.colors.primary)
//            .clickable(
//                enabled = number != null && code != null,
//                onClick = {
//                    showStats.value = !showStats.value
//                }
//            )
    ) {
        AsyncImage(
            model = photoUrl,
            error = painterResource(id = R.drawable.unknown_avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size)
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(AppTheme.colors.backgroundPrimary),
            contentDescription = null,
        )

        Fade(
            visible = showStats.value,
            modifier = Modifier
                .align(Alignment.Center)
                .size(size)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.7f))
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DriverNumber(
                    number = number!!.toString(),
                    highlightNumber = constructorColor ?: AppTheme.colors.contentPrimary
                )

                DriverNumber(
                    modifier = Modifier.padding(top = AppTheme.dimens.xxsmall),
                    number = code!!.toString(),
                    highlightNumber = Color.White
                )
            }
        }
    }
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
            Row(
                Modifier
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

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        DriverIcon(
            photoUrl = "",
            constructorColor = Color.Red
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewInfo() {
    AppThemePreview {
        DriverIcon(
            photoUrl = "",
            constructorColor = Color.Red,
            defaultShowStats = true,
            number = 16,
            code = "LEC"
        )
    }
}