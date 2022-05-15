package tmg.flashback.ui.components.badges

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Badge
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.R

data class Badge(
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int? = null,
)

@Composable
fun BadgesView(
    list: List<Badge>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .padding(start = AppTheme.dimensions.paddingMedium)
        .horizontalScroll(rememberScrollState())
    ) {
        list.forEach {
            BadgeView(model = it)
            Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
        }
    }
}

@Composable
fun BadgeView(
    model: Badge,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
        .background(AppTheme.colors.backgroundSecondary)
        .padding(
            horizontal = AppTheme.dimensions.paddingSmall,
            vertical = AppTheme.dimensions.paddingXSmall
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (model.icon != null) {
            Icon(
                painter = painterResource(id = model.icon),
                contentDescription = null,
                tint = AppTheme.colors.contentPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
        }
        TextBody2(
            text = stringResource(id = model.label),
            bold = true
        )
    }
}

@Preview
@Composable
private fun PreviewListLight() {
    AppThemePreview(isLight = true) {
        BadgesView(list = listOf(fakeMenuBadge, fakeBackIconBadge))
    }
}

@Preview
@Composable
private fun PreviewListDark() {
    AppThemePreview(isLight = false) {
        BadgesView(list = listOf(fakeMenuBadge, fakeBackIconBadge))
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        BadgeView(fakeBackBadge)
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        BadgeView(fakeMenuBadge)
    }
}

private val fakeMenuBadge = Badge(
    label = R.string.ab_menu
)
private val fakeMenuIconBadge = Badge(
    label = R.string.ab_menu,
    icon = R.drawable.ic_menu
)
private val fakeBackBadge = Badge(
    label = R.string.ab_back
)
private val fakeBackIconBadge = Badge(
    label = R.string.ab_back,
    icon = R.drawable.ic_back
)