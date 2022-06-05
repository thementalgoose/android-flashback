package tmg.flashback.stats.ui.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tmg.flashback.formula1.model.Driver
import tmg.flashback.stats.R
import tmg.flashback.style.AppTheme

@Composable
fun DriverImage(
    driver: Driver,
    modifier: Modifier = Modifier
) {
    DriverImage(
        photoUrl = driver.photoUrl,
        modifier = modifier
    )
}

@Composable
fun DriverImage(
    photoUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
) {
    AsyncImage(
        modifier = modifier
            .size(size)
            .padding(4.dp)
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall)),
        contentScale = ContentScale.Crop,
        model = photoUrl,
        contentDescription = null,
        error = painterResource(id = R.drawable.unknown_avatar)
    )
}