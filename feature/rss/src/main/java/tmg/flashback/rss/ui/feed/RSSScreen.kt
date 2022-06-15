package tmg.flashback.rss.ui.feed

import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import androidx.core.text.htmlEncode
import androidx.core.text.parseAsHtml
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.viewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.ads.components.NativeBanner
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.messages.Message
import tmg.utilities.extensions.fromHtml

private val badgeSize: Dp = 42.dp

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
            itemClicked = viewModel.inputs::clickModel,
            configureSources = viewModel.inputs::configure,
            settings = viewModel.inputs::settings,
            actionUpClicked = actionUpClicked
        )
    }
}

@Composable
fun RSSScreen(
    list: List<RSSModel>,
    itemClicked: (RSSModel.RSS) -> Unit,
    configureSources: () -> Unit,
    settings: () -> Unit,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                Header(
                    text = stringResource(id = R.string.title_rss),
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    overrideIcons = {
                        IconButton(onClick = settings) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_rss_settings),
                                contentDescription = stringResource(id = R.string.ab_rss_settings),
                                tint = AppTheme.colors.contentPrimary
                            )
                        }
                    },
                    actionUpClicked = actionUpClicked
                )
            }
            items(list, key = { it.key }) {
                when (it) {
                    is RSSModel.RSS -> Item(it, itemClicked)
                    is RSSModel.Message -> Message(stringResource(id = R.string.home_last_updated, it.msg))
                    RSSModel.NoNetwork -> {
                        NetworkError(error = NetworkError.NETWORK_ERROR)
                    }
                    RSSModel.Advert -> {
                        NativeBanner(badgeOffset = true)
                    }
                    RSSModel.InternalError -> {
                        NetworkError(error = NetworkError.INTERNAL_ERROR)
                    }
                    RSSModel.SourcesDisabled -> {
                        SourcesDisabled(sourceClicked = configureSources)
                    }
                }
            }
        }
    )
}

@Composable
private fun Item(
    model: RSSModel.RSS,
    clickItem: (RSSModel.RSS) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = {
            clickItem(model)
        })
        .padding(
            vertical = AppTheme.dimensions.paddingNSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
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
                    .padding(bottom = 4.dp)
            )
            model.item.description?.let { desc ->
                AndroidView(factory = {
                    TextView(it).apply {
                        this.text = desc.split("<br").firstOrNull()
                            ?.trim()
                            ?.fromHtml() ?: ""
                        this.layoutParams = ViewGroup.LayoutParams(
                             ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                        )
                        this.setTextColor(when (AppTheme.isLight) {
                            true -> android.graphics.Color.BLACK
                            false -> android.graphics.Color.WHITE
                        })
                    }
                })
            }
            TextCaption(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
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
            style = AppTheme.typography.title.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Center),
            color = textColour
        )
    }
}

@Composable
private fun SourcesDisabled(
    sourceClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = sourceClicked)
        .padding(
            vertical = AppTheme.dimensions.paddingMedium,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_rss_icon_no_sources),
            contentDescription = null,
            tint = AppTheme.colors.contentSecondary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.width(16.dp))
        TextBody1(
            text = stringResource(id = R.string.rss_no_articles)
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        RSSScreen(
            list = listOf(RSSModel.RSS(item = fakeArticle)),
            itemClicked = {},
            configureSources = {},
            settings = {},
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