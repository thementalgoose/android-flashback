package tmg.flashback.season.presentation.messaging

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import org.threeten.bp.LocalDate
import tmg.flashback.flashbacknews.api.models.news.News
import tmg.flashback.season.R
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.layouts.Container
import tmg.flashback.ui.lifecycle.OnLifecycleEvent
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

private val newsItemWidth: Dp = 250.dp

@Composable
fun News(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel<NewsViewModel>()
) {
    val uiState = viewModel.outputs.uiState.collectAsState()

    OnLifecycleEvent { owner, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.refresh(background = true)
        }
    }

    News(
        modifier = modifier,
        uiState = uiState.value,
        itemClicked = viewModel::itemClicked
    )
}

@Composable
private fun News(
    modifier: Modifier = Modifier,
    uiState: NewsUiState,
    itemClicked: (String) -> Unit
) {
    AnimatedContent(
        modifier = modifier.fillMaxWidth(),
        targetState = uiState,
        label = "news",
        content = {
            if (it !is NewsUiState.NoNews) {
                Container(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppTheme.dimens.small),
                    isOutlined = true
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        TextBody1(
                            text = stringResource(id = string.dashboard_now),
                            bold = true,
                            modifier = Modifier.padding(
                                start = AppTheme.dimens.medium,
                                end = AppTheme.dimens.medium,
                                top = AppTheme.dimens.small,
                                bottom = AppTheme.dimens.small
                            )
                        )

                        val scrollState = rememberScrollState()
                        NewsRow(
                            scrollState = scrollState,
                            itemClicked = itemClicked,
                            list = (it as? NewsUiState.News)?.items ?: emptyList()
                        )
                        Spacer(Modifier.height(AppTheme.dimens.small))
                    }
                }
            }
        }
    )
}

@Composable
private fun Placeholder(
    modifier: Modifier
) {
    Column(modifier = modifier
        .background(AppTheme.colors.backgroundTertiary)
        .placeholder(
            highlight = PlaceholderHighlight.shimmer(
                highlightColor = AppTheme.colors.backgroundSecondary
            ),
            visible = true,
            color = AppTheme.colors.backgroundTertiary,
            shape = RoundedCornerShape(4.dp)
        )
    ) {
        TextBody1(text = "")
        TextBody1(text = "")
        TextBody1(text = "")
    }
}

@Composable
private fun NewsRow(
    scrollState: ScrollState,
    list: List<Pair<LocalDate, List<News>>>,
    itemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
    ) {
        Spacer(Modifier.width(0.dp))
        if (list.isEmpty()) {
            Placeholder(modifier = Modifier.width(newsItemWidth))
        }
        for ((date, newsArticles) in list) {
            Column {
                TextBody2(
                    text = date.format("EEE '${date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "",
                    modifier = Modifier.padding(bottom = AppTheme.dimens.xsmall)
                )
                Row {
                    newsArticles.forEach {
                        Item(
                            modifier = Modifier.width(newsItemWidth),
                            itemClicked = itemClicked,
                            news = it,
                        )
                        Spacer(Modifier.width(AppTheme.dimens.small))
                    }
                }
            }
        }
        Spacer(Modifier.width(0.dp))
    }
}

@Composable
private fun Item(
    news: News,
    itemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(
                enabled = news.url != null,
                onClick = {
                    itemClicked(news.url!!)
                }
            )
            .background(AppTheme.colors.backgroundTertiary)
            .padding(AppTheme.dimens.nsmall)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextBody1(
                modifier = Modifier.weight(1f),
                text = news.message,
            )
            if (news.url != null) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.ic_banner_link),
                    tint = AppTheme.colors.contentTertiary,
                    contentDescription = null
                )
            }
        }
        if (news.image != null) {
            Spacer(Modifier.height(4.dp))
            AsyncImage(
                model = news.image,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(150.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(AppTheme.colors.backgroundTertiaryInverse)
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewNoNews() {
    AppThemePreview {
        News(
            uiState = NewsUiState.NoNews,
            itemClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewLoading() {
    AppThemePreview {
        News(
            uiState = NewsUiState.Loading,
            itemClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNews() {
    AppThemePreview {
        News(
            uiState = NewsUiState.News(
                items = listOf(
                    LocalDate.of(2024, 2, 2) to listOf(
                        fakeNews(),
                        fakeNews(url = null)
                    )
                )
            ),
            itemClicked = { }
        )
    }
}
private fun fakeNews(
    message: String = "News article",
    url: String? = "https://www.google.com",
    image: String? = null
): News = News(
    message = message,
    url = url,
    image = image,
    dateAdded = "2024-02-02"
)