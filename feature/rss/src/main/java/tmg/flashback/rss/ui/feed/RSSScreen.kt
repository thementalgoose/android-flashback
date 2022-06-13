package tmg.flashback.rss.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.viewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.messages.Message

private val badgeSize: Dp = 48.dp

@Composable
fun RSSScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<RSSViewModel>()

    val list = viewModel.outputs.list.observeAsState(emptyList())
    val isLoading = viewModel.outputs.isRefreshing.observeAsState(false)
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isLoading.value),
        onRefresh = viewModel.inputs::refresh
    ) {
        RSSScreen(
            list = list.value,
            actionUpClicked = actionUpClicked
        )
    }
}

@Composable
fun RSSScreen(
    list: List<RSSModel>,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        content = {
            item("header") {
                Header(
                    text = stringResource(id = R.string.title_rss),
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }
            items(list, key = { it.key }) {
                when (it) {
                    is RSSModel.RSS -> Item(it)
                    is RSSModel.Message -> Message(it.msg)
                    RSSModel.NoNetwork -> {
                        TextBody2(text = "No Network")
                    }
                    RSSModel.Advert -> {
                        TextBody2(text = "Advert")
                    }
                    RSSModel.InternalError -> {
                        TextBody2(text = "Internal Error")
                    }
                    RSSModel.SourcesDisabled -> {
                        TextBody2(text = "Sources Disabled")
                    }
                }
            }
        }
    )
}

@Composable
private fun Item(
    model: RSSModel.RSS,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(
        vertical = AppTheme.dimensions.paddingSmall,
        horizontal = AppTheme.dimensions.paddingMedium
    )) {
        SourceBadge(
            title = model.item.source.shortSource
                ?: model.item.source.source.substring(0..2),
            textColour = Color(model.item.source.textColor.toColorInt()),
            colour = Color(model.item.source.colour.toColorInt())
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                text = model.item.title,
                bold = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
            )
            model.item.description?.let { desc ->
                TextBody2(
                    text = desc,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                )
            }
            TextCaption(
                text = "${model.item.source.source} - ${model.item.date?.format(DateTimeFormatter.ofPattern("HH:mm 'at' dd MMM"))}",
            )
        }
    }
}

@Composable
private fun SourceBadge(
    title: String,
    textColour: Color,
    colour: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .size(badgeSize)
        .clip(CircleShape)
        .background(colour)
    ) {
        Text(
            text = title,
            maxLines = 1,
            style = AppTheme.typography.title,
            modifier = Modifier.align(Alignment.Center),
            color = textColour
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        RSSScreen(
            list = listOf(RSSModel.RSS(item = fakeArticle)),
            actionUpClicked = {}
        )
    }
}

private val fakeArticle: Article get() = Article(
    id = "id",
    title = "Title",
    description = "This is a long description of the content of the website for the reader to confume",
    link = "https://www.link.com",
    date = LocalDateTime.now(),
    source = fakeArticleSource,
)

private val fakeArticleSource: ArticleSource get() = ArticleSource(
    title = "title",
    colour = "#123123",
    textColor = "#ffffff",
    rssLink = "https://www.rsslink.com/link",
    source = "https://www.rsslink.com",
    shortSource = "SO",
    contactLink = "https://www.rsslink.com/contact",
)